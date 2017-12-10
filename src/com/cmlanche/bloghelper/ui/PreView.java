package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.utils.BucketUtils;
import com.cmlanche.bloghelper.utils.ResManager;
import com.cmlanche.bloghelper.utils.UIUtils;
import com.cmlanche.bloghelper.utils.Utils;
import com.fx.base.mvvm.CustomView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by cmlanche on 2017/12/4.
 * 图片预览界面
 */
public class PreView extends CustomView {

    @FXML
    ImageView imageView;
    @FXML
    Label o_nameLabel;
    @FXML
    Label o_sizeLabel;
    @FXML
    Label n_nameLabel;
    @FXML
    Label n_sizeLabel;
    @FXML
    Button operationBtn;
    @FXML
    VBox detailBox;
    @FXML
    Label tipLabel;
    @FXML
    Label compressRatioLabel;

    private BucketFile bucketFile;

    @Override
    protected void onViewCreated() {

    }

    /**
     * 加载一个文件到预览界面
     *
     * @param bucketFile
     */
    public void loadFile(BucketFile bucketFile) {
        if (bucketFile != null) {
            this.bucketFile = bucketFile;

            o_nameLabel.setText(bucketFile.getName());
            o_sizeLabel.setText(Utils.getSizeName(bucketFile.getSize()));
            int status = BucketUtils.getBucketFileStauts(bucketFile);
            detailBox.getStyleClass().clear();
            detailBox.getStyleClass().add(UIUtils.getStatusBackground(status));

            File optimizeFile = new File(BucketUtils.getLocalBucketfileOptimizedFilePath(bucketFile));
            long size = 0;
            if (optimizeFile.exists()) {
                size = FileUtils.sizeOf(optimizeFile);
            }
            switch (status) {
                case BucketUtils.NORMAL:
                    n_nameLabel.setText("-");
                    n_sizeLabel.setText("-");
                    compressRatioLabel.setText("-");
                    operationBtn.setText("下载");
                    operationBtn.setUserData("download");
                    break;
                case BucketUtils.DOWNLOADED:
                    n_nameLabel.setText("-");
                    n_sizeLabel.setText("-");
                    compressRatioLabel.setText("-");
                    operationBtn.setText("优化");
                    operationBtn.setUserData("optimize");
                    break;
                case BucketUtils.OPTIMIZEED:
                    n_nameLabel.setText(optimizeFile.getName());
                    n_sizeLabel.setText(Utils.getSizeName(size));
                    compressRatioLabel.setText(String.format("-%.2f%%",
                            (1 - (float) FileUtils.sizeOf(optimizeFile) / bucketFile.getSize()) * 100));
                    operationBtn.setText("上传");
                    operationBtn.setUserData("upload");
                    break;
                case BucketUtils.OPTIMIZED_UPLOADED:
                    n_nameLabel.setText(optimizeFile.getName());
                    n_sizeLabel.setText(Utils.getSizeName(size));
                    compressRatioLabel.setText(String.format("-%.2f%%",
                            (1 - (float) FileUtils.sizeOf(optimizeFile) / bucketFile.getSize()) * 100));
                    operationBtn.setText("优化");
                    operationBtn.setUserData("optimize");
                    break;
            }

            if (ResManager.getInstance().existImage(bucketFile)) {
                // 从压缩的图片中取，如果没有则进行压缩处理
                File file;
                if (ResManager.getInstance().existOptimizedImage(bucketFile)) {
                    file = new File(BucketUtils.getLocalBucketfileOptimizedFilePath(bucketFile));
                } else {
                    file = new File(BucketUtils.getLocalBucketFilePath(bucketFile));
                }
                if (UIUtils.isGif(bucketFile.getMineType())) {
                    file = new File("cache/cmlanchecom/test.gif");
                }
                showImage(file, bucketFile.getMineType());
            } else {
                Logger.info(tag, "未下载：" + bucketFile.getName());
                imageView.setImage(null);
            }
        }
    }

    /**
     * 展示图像
     *
     * @param file
     */
    private void showImage(File file, String mineType) {
        if (file.exists()) {
            long filesize = FileUtils.sizeOf(file);
            if (filesize <= 5 * 1024 * 1024) {
                if (UIUtils.isJpg(mineType) || UIUtils.isPng(mineType) || UIUtils.isGif(mineType)) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        Image image = new Image(fis);
                        tipLabel.setVisible(false);
                        imageView.setVisible(true);
                        imageView.setImage(image);
                        fis.close();
                    } catch (IOException e) {
                        Logger.error(tag, e.getMessage(), e);
                    }
                } else if (UIUtils.isGif(mineType)) {

                }
            } else {
                // 文件大于5M，不宜展示，建议先优化处理
                imageView.setImage(null);
                imageView.setVisible(false);
                tipLabel.setVisible(true);
                tipLabel.setText("文件大于5M，请先优化处理");
            }
        } else {
            // 提示文件不存在
            imageView.setImage(null);
            imageView.setVisible(false);
            tipLabel.setVisible(true);
            tipLabel.setText("文件不存在");
        }
    }
}

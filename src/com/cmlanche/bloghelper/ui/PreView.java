package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.downloader.DownloadListener;
import com.cmlanche.bloghelper.downloader.FileDownloader;
import com.cmlanche.bloghelper.model.BucketFile;
import com.fx.base.mvvm.CustomView;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * Created by cmlanche on 2017/12/4.
 * 图片预览界面
 */
public class PreView extends CustomView {

    @FXML
    ImageView imageView;

    private BucketFile bucketFile;

    @Override
    protected void init() {

    }

    @Override
    protected void initView() {
    }

    /**
     * 加载一个文件到预览界面
     *
     * @param bucketFile
     */
    public void loadFile(BucketFile bucketFile) {
        if (bucketFile != null) {
            this.bucketFile = bucketFile;

            FileDownloader.getInstance().download(bucketFile, new DownloadListener() {
                @Override
                public void onWating() {

                }

                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(int progress) {

                }

                @Override
                public void onFinished(String path) {
//                    FileInputStream fis = null;
//                    try {
//                        fis = new FileInputStream(new File(path));
//                        Image image = new Image(fis);
//                        imageView.setImage(image);
//                        fis.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
}

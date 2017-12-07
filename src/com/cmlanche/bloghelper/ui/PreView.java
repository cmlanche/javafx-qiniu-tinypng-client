package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.downloader.DownloadListener;
import com.cmlanche.bloghelper.downloader.FileDownloader;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.tinypng.TinypngManger;
import com.cmlanche.bloghelper.utils.BucketUtils;
import com.cmlanche.bloghelper.utils.ResManager;
import com.fx.base.mvvm.CustomView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

            if (ResManager.getInstance().existImage(bucketFile)) {
                // 从压缩的图片中取，如果没有则进行压缩处理
                if (ResManager.getInstance().existCompressedImage(bucketFile)) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(new File(BucketUtils.getLocalBucketfileCompressedFilePath(bucketFile)));
                        Image image = new Image(fis);
                        imageView.setImage(image);
                        fis.close();
                    } catch (IOException e) {
                        Logger.error(tag, e.getMessage(), e);
                    }
                } else {
                    // 进入压缩
                    if ("image/png".equals(bucketFile.getMineType())
                            || "image/jpg".equals(bucketFile.getMineType())) {
                        TinypngManger.getInstance().compress(bucketFile);
                    }
                }
            } else {
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
}

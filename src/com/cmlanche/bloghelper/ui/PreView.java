package com.cmlanche.bloghelper.ui;

import com.cmlanche.bloghelper.model.BucketFile;
import com.fx.base.mvvm.CustomView;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

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

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(bucketFile.getUrl()).build();
            Call call = httpClient.newCall(request);
            try {
                Response response = call.execute();
                InputStream inputStream = response.body().byteStream();
                Image image = new Image(inputStream);
                imageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.cmlanche.bloghelper.downloader;

/**
 * Created by cmlanche on 2017/12/5.
 */
public interface DownloadListener {

    void onWating();

    void onStart();

    void onProgress(int progress);

    void onFinished(byte[] bytes, String path);

    void onError(String error);
}

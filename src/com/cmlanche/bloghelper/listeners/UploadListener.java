package com.cmlanche.bloghelper.listeners;

/**
 * Created by cmlanche on 2017/12/10.
 */
public interface UploadListener {

    void prepare();

    void uploading();

    void finish();

    void error(String message);
}

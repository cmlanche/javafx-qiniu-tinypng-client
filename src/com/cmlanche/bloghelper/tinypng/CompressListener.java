package com.cmlanche.bloghelper.tinypng;

/**
 * Created by cmlanche on 2017/12/10.
 */
public interface CompressListener {

    void prepare();

    void compressing();

    void finish();

    void error(String message);
}

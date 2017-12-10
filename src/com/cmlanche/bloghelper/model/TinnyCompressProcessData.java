package com.cmlanche.bloghelper.model;

/**
 * Created by cmlanche on 2017/12/10.
 * 优化进度数据
 */
public class TinnyCompressProcessData extends ProcessData {

    private String error;

    public TinnyCompressProcessData(int state) {
        super(state);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

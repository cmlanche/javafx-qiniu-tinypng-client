package com.cmlanche.bloghelper.model;

/**
 * Created by cmlanche on 2017/12/10.
 */
public class UploadProcessData extends ProcessData {

    private String error;

    public UploadProcessData(int state) {
        super(state);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

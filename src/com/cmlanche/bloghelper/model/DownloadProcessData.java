package com.cmlanche.bloghelper.model;

/**
 * Created by cmlanche on 2017/12/10.
 * 下载进度数据
 */
public class DownloadProcessData extends ProcessData {

    private int progress;

    private String error;

    public DownloadProcessData(int state) {
        super(state);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

package com.cmlanche.bloghelper.downloader;

/**
 * Created by cmlanche on 2017/12/10.
 * 下载状态信息
 */
public class DlState {

    private String url;
    private int progress;

    public DlState(String url) {
        this.url = url;
    }

    public DlState(String url, int progress) {
        this.url = url;
        this.progress = progress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

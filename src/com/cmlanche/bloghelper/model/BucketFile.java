package com.cmlanche.bloghelper.model;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class BucketFile {

    private String bucket;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件hash值
     */
    private String hash;

    /**
     * 文件类型
     */
    private String mineType;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 更新时间
     */
    private long updateTime;

    /**
     * 图像的宽高大小
     */
    private String whSize;

    /**
     * 文件url地址
     */
    private String url;

    /**
     * 进度信息
     */
    private ProcessData process;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getWhSize() {
        return whSize;
    }

    public void setWhSize(String whSize) {
        this.whSize = whSize;
    }

    public ProcessData getProcess() {
        return process;
    }

    public void setProcess(ProcessData process) {
        this.process = process;
    }
}

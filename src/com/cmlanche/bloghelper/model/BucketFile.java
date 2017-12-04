package com.cmlanche.bloghelper.model;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class BucketFile {

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String mineType;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 文件url地址
     */
    private String url;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

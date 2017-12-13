package com.cmlanche.bloghelper.ui.optimize;

/**
 * Created by cmlanche on 2017/12/13.
 * 图片优化设置数据
 */
public class PngOptimzeResponse {

    private int width;
    private int height;
    private boolean isUse;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }
}

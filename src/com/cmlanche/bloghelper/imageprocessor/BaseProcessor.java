package com.cmlanche.bloghelper.imageprocessor;

/**
 * Created by cmlanche on 2017/12/7.
 */
public class BaseProcessor implements IProcessor {

    private String filePath;

    public BaseProcessor(String filePath) {
        this.filePath = filePath;
    }
}

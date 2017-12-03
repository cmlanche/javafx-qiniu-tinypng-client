package com.cmlanche.bloghelper.common;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class Config {

    static final String tag = "config";

    private static class ConfigHolder {
        static Config config = new Config();
    }

    private Config() {
        load();
    }

    private void load() {
        FileInputStream fis = null;
        try {
            configProp = new Properties();
            fis = new FileInputStream(new File("./config/config.properties"));
            configProp.load(fis);
        } catch (IOException e) {
            Logger.error(tag, e.getMessage(), e);
        }
    }

    private void save() {
        try {
            FileOutputStream fos = new FileOutputStream(new File("./config/config.properties"));
            configProp.store(fos, "save config at " + System.currentTimeMillis());
        } catch (IOException e) {
            Logger.error(tag, e.getMessage(), e);
        }
    }

    private Properties configProp;
    private String qiniuAccessKey;
    private String qiniuSecretKey;
    private String lastestBucket;

    public static Config getInstance() {
        return ConfigHolder.config;
    }

    public String getQiniuAccessKey() {
        if (StringUtils.isEmpty(qiniuAccessKey)) {
            qiniuAccessKey = configProp.getProperty("qiniu.accesskey");
        }
        return qiniuAccessKey;
    }

    public void setQiniuAccessKey(String qiniuAccessKey) {
        this.qiniuAccessKey = qiniuAccessKey;
        configProp.setProperty("qiniu.accesskey", qiniuAccessKey);
        save();
    }

    public String getQiniuSecretKey() {
        if (StringUtils.isEmpty(qiniuSecretKey)) {
            qiniuSecretKey = configProp.getProperty("qiniu.secretkey");
        }
        return qiniuSecretKey;
    }

    public void setQiniuSecretKey(String qiniuSecretKey) {
        this.qiniuSecretKey = qiniuSecretKey;
        configProp.setProperty("qiniu.secretkey", qiniuSecretKey);
        save();
    }

    public String getLastestBucket() {
        if (StringUtils.isEmpty(lastestBucket)) {
            lastestBucket = configProp.getProperty("qiniu.lastest.bucket");
        }
        return lastestBucket;
    }

    public void setLastestBucket(String lastestBucket) {
        if (StringUtils.isEmpty(lastestBucket)) return;
        this.lastestBucket = lastestBucket;
        configProp.setProperty("qiniu.lastest.bucket", lastestBucket);
    }
}

package com.cmlanche.bloghelper.utils;

import com.cmlanche.bloghelper.model.BucketFile;

import java.io.File;

/**
 * Created by cmlanche on 2017/12/5.
 */
public class BucketUtils {

    public static String getBucketCacheFilePath(BucketFile bucketFile) {
        return "cache" + File.separator + bucketFile.getBucket() + File.separator + bucketFile.getName();
    }
}

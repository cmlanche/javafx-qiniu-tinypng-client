package com.cmlanche.bloghelper.utils;

import com.cmlanche.bloghelper.model.BucketFile;

import java.io.File;

/**
 * Created by cmlanche on 2017/12/5.
 */
public class BucketUtils {

    /**
     * 获取bucket的路径
     *
     * @param bucketFile
     * @return
     */
    public static String getLocalBucketPath(BucketFile bucketFile) {
        return "cache" + File.separator + bucketFile.getBucket() + File.separator;
    }

    /**
     * 获取本地bucketfile的文件路径
     *
     * @param bucketFile
     * @return
     */
    public static String getLocalBucketFilePath(BucketFile bucketFile) {
        return getLocalBucketPath(bucketFile) + bucketFile.getName();
    }

    /**
     * 获取被压缩的bucket的路径
     *
     * @param bucketFile
     * @return
     */
    public static String getLocalBucketCompressedFilePath(BucketFile bucketFile) {
        return getLocalBucketPath(bucketFile) + "compressed" + File.separator;
    }

    /**
     * 获取bucketfile的被压缩的文件路径
     *
     * @param bucketFile
     * @return
     */
    public static String getLocalBucketfileCompressedFilePath(BucketFile bucketFile) {
        return getLocalBucketCompressedFilePath(bucketFile) + String.format("compressed_%s", bucketFile.getName());
    }
}

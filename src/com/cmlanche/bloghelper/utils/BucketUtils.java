package com.cmlanche.bloghelper.utils;

import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.qiniu.QETag;

import java.io.File;

/**
 * Created by cmlanche on 2017/12/5.
 */
public class BucketUtils {

    public static final String tag = "BucketUtils";

    /**
     * 正常状态
     */
    public static final int NORMAL = 0;
    /**
     * 已下载的状态
     */
    public static final int DOWNLOADED = 1;
    /**
     * 已经下载并且被优化了
     */
    public static final int OPTIMIZEED = 2;
    /**
     * 已经下载，已经被优化，并且已经上传了
     */
    public static final int OPTIMIZED_UPLOADED = 3;

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
    public static String getLocalBucketfileOptimizedFilePath(BucketFile bucketFile) {
        return getLocalBucketCompressedFilePath(bucketFile) + String.format("compressed_%s", bucketFile.getName());
    }

    /**
     * 获取bucketfile的文件状态
     *
     * @param bucketFile
     * @return
     */
    public static int getBucketFileStauts(BucketFile bucketFile) {
        try {
            File optimizedFile = new File(getLocalBucketfileOptimizedFilePath(bucketFile));
            if (optimizedFile.exists()) {
                String hash = new QETag().calcETag(optimizedFile.getPath());
                if (bucketFile.getHash().equals(hash)) {
                    return OPTIMIZED_UPLOADED;
                }
                return OPTIMIZEED;
            } else {
                File file = new File(getLocalBucketFilePath(bucketFile));
                if (file.exists()) {
                    return DOWNLOADED;
                }
            }
        } catch (Exception ex) {
            Logger.error(tag, ex.getMessage(), ex);
        }

        return NORMAL;
    }

}

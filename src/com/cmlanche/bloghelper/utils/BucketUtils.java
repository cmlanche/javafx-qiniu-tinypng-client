package com.cmlanche.bloghelper.utils;

import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.qiniu.QETag;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

    /**
     * 获取图片宽高尺寸
     *
     * @param bucketFile
     * @return
     */
    public static String getPictureSize(BucketFile bucketFile) {
        File file = new File(BucketUtils.getLocalBucketFilePath(bucketFile));
        if (file.exists()) {
            if (UIUtils.isJpg(bucketFile.getMineType()) || UIUtils.isPng(bucketFile.getMineType())) {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    BufferedImage bufferedImage = ImageIO.read(fis);
                    String size;
                    if (bufferedImage != null) {
                        size = String.format("%dx%d", bufferedImage.getWidth(), bufferedImage.getHeight());
                    } else {
                        size = "未能读取图片尺寸";
                    }
                    fis.close();
                    return size;
                } catch (IOException e) {
                    Logger.error(tag, e.getMessage(), e);
                }
            } else if (UIUtils.isGif(bucketFile.getMineType())) {
                try {
                    //要操作的图片
                    FileInputStream is = new FileInputStream(file);
                    //把图片读取读取到内存的流
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte buffer[] = new byte[1024];
                    int leng = 0;
                    while ((leng = is.read(buffer)) != -1) {
                        bos.write(buffer, 0, leng);
                    }
                    //截取第一张图
                    String size;
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bos.toByteArray(), 0, bos.size()));
                    if (bufferedImage != null) {
                        size = String.format("%dx%d", bufferedImage.getWidth(), bufferedImage.getHeight());
                    } else {
                        size = "未能读取图片尺寸";
                    }
                    is.close();
                    bos.close();
                    return size;
                } catch (Exception e) {
                    Logger.error(tag, e.getMessage(), e);
                }
            }
        } else {
            return "-";
        }
        return "";
    }

}

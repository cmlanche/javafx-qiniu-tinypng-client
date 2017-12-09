package com.cmlanche.bloghelper.utils;

import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.model.BucketFile;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by cmlanche on 2017/12/6.
 * 资源管理器
 */
public class ResManager {

    private static final String tag = "ResManager";

    private static class ResManagerHolder {
        static ResManager instance = new ResManager();
    }

    private ResManager() {
    }

    public static ResManager getInstance() {
        return ResManagerHolder.instance;
    }

    /**
     * 获取一个图片
     *
     * @param bucketFile
     * @return
     */
    public Image getImage(BucketFile bucketFile) {
        File file = new File(BucketUtils.getLocalBucketFilePath(bucketFile));
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                return new Image(fileInputStream);
            } catch (FileNotFoundException e) {
                Logger.error(tag, e.getMessage(), e);
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * 判断某个bucketfile是否存在
     *
     * @param bucketFile
     * @return
     */
    public boolean existImage(BucketFile bucketFile) {
        if (bucketFile == null) return false;
        return new File(BucketUtils.getLocalBucketFilePath(bucketFile)).exists();
    }

    /**
     * 某个bucketfile是否有压缩的文件（用于预览）
     *
     * @param bucketFile
     * @return
     */
    public boolean existOptimizedImage(BucketFile bucketFile) {
        if (bucketFile == null) return false;
        return new File(BucketUtils.getLocalBucketfileOptimizedFilePath(bucketFile)).exists();
    }
}

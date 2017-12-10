package com.cmlanche.bloghelper.tinypng;

import com.cmlanche.bloghelper.common.Config;
import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.utils.BucketUtils;
import com.tinify.Source;
import com.tinify.Tinify;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cmlanche on 2017/12/7.
 */
public class TinypngManger {

    private static final String tag = "TinypngManger";

    private static class TinypngManagerHolder {
        static TinypngManger instance = new TinypngManger();
    }

    private TinypngManger() {
        Tinify.setKey(Config.getInstance().getTinyToken());
        pool = Executors.newFixedThreadPool(5);
        bucketFileMap = new HashMap<>();
    }

    public static TinypngManger getInstance() {
        return TinypngManagerHolder.instance;
    }

    private ExecutorService pool;
    private Map<String, BucketFile> bucketFileMap;

    /**
     * 添加一个压缩文件的任务
     *
     * @param bucketFile
     * @return
     */
    public void compress(BucketFile bucketFile, CompressListener compressListener) {
        if (bucketFileMap.containsKey(bucketFile.getHash())) {
            return;
        }
        if (compressListener != null) compressListener.prepare();
        bucketFileMap.put(bucketFile.getHash(), bucketFile);
        pool.submit(() -> {
            File file = new File(BucketUtils.getLocalBucketFilePath(bucketFile));
            if (file.exists()) {
                File outFilePath = new File(BucketUtils.getLocalBucketCompressedFilePath(bucketFile));
                if (!outFilePath.exists()) {
                    outFilePath.mkdirs();
                }
                String outfile = BucketUtils.getLocalBucketfileOptimizedFilePath(bucketFile);
                try {
                    Logger.info(tag, "use tinypng compress file: " + file);
                    if (compressListener != null) compressListener.compressing();
                    Source source = Tinify.fromFile(file.getPath());
                    source.toFile(outfile);
                    if (compressListener != null) compressListener.finish();
                } catch (IOException e) {
                    Logger.error(tag, e.getMessage(), e);
                    if (compressListener != null) compressListener.error(e.getMessage());
                }
            } else {
                if (compressListener != null) compressListener.error("文件不存在！");
                Logger.error(tag, "the bucket file is not exist: " + file);
            }
            bucketFileMap.remove(bucketFile.getHash());
        });
    }

    /**
     * 判断某个文件是否在压缩中
     *
     * @param bucketFile
     * @return
     */
    public boolean isCompressing(BucketFile bucketFile) {
        if (bucketFile == null) return false;
        return bucketFileMap.containsKey(bucketFile.getHash());
    }
}

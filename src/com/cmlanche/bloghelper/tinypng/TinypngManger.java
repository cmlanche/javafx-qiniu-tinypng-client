package com.cmlanche.bloghelper.tinypng;

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
        Tinify.setKey("HkrQMvyzirhHrcSctf9C1FPXUdlK5Sfn");
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
    public void compress(BucketFile bucketFile) {
        if (bucketFileMap.containsKey(bucketFile.getHash())) {
            return;
        }
        bucketFileMap.put(bucketFile.getHash(), bucketFile);
        pool.submit(() -> {
            File file = new File(BucketUtils.getLocalBucketFilePath(bucketFile));
            if (file.exists()) {
                File outFilePath = new File(BucketUtils.getLocalBucketCompressedFilePath(bucketFile));
                if (!outFilePath.exists()) {
                    outFilePath.mkdirs();
                }
                String outfile = BucketUtils.getLocalBucketfileCompressedFilePath(bucketFile);
                try {
                    Logger.info(tag, "use tinypng compress file: " + outfile);
                    Source source = Tinify.fromFile(file.getPath());
                    source.toFile(outfile);
                } catch (IOException e) {
                    Logger.error(tag, e.getMessage(), e);
                }
            } else {
                Logger.error(tag, "the bucket file is not exist: " + file);
            }
            bucketFileMap.remove(bucketFile.getHash());
        });
    }
}

package com.cmlanche.bloghelper.downloader;

import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.utils.BucketUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cmlanche on 2017/12/5.
 * <p>
 * 文件下载器
 */
public class FileDownloader {

    private static final String tag = "FileDownloader";
    private static final int POOLSIZE = 10;

    private static class FileDownloaderHolder {
        static FileDownloader instance = new FileDownloader();
    }

    private FileDownloader() {
        pool = Executors.newFixedThreadPool(POOLSIZE);
    }

    public static FileDownloader getInstance() {
        return FileDownloaderHolder.instance;
    }

    private ExecutorService pool;
    private OkHttpClient okHttpClient;

    /**
     * 下载一个文件
     */
    public void download(BucketFile bucketFile, DownloadListener listener) {
        if (bucketFile == null || listener == null) return;
        listener.onWating();
        pool.submit(() -> {
            Logger.info(tag, "begin to download:" + bucketFile.getUrl());
            listener.onStart();

            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient();
            }
            Request request = new Request.Builder().url(bucketFile.getUrl()).build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String filePath = BucketUtils.getBucketCacheFilePath(bucketFile);
                File path = new File(filePath);
                if (path.exists()) {
                    path.delete();
                }
                path.getParentFile().mkdirs();

                FileOutputStream fos = new FileOutputStream(path);
                ResponseBody rb = response.body();
                if (rb != null) {
                    byte[] bytes = rb.bytes();
                    if (bytes != null) {
                        fos.write(bytes);
                        fos.close();
                        listener.onFinished(filePath);
                    } else {
                        Logger.error(tag, "the bytes of body is null");
                    }
                } else {
                    Logger.error(tag, "the body of response is null");
                }
                Logger.info(tag, "downloaded:" + filePath);
            } catch (IOException e) {
                Logger.error(tag, e.getMessage(), e);
                listener.onError(e.getMessage());
            }
        });
    }
}

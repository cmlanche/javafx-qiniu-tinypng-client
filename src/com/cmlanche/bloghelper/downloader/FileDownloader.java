package com.cmlanche.bloghelper.downloader;

import com.cmlanche.bloghelper.common.Logger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cmlanche on 2017/12/5.
 * <p>
 * 文件下载器
 */
public class FileDownloader {

    private static final String tag = "FileDownloader";

    private static class FileDownloaderHolder {
        static FileDownloader instance = new FileDownloader();
    }

    private FileDownloader() {
    }

    public static FileDownloader getInstance() {
        return FileDownloaderHolder.instance;
    }

    private OkHttpClient okHttpClient;
    private Map<String, String> runningTask = new HashMap<>();

    /**
     * 下载一个文件
     */
    public void download(String url, String savePath, String name, DownloadListener listener) {
        if (StringUtils.isEmpty(url) || listener == null) return;

        if (runningTask.containsKey(url)) {
            Logger.info(tag, "the url " + url + " is downloading, skip it");
            return;
        }
        runningTask.put(url, url);

        listener.onWating();
        Logger.info(tag, "begin to download:" + url);
        listener.onStart();
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onError(e.getMessage());
                runningTask.remove(url);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File filePath = new File(savePath);
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    fos = new FileOutputStream(new File(filePath, name));
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Logger.debug(tag, "download " + url + " " + progress + "%");
                        // 下载中
                        listener.onProgress(progress);
                    }
                    fos.flush();
                    // 下载完成
                    runningTask.remove(url);
                    listener.onFinished(savePath + name);
                } catch (Exception e) {
                    runningTask.remove(url);
                    listener.onError(e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        Logger.error(tag, e.getMessage(), e);
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        Logger.error(tag, e.getMessage(), e);
                    }
                }
            }
        });
    }
}

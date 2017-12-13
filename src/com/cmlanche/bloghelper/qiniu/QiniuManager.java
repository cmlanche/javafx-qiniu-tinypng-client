package com.cmlanche.bloghelper.qiniu;

import com.cmlanche.bloghelper.common.Config;
import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.listeners.UploadListener;
import com.cmlanche.bloghelper.model.BucketFile;
import com.cmlanche.bloghelper.model.ProcessData;
import com.cmlanche.bloghelper.utils.BucketUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cmlanche on 2017/12/3.
 */
public class QiniuManager {

    static final String tag = "ResManager";

    private static class ResManagerHolder {
        static QiniuManager instacne = new QiniuManager();
    }

    private QiniuManager() {
    }

    public static QiniuManager getInstance() {
        return ResManagerHolder.instacne;
    }

    private Auth auth;
    private BucketManager bucketManager;
    private UploadManager uploadManager;
    private Configuration cfg;

    private Map<String, BucketFile> uploadData;
    private ExecutorService uploadPool;

    public void load() {
        cfg = new Configuration(Zone.autoZone());
        auth = Auth.create(Config.getInstance().getQiniuAccessKey(),
                Config.getInstance().getQiniuSecretKey());
        bucketManager = new BucketManager(auth, cfg);
        uploadManager = new UploadManager(cfg);

        uploadData = new HashMap<>();
        uploadPool = Executors.newFixedThreadPool(10);
    }

    /**
     * 获取当前账号下的所有bucket
     *
     * @return
     */
    public String[] getBuckets() {
        if (bucketManager == null) {
            load();
        }
        try {
            return bucketManager.buckets();
        } catch (QiniuException e) {
            Logger.error(tag, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取某个bucket的文件
     *
     * @param bucket 空间名称
     * @param prefix 文件前缀
     * @param limit  每次迭代的长度，最大1000，推荐1000
     * @return
     */
    public FileListing getFiles(String bucket, String prefix, int limit) {
        if (bucketManager == null) {
            load();
        }
        try {
            return bucketManager.listFiles(bucket, prefix, "", limit, "");
        } catch (QiniuException e) {
            Logger.error(tag, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取某个bucket的所有文件
     *
     * @param bucket
     * @param limit
     * @return
     */
    public FileListing getFiles(String bucket, int limit) {
        return getFiles(bucket, "", limit);
    }

    /**
     * 获取某个bucket的域名列表
     *
     * @param bucket
     * @return
     */
    public String[] getDomains(String bucket) {
        try {
            return bucketManager.domainList(bucket);
        } catch (QiniuException e) {
            Logger.error(tag, e.getMessage(), e);
        }
        return null;
    }

    /**
     * 重命名
     *
     * @param bucketFile
     * @param newName
     */
    public boolean rename(BucketFile bucketFile, String newName) {
        if (bucketFile == null || StringUtils.isEmpty(newName)) return false;
        try {
            if (newName.equals(bucketFile.getName())) return true;
            Response response = bucketManager.rename(bucketFile.getBucket(), bucketFile.getName(), newName);
            Logger.info(tag, response.toString());
            return true;
        } catch (QiniuException e) {
            Logger.error(tag, e.getMessage(), e);
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param bucketFile
     * @return
     */
    public boolean delete(BucketFile bucketFile) {
        if (bucketFile == null) return false;
        try {
            bucketManager.delete(bucketFile.getBucket(), bucketFile.getName());
            return true;
        } catch (QiniuException e) {
            Logger.error(tag, e.getMessage(), e);
        }
        return false;
    }

    /**
     * 更新bucket信息
     *
     * @param bucketFile
     */
    public void updateStat(BucketFile bucketFile) {
        try {
            FileInfo fileInfo = bucketManager.stat(bucketFile.getBucket(), bucketFile.getName());
            if (fileInfo != null) {
                bucketFile.setName(fileInfo.key);
                bucketFile.setMineType(fileInfo.mimeType);
                bucketFile.setSize(fileInfo.fsize);
                bucketFile.setUpdateTime(fileInfo.putTime / 10000); // 七牛云的文件时间单位为100纳秒
                bucketFile.setHash(fileInfo.hash);
                // 计算图像的宽高大小，当文件已经被下载了才去计算
                bucketFile.setWhSize(BucketUtils.getPictureSize(bucketFile));
                bucketFile.setProcess(new ProcessData(ProcessData.IDLE));
            }
        } catch (QiniuException e) {
            Logger.error(tag, e.getMessage(), e);
        }
    }

    /**
     * (覆盖)上传文件
     *
     * @param bucketFile
     * @return
     */
    public void upload(BucketFile bucketFile, UploadListener uploadListener) {
        if (bucketFile == null) return;
        if (uploadListener == null) return;
        if (uploadData.containsKey(bucketFile.getUrl())) {
            return;
        }
        uploadData.put(bucketFile.getUrl(), bucketFile);
        uploadListener.prepare();

        uploadPool.submit(() -> {
            uploadListener.uploading();
            String upToken = auth.uploadToken(bucketFile.getBucket(), bucketFile.getName());
            Logger.info(tag, "上传token：" + upToken);
            try {
                Response response = uploadManager.put(BucketUtils.getLocalBucketfileOptimizedFilePath(bucketFile),
                        bucketFile.getName(), upToken);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                Logger.info(tag, "上传成功：" + putRet.key + " - " + putRet.hash);
                uploadListener.finish();
            } catch (QiniuException ex) {
                Response r = ex.response;
                Logger.error(tag, ex.getMessage(), ex);
                uploadListener.error(ex.getMessage());
                try {
                    Logger.error(tag, r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            } finally {
                uploadData.remove(bucketFile.getUrl());
            }
        });
    }

    /**
     * 是否正在下载中...
     *
     * @param bucketFile
     * @return
     */
    public boolean isUploading(BucketFile bucketFile) {
        if (bucketFile == null) return false;
        return uploadData.containsKey(bucketFile.getUrl());
    }
}

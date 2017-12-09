package com.cmlanche.bloghelper.qiniu;

import com.cmlanche.bloghelper.common.Config;
import com.cmlanche.bloghelper.common.Logger;
import com.cmlanche.bloghelper.model.BucketFile;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;

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

    public void load() {
        Configuration cfg = new Configuration(Zone.zone0());
        auth = Auth.create(Config.getInstance().getQiniuAccessKey(),
                Config.getInstance().getQiniuSecretKey());
        bucketManager = new BucketManager(auth, cfg);
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
    public void rename(BucketFile bucketFile, String newName) {
        if (bucketFile == null || StringUtils.isEmpty(newName)) return;
        try {
            if (newName.equals(bucketFile.getName())) return;
            Response response = bucketManager.rename(bucketFile.getBucket(), bucketFile.getName(), newName);
            Logger.info(tag, response.toString());
        } catch (QiniuException e) {
            Logger.error(tag, e.getMessage(), e);
        }
    }
}

package com.cmlanche.bloghelper.utils;

/**
 * Created by cmlanche on 2017/12/9.
 */
public class UIUtils {

    public static String getGlobalStylesheet() {
        return "/com/cmlanche/bloghelper/ui/application.css";
    }

    public static String getStatusBackground(int status) {
        switch (status) {
            case BucketUtils.NORMAL:
                return "status-normal-bg";
            case BucketUtils.DOWNLOADED:
                return "status-download-bg";
            case BucketUtils.OPTIMIZEED:
                return "status-optimize-bg";
            case BucketUtils.OPTIMIZED_UPLOADED:
                return "status-uploaded-bg";
        }
        return "";
    }

    public static boolean isPng(String minetype) {
        return "image/png".equalsIgnoreCase(minetype);
    }

    public static boolean isJpg(String minetype) {
        return "image/jpg".equalsIgnoreCase(minetype);
    }

    public static boolean isGif(String minetype) {
        return "image/gif".equalsIgnoreCase(minetype);
    }
}

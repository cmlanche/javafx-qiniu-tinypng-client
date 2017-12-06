package com.cmlanche.bloghelper.common;

/**
 * Created by cmlanche on 2017/12/3.
 * 日志
 */
public class Logger {

    private static boolean Debug = true;

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(Logger.class);

    public static void info(String tag, String message) {
        logger.info(String.format("[%s] %s", tag, message));
    }

    public static void debug(String tag, String message) {
        if (Debug) {
            logger.info(String.format("[%s] %s", tag, message));
        }
    }

    public static void error(String tag, String message, Throwable throwable) {
        if (throwable != null) {
            logger.error(String.format("[%s] %s", tag, message), throwable);
        } else {
            logger.error(String.format("[%s] %s", tag, message));
        }
    }

    public static void error(String tag, String message) {
        error(tag, message, null);
    }
}

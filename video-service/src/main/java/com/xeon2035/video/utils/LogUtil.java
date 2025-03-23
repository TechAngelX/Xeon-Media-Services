package com.xeon2035.video.utils;
import java.util.logging.Logger;

public class LogUtil {

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}

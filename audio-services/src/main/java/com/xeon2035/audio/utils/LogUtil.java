// LogUtil.java
package com.xeon2035.audio.utils;

import java.util.logging.Logger;

public class LogUtil {

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}

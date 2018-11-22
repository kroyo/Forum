package com.kroyoshi.forum.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by Kroyoshi on 16/7/25.
 * Log  输出检测数据
 */
public class L {
    private static final boolean DEBUG = true;

    public static void d(String message) {
        if (DEBUG) Logger.d(message);
    }

    public static void v(String message) {
        if (DEBUG) Logger.v(message);
    }

    public static void e(String message) {
        if (DEBUG) Logger.e(message);
    }

    public static void w(String message) {
        if (DEBUG) Logger.w(message);
    }

    public static void wtf(String message) {
        if (DEBUG) Logger.wtf(message);
    }

    public static void json(String message) {
        if (DEBUG) Logger.json(message);
    }

    public static void xml(String message) {
        if (DEBUG) Logger.xml(message);
    }

}

package com.example.taobaoalliance.utils;

import android.util.Log;

import com.example.taobaoalliance.BuildConfig;

public class LogUtils {

    private static int current_Lev = BuildConfig.DEBUG ? 4 : 0;
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;

    public static void d(Object object, String log) {
        if (current_Lev >= DEBUG_LEV) {
            Log.d(object.getClass().getSimpleName(), log);
        }
    }

    public static void i(Object object, String log) {
        if (current_Lev >= INFO_LEV) {
            Log.i(object.getClass().getSimpleName(), log);
        }
    }

    public static void w(Object object, String log) {
        if (current_Lev >= WARNING_LEV) {
            Log.w(object.getClass().getSimpleName(), log);
        }
    }

    public static void e(Object object, String log) {
        if (current_Lev >= ERROR_LEV) {
            Log.e(object.getClass().getSimpleName(), log);
        }
    }
}

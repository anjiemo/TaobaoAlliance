package com.example.taobaoalliance.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.taobaoalliance.base.BaseApplication;

/**
 * 功能：隐藏或显示键盘
 */
public class KeyboardUtil {
    public static boolean hide(View view) {
        InputMethodManager im = (InputMethodManager) BaseApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean show(View view) {
        InputMethodManager im = (InputMethodManager) BaseApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.showSoftInput(view, 0);
    }
}

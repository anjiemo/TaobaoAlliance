package com.example.taobaoalliance.utils;

public class UrlUtils {

    public static String getCoverPath(String pict_url, int size) {
        return "http:" + pict_url + "_" + size + "x" + size + ".jpg";
    }
}

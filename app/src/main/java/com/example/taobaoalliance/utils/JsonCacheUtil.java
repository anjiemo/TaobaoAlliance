package com.example.taobaoalliance.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telecom.Call;

import com.blankj.utilcode.util.GsonUtils;
import com.example.taobaoalliance.base.BaseApplication;
import com.example.taobaoalliance.model.domain.CacheWithDuration;

public class JsonCacheUtil {

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private static volatile JsonCacheUtil singleton = null;
    private final SharedPreferences mSharedPreferences;

    private JsonCacheUtil() {
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
    }

    public void saveCache(String key, Object value) {
        saveCache(key, value, -1L);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        String valueStr = GsonUtils.toJson(value);
        if (duration != -1) {
            //当前时间
            duration += System.currentTimeMillis();
        }
        //保存一个有数据有时间的内容
        CacheWithDuration cacheWithDuration = new CacheWithDuration(valueStr, duration);
        String cacheWithTime = GsonUtils.toJson(cacheWithDuration);
        edit.putString(key, cacheWithTime);
        edit.apply();
    }

    public void delCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> tClazz) {
        String valueWithDuration = mSharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = GsonUtils.fromJson(valueWithDuration, CacheWithDuration.class);
        //对时间进行判断
        long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() <= 0) {
            //判断是否过期了
            //过期了
            return null;
        } else {
            //没过期
            String cache = cacheWithDuration.getCache();
            return GsonUtils.fromJson(cache, tClazz);
        }
    }

    public static JsonCacheUtil getInstance() {
        if (singleton == null) {
            synchronized (JsonCacheUtil.class) {
                if (singleton == null) {
                    singleton = new JsonCacheUtil();
                }
            }
        }
        return singleton;
    }
}

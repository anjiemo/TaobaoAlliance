package com.example.taobaoalliance.model.domain;

public class CacheWithDuration {

    private String cache;
    private long duration;

    public CacheWithDuration(String cache, long duration) {
        this.cache = cache;
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

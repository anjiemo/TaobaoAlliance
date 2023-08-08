package com.example.taobaoalliance.utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static final String TAG = "RetrofitManager";
    private static final RetrofitManager ourInstance = new RetrofitManager();
    private final Retrofit mRetrofit;

    public static RetrofitManager getOurInstance() {
        return ourInstance;
    }

    private RetrofitManager() {
        // 创建 OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}

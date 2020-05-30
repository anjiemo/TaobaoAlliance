package com.example.taobaoalliance.presenter.impl;

import android.util.Log;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.Categories;
import com.example.taobaoalliance.presenter.IHomePresenter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.RetrofitManager;
import com.example.taobaoalliance.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    @Override
    public void getCategories() {
        //加载分类数据
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //数据结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "result code is =======> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    Categories categories = response.body();
                    LogUtils.d(HomePresenterImpl.this,  "result ===========> " + categories.toString());
                } else {
                    //请求失败
                    LogUtils.i(HomePresenterImpl.this, "===========> 请求失败....");
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败的结果
                // TODO: 2020/5/30
                LogUtils.e(HomePresenterImpl.this, "==============> 请求错误....");
            }
        });
    }

    @Override
    public void registerCallback(IHomeCallback callback) {

    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {

    }
}

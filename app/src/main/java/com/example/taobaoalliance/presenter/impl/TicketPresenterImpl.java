package com.example.taobaoalliance.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.TicketParams;
import com.example.taobaoalliance.model.domain.TicketResult;
import com.example.taobaoalliance.presenter.ITickPresenter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.RetrofitManager;
import com.example.taobaoalliance.utils.UrlUtils;
import com.example.taobaoalliance.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITickPresenter {

    @Override
    public void getTicket(String title, String url, String cover) {
        String targetUrl = UrlUtils.getTicketUrl(url);
        //去获取淘口令
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(title, targetUrl);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(@NonNull Call<TicketResult> call, @NonNull Response<TicketResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    TicketResult ticketResult = response.body();
                    LogUtils.d(TicketPresenterImpl.this,"result =====> "+ticketResult);
                } else {
                    //请求失败

                }
            }

            @Override
            public void onFailure(@NonNull Call<TicketResult> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {

    }
}

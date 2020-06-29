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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITickPresenter {

    private List<ITicketPagerCallback> mViewCallbacks = new ArrayList<>();
    private String mCover = null;
    private TicketResult mTicketResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        mCover = cover;
        onTicketLoading();
        LogUtils.d(this, "title =====> " + title);
        LogUtils.d(this, "url =====> " + url);
        LogUtils.d(this, "cover =====> " + cover);
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
                    mTicketResult = response.body();
                    LogUtils.d(TicketPresenterImpl.this, "result =====> " + mTicketResult);
                    //通知UI更新
                    onTicketLoadedSuccess();
                } else {
                    //请求失败
                    onLoadedTicketError();
                    mCurrentState = LoadState.ERROR;
                }
            }

            @Override
            public void onFailure(@NonNull Call<TicketResult> call, @NonNull Throwable t) {
                //失败
                onLoadedTicketError();
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallbacks != null) {
            for (ITicketPagerCallback viewCallback : mViewCallbacks) {
                viewCallback.onTicketLoaded(mCover, mTicketResult);
            }
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallbacks != null) {
            for (ITicketPagerCallback viewCallback : mViewCallbacks) {
                viewCallback.onError();
            }
        } else {
            mCurrentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        if (!mViewCallbacks.contains(callback)) {
            mViewCallbacks.add(callback);
        }
        if (mCurrentState != LoadState.NONE) {
            //说明状态已经改变了
            //更新UI
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadedSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadedTicketError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoading();
            }
        }
    }

    private void onTicketLoading() {
        if (mViewCallbacks != null) {
            for (ITicketPagerCallback viewCallback : mViewCallbacks) {
                viewCallback.onLoading();
            }
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        mViewCallbacks.remove(callback);
    }
}

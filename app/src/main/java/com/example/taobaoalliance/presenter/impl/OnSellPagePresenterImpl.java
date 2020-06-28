package com.example.taobaoalliance.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.OnSellContent;
import com.example.taobaoalliance.presenter.IOnSellPagePresenter;
import com.example.taobaoalliance.utils.RetrofitManager;
import com.example.taobaoalliance.utils.UrlUtils;
import com.example.taobaoalliance.view.IOnSellPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterImpl implements IOnSellPagePresenter {

    private List<IOnSellPageCallback> mViewCallbacks = new ArrayList<>();
    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private final Api mApi;

    public OnSellPagePresenterImpl() {
        //获取特惠内容
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getOnSellContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //通知UI状态为加载中...
        for (IOnSellPageCallback viewCallback : mViewCallbacks) {
            viewCallback.onLoading();
        }
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(@NonNull Call<OnSellContent> call, @NonNull Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OnSellContent> call, @NonNull Throwable t) {
                onError();
            }
        });
    }

    private void onSuccess(OnSellContent result) {
        for (IOnSellPageCallback viewCallback : mViewCallbacks) {
            if (isEmpty(result)) {
                viewCallback.onEmpty();
            } else {
                viewCallback.onContentLoadedSuccess(result);
            }
        }
    }

    private boolean isEmpty(OnSellContent content) {
        int size = 0;
        try {
            size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return size == 0;
    }

    private void onError() {
        mIsLoading = false;
        for (IOnSellPageCallback viewCallback : mViewCallbacks) {
            viewCallback.onError();
        }
    }

    @Override
    public void reLoad() {
        //重新加载
        getOnSellContent();
    }

    /**
     * 当前加载状态
     */
    private boolean mIsLoading = false;

    @Override
    public void loaderMore() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //加载更多
        mCurrentPage++;
        //去加载更多内容
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(@NonNull Call<OnSellContent> call, @NonNull Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onMoreLoaded(result);
                } else {
                    onMoreLoadedError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OnSellContent> call, @NonNull Throwable t) {
                onMoreLoadedError();
            }
        });
    }

    private void onMoreLoadedError() {
        mIsLoading = false;
        mCurrentPage--;
        for (IOnSellPageCallback viewCallback : mViewCallbacks) {
            viewCallback.onMoreLoadedError();
        }
    }

    /**
     * 加载更多的结果，通知UI更新。
     *
     * @param result
     */
    private void onMoreLoaded(OnSellContent result) {
        for (IOnSellPageCallback viewCallback : mViewCallbacks) {
            if (isEmpty(result)) {
                mCurrentPage--;
                viewCallback.onMoreLoadedEmpty();
            } else {
                viewCallback.onMoreLoaded(result);
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSellPageCallback callback) {
        if (!mViewCallbacks.contains(callback)) {
            mViewCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IOnSellPageCallback callback) {
        mViewCallbacks.remove(callback);
    }
}

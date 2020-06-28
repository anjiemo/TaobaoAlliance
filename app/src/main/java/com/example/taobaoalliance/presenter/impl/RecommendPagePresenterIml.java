package com.example.taobaoalliance.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.RecommendContent;
import com.example.taobaoalliance.model.domain.RecommendPageCategory;
import com.example.taobaoalliance.presenter.IRecommendPagePresenter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.RetrofitManager;
import com.example.taobaoalliance.utils.UrlUtils;
import com.example.taobaoalliance.view.IRecommendPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Url;

public class RecommendPagePresenterIml implements IRecommendPagePresenter {

    private final Api mApi;
    private RecommendPageCategory.DataBean mCurrentCategoryItem = null;

    public RecommendPagePresenterIml() {
        //拿到Retrofit
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    private List<IRecommendPageCallback> mViewCallbacks = new ArrayList<>();

    @Override
    public void getCategories() {
        for (IRecommendPageCallback viewCallback : mViewCallbacks) {
            viewCallback.onLoading();
        }
        Call<RecommendPageCategory> task = mApi.getRecommendPageCategories();
        task.enqueue(new Callback<RecommendPageCategory>() {
            @Override
            public void onResponse(@NonNull Call<RecommendPageCategory> call, @NonNull Response<RecommendPageCategory> response) {
                int resultCode = response.code();
                LogUtils.d(RecommendPagePresenterIml.this, "getCategories result code ========> " + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    RecommendPageCategory result = response.body();
                    //通知UI更新
                    for (IRecommendPageCallback viewCallback : mViewCallbacks) {
                        viewCallback.onCategoriesLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendPageCategory> call, @NonNull Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        for (IRecommendPageCallback viewCallback : mViewCallbacks) {
            viewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(RecommendPageCategory.DataBean item) {
        mCurrentCategoryItem = item;
        int categoryId = item.getFavorites_id();
        LogUtils.d(this,"getContentByCategory categoryId =======> " + categoryId);
        String targetUrl = UrlUtils.getRecommendPageContentUrl(categoryId);
        Call<RecommendContent> task = mApi.getRecommendPageContent(targetUrl);
        task.enqueue(new Callback<RecommendContent>() {
            @Override
            public void onResponse(@NonNull Call<RecommendContent> call, @NonNull Response<RecommendContent> response) {
                int resultCode = response.code();
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    RecommendContent result = response.body();
                    for (IRecommendPageCallback viewCallback : mViewCallbacks) {
                        viewCallback.onContentLoaded(result);
                    }
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendContent> call, @NonNull Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reloadContent() {
        if (mCurrentCategoryItem != null) {
            getContentByCategory(mCurrentCategoryItem);
        }
    }

    @Override
    public void registerViewCallback(IRecommendPageCallback callback) {
        if (!mViewCallbacks.contains(callback)) {
            mViewCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(IRecommendPageCallback callback) {
        mViewCallbacks.remove(callback);
    }
}

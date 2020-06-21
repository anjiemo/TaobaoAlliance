package com.example.taobaoalliance.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.HomePagerContent;
import com.example.taobaoalliance.presenter.ICategoryPagerPresenter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.RetrofitManager;
import com.example.taobaoalliance.view.ICategoryCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();
    public static final int DEFAULT_PAGE = 1;

    private CategoryPagePresenterImpl() {

    }

    private static ICategoryPagerPresenter sInstance = null;

    public static ICategoryPagerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagePresenterImpl();
        }
        return sInstance;
    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId){
                callback.onLoading();
            }
        }
        //根据分类id去加载内容
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = api.getHomePagerContent(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(@NonNull Call<HomePagerContent> call, @NonNull Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this, "onResponse===========> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this, "pagerContent============> " + pagerContent);
                    //把数据给UI更新
                    handleHomePageContentResult(pagerContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomePagerContent> call, @NonNull Throwable t) {
                LogUtils.d(this, "onFailure===========> " + t.toString());
                handleNetworkError(categoryId);
            }
        });
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    private void handleHomePageContentResult(HomePagerContent pagerContent, int categoryId) {
        //通知UI层更新数据
        for (ICategoryCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    callback.onContentLoaded(pagerContent.getData());
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryCallback> mCallbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryCallback callback) {
        mCallbacks.remove(callback);
    }
}

package com.example.taobaoalliance.presenter.impl;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.HomePagerContent;
import com.example.taobaoalliance.presenter.ICategoryPagerPresenter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.RetrofitManager;
import com.example.taobaoalliance.view.ICategoryCallback;
import com.google.gson.internal.LazilyParsedNumber;
import com.vondear.rxui.fragment.FragmentLazy;

import java.net.HttpURLConnection;
import java.util.HashMap;
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
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this, "onResponse===========> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this, "pagerContent============> " + pagerContent);
                } else {
                    // TODO: 2020/6/21
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(this, "onFailure===========> " + t.toString());
            }
        });
    }

    @Override
    public void loadMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ICategoryCallback callback) {

    }
}

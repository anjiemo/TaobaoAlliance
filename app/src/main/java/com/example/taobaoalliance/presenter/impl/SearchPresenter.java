package com.example.taobaoalliance.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.SearchRecommend;
import com.example.taobaoalliance.presenter.ISearchPresenter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.RetrofitManager;
import com.example.taobaoalliance.view.ISearchPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {

    private List<ISearchPageCallback> mSearchPageCallbacks = new ArrayList<>();
    private final Api mApi;

    private SearchPresenter() {
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getHistories() {

    }

    @Override
    public void delHistories() {

    }

    @Override
    public void doSearch(String keyword) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void loaderMore() {

    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(@NonNull Call<SearchRecommend> call, @NonNull Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this, "getRecommendWords result code ====>" + code);
                if (code == HttpURLConnection.HTTP_OK && response.body() != null) {
                    for (ISearchPageCallback callback : mSearchPageCallbacks) {
                        callback.onRecommendWordsLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchRecommend> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
        if (!mSearchPageCallbacks.contains(callback)) {
            mSearchPageCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallback callback) {
        mSearchPageCallbacks.remove(callback);
    }
}

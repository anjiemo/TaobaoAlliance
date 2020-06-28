package com.example.taobaoalliance.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobaoalliance.model.Api;
import com.example.taobaoalliance.model.domain.Histories;
import com.example.taobaoalliance.model.domain.SearchRecommend;
import com.example.taobaoalliance.model.domain.SearchResult;
import com.example.taobaoalliance.presenter.ISearchPresenter;
import com.example.taobaoalliance.utils.JsonCacheUtil;
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
    public static final int DEFAULT_PAGE = 0;
    //搜索的当前页面
    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyword = null;
    private final JsonCacheUtil mJsonCacheUtil;

    public SearchPresenter() {
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (histories != null && histories.getHistories() != null && histories.getHistories().size() != 0) {
            for (ISearchPageCallback callback : mSearchPageCallbacks) {
                callback.onHistoriesLoaded(histories.getHistories());
            }
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
    }

    public static final String KEY_HISTORIES = "key_histories";
    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private int mHistoriesMaxSize = DEFAULT_HISTORIES_SIZE;

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        List<String> historiesList = null;
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        //如果已经存在了，就干掉，然后再添加
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            historiesList.remove(history);
        }
        //去重完成
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        //对个数进行限制
        if (historiesList.size() > mHistoriesMaxSize) {
            historiesList.subList(0, mHistoriesMaxSize);
        }
        //添加记录
        historiesList.add(history);
        //保存记录
        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)) {
            saveHistory(keyword);
            mCurrentKeyword = keyword;
        }
        //更新UI状态
        onLoading();
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchResult> call, @NonNull Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this, "doSearch result code ===> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //处理结果
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResult> call, @NonNull Throwable t) {
                onError();
            }
        });
    }

    private void onLoading() {
        for (ISearchPageCallback callback : mSearchPageCallbacks) {
            callback.onLoading();
        }
    }

    private void onError() {
        for (ISearchPageCallback callback : mSearchPageCallbacks) {
            callback.onError();
        }
    }

    private void handleSearchResult(SearchResult result) {
        if (isResultEmpty(result)) {
            //数据为空
            onEmpty();
        } else {
            for (ISearchPageCallback callback : mSearchPageCallbacks) {
                callback.onSearchSuccess(result);
            }
        }
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    public void refresh() {
        if (mCurrentKeyword == null) {
            onEmpty();
        } else {
            //可以重新搜索
            doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loaderMore() {
        mCurrentPage++;
        //进行搜索
        if (mCurrentKeyword == null) {
            onEmpty();
        } else {
            //做搜索的事情
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchResult> call, @NonNull Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this, "doSearch result code ===> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //处理结果
                    handleMoreSearchResult(response.body());
                } else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResult> call, @NonNull Throwable t) {
                onLoaderMoreError();
            }
        });
    }

    /**
     * 处理加载更多的结果
     *
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if (isResultEmpty(result)) {
            //数据为空
            onLoadMoreEmpty();
        } else {
            for (ISearchPageCallback callback : mSearchPageCallbacks) {
                callback.onMoreLoaded(result);
            }
        }
    }

    private void onLoadMoreEmpty() {
        for (ISearchPageCallback callback : mSearchPageCallbacks) {
            callback.onMoreLoadedEmpty();
        }
    }

    /**
     * 加载更多内容失败
     */
    private void onLoaderMoreError() {
        mCurrentPage--;
        for (ISearchPageCallback callback : mSearchPageCallbacks) {
            callback.onMoreLoaderError();
        }
    }

    private void onEmpty() {
        for (ISearchPageCallback callback : mSearchPageCallbacks) {
            callback.onEmpty();
        }
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

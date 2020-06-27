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
    private Integer mCurrentPage;

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        //根据分类id去加载内容
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(@NonNull Call<HomePagerContent> call, @NonNull Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this, "onResponse===========> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this, "pagerContent============> " + pagerContent);
                    //把数据给UI更新
                    if (pagerContent != null) {
                        handleHomePageContentResult(pagerContent, categoryId);
                        return;
                    }
                }
                handleNetworkError(categoryId);
            }

            @Override
            public void onFailure(@NonNull Call<HomePagerContent> call, @NonNull Throwable t) {
                LogUtils.d(this, "onFailure===========> " + t.toString());
                handleNetworkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        Retrofit retrofit = RetrofitManager.getOurInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        return api.getHomePagerContent(categoryId, targetPage);
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
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (ICategoryCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (data == null || data.size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        //加载更多的数据
        //1、拿到当前页面
        mCurrentPage = pagesInfo.get(categoryId);
        if (mCurrentPage == null) {
            mCurrentPage = DEFAULT_PAGE;
        }
        //2、页码++
        mCurrentPage++;
        //3、加载数据
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        //4、处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(@NonNull Call<HomePagerContent> call, @NonNull Response<HomePagerContent> response) {
                //结果
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this, "result code ====> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this, "result data =====> " + result);
                    handleLoadMoreResult(categoryId, result);
                } else {
                    //请求失败
                    handleLoadMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomePagerContent> call, @NonNull Throwable t) {
                //请求失败
                LogUtils.d(CategoryPagePresenterImpl.this, t.toString());
                handleLoadMoreError(categoryId);
            }
        });
    }

    private void handleLoadMoreResult(int categoryId, HomePagerContent result) {
        for (ICategoryCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoaderMoreEmpty();
                } else {
                    callback.onLoaderMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoadMoreError(int categoryId) {
        mCurrentPage--;
        pagesInfo.put(categoryId, mCurrentPage);
        for (ICategoryCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoaderMoreError();
            }
        }
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

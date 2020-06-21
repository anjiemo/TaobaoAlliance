package com.example.taobaoalliance.view;

import com.example.taobaoalliance.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryCallback {

    /**
     * 数据加载回来
     *
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 加载中
     *
     * @param categoryId
     */
    void onLoading(int categoryId);

    /**
     * 加载出错
     *
     * @param categoryId
     */
    void onError(int categoryId);

    /**
     * 数据为空
     *
     * @param categoryId
     */
    void onEmpty(int categoryId);

    /**
     * 加载更多时网络错误
     *
     * @param categoryId
     */
    void onLoaderMoreError(int categoryId);

    /**
     * 没有更多内容
     *
     * @param categoryId
     */
    void onLoaderMoreEmpty(int categoryId);

    /**
     * 加载到了更多内容
     *
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图内容加载到了
     *
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);
}

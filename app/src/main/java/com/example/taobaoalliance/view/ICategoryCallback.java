package com.example.taobaoalliance.view;

import com.example.taobaoalliance.base.IBaseCallback;
import com.example.taobaoalliance.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryCallback  extends IBaseCallback {

    /**
     * 数据加载回来
     *
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();

    /**
     * 加载更多时网络错误
     *
     */
    void onLoaderMoreError();

    /**
     * 没有更多内容
     *
     */
    void onLoaderMoreEmpty();

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

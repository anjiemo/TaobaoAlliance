package com.example.taobaoalliance.presenter;

import com.example.taobaoalliance.base.IBasePresenter;
import com.example.taobaoalliance.view.ISearchViewCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchViewCallback> {

    /**
     * 获取搜索历史内容
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 搜素
     *
     * @param keyword
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void refresh();

    /**
     * 获取更多的搜索内容
     */
    void loaderMore();

    /**
     * 获取推荐词
     */
    void getRecommendWords();
}

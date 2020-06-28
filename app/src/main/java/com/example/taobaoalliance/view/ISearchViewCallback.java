package com.example.taobaoalliance.view;

import com.example.taobaoalliance.base.IBaseCallback;
import com.example.taobaoalliance.model.domain.SearchResult;

import java.util.List;

public interface ISearchViewCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     *
     * @param histories
     */
    void onHistoriesLoaded(List<String> histories);

    /**
     * 历史记录删除完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果：成功
     *
     * @param results
     */
    void onSearchSuccess(List<SearchResult> results);

    /**
     * 加载到了更多内容
     *
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多时网络出错
     */
    void onMoreLoaderError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();

    /**
     * 推荐词获取结果
     *
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<String> recommendWords);
}

package com.example.taobaoalliance.view;

import com.example.taobaoalliance.base.IBaseCallback;
import com.example.taobaoalliance.model.domain.RecommendContent;
import com.example.taobaoalliance.model.domain.RecommendPageCategory;

public interface IRecommendPageCallback extends IBaseCallback {

    /**
     * 分类内容结果
     *
     * @param categories 分类内容
     */
    void onCategoriesLoaded(RecommendPageCategory categories);

    /**
     * 内容
     *
     * @param content
     */
    void onContentLoaded(RecommendContent content);
}

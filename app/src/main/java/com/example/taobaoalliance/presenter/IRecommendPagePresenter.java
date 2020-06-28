package com.example.taobaoalliance.presenter;

import com.example.taobaoalliance.base.IBasePresenter;
import com.example.taobaoalliance.model.domain.RecommendPageCategory;
import com.example.taobaoalliance.view.IRecommendPageCallback;

public interface IRecommendPagePresenter extends IBasePresenter<IRecommendPageCallback> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取分类内容
     *
     * @param item
     */
    void getContentByCategory(RecommendPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();
}

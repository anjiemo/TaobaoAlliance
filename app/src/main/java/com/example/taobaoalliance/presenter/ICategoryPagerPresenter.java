package com.example.taobaoalliance.presenter;

import com.example.taobaoalliance.base.IBasePresenter;
import com.example.taobaoalliance.view.ICategoryCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryCallback> {

    /**
     * 根据分类Id去获取分类内容
     *
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void loadMore(int categoryId);

    void reload(int categoryId);
}

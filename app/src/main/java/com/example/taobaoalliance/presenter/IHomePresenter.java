package com.example.taobaoalliance.presenter;

import com.example.taobaoalliance.base.IBasePresenter;
import com.example.taobaoalliance.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {

    /**
     * 获取商品分类
     */

    void getCategories();
}

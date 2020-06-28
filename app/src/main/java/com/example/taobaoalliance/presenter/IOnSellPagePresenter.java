package com.example.taobaoalliance.presenter;

import com.example.taobaoalliance.base.IBasePresenter;
import com.example.taobaoalliance.view.IOnSellPageCallback;

public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallback> {
    /**
     * 加载特惠内容
     *
     */
    void getOnSellContent();

    /**
     * 重新加载内容
     * @Call 网络出问题，恢复网络以后
     */
    void reLoad();

    /**
     * 加载更多特惠内容
     */
    void loaderMore();
}

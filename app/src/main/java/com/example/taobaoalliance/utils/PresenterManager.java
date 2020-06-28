package com.example.taobaoalliance.utils;

import com.example.taobaoalliance.presenter.ICategoryPagerPresenter;
import com.example.taobaoalliance.presenter.IHomePresenter;
import com.example.taobaoalliance.presenter.IOnSellPagePresenter;
import com.example.taobaoalliance.presenter.IRecommendPagePresenter;
import com.example.taobaoalliance.presenter.ISearchPresenter;
import com.example.taobaoalliance.presenter.ITickPresenter;
import com.example.taobaoalliance.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaoalliance.presenter.impl.HomePresenterImpl;
import com.example.taobaoalliance.presenter.impl.OnSellPagePresenterImpl;
import com.example.taobaoalliance.presenter.impl.RecommendPagePresenterIml;
import com.example.taobaoalliance.presenter.impl.SearchPresenter;
import com.example.taobaoalliance.presenter.impl.TicketPresenterImpl;

public class PresenterManager {
    private static volatile PresenterManager singleton = null;
    private final ICategoryPagerPresenter mCategoryPagePresenter;
    private final IHomePresenter mHomePresenter;
    private final ITickPresenter mTicketPresenter;
    private final IRecommendPagePresenter mRecommendPagePresenter;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public static PresenterManager getInstance() {
        if (singleton == null) {
            synchronized (PresenterManager.class) {
                if (singleton == null) {
                    singleton = new PresenterManager();
                }
            }
        }
        return singleton;
    }

    public ICategoryPagerPresenter getCategoryPagePresenter() {
        return mCategoryPagePresenter;
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ITickPresenter getTicketPresenter() {
        return mTicketPresenter;
    }

    public IRecommendPagePresenter getRecommendPagePresenter() {
        return mRecommendPagePresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return mOnSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }

    private PresenterManager() {
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresenter = new TicketPresenterImpl();
        mRecommendPagePresenter = new RecommendPagePresenterIml();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
        mSearchPresenter = new SearchPresenter();
    }
}

package com.example.taobaoalliance.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.Categories;
import com.example.taobaoalliance.model.domain.HomePagerContent;
import com.example.taobaoalliance.presenter.ICategoryPagerPresenter;
import com.example.taobaoalliance.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaoalliance.utils.Constants;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.view.ICategoryCallback;

import java.util.List;

public class HomePagerFragment extends BaseFragment implements ICategoryCallback {

    private ICategoryPagerPresenter mCategoryPagerPresenter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment fragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = CategoryPagePresenterImpl.getInstance();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        // TODO: 2020/6/21 加载数据
        LogUtils.d(this, "=======title：" + title);
        LogUtils.d(this, "=======materialId：" + materialId);
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryId(materialId);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLoading(int categoryId) {

    }

    @Override
    public void onError(int categoryId) {

    }

    @Override
    public void onEmpty(int categoryId) {

    }

    @Override
    public void onLoaderMoreError(int categoryId) {

    }

    @Override
    public void onLoaderMoreEmpty(int categoryId) {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }
}

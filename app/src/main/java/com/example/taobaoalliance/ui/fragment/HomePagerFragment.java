package com.example.taobaoalliance.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.Categories;
import com.example.taobaoalliance.utils.Constants;
import com.example.taobaoalliance.utils.LogUtils;

public class HomePagerFragment extends BaseFragment {

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
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        // TODO: 2020/6/21 加载数据
        LogUtils.d(this, "=======title：" + title);
        LogUtils.d(this, "=======materialId：" + materialId);
    }
}

package com.example.taobaoalliance.ui.fragment;

import android.view.View;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;

public class RecommendFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

    }
}

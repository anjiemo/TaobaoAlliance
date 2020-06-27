package com.example.taobaoalliance.ui.activity;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseActivity;
import com.example.taobaoalliance.model.domain.TicketResult;
import com.example.taobaoalliance.presenter.ITickPresenter;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.view.ITicketPagerCallback;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private ITickPresenter mTicketPresenter;

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
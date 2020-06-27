package com.example.taobaoalliance.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseActivity;
import com.example.taobaoalliance.model.domain.TicketResult;
import com.example.taobaoalliance.presenter.ITickPresenter;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.utils.UrlUtils;
import com.example.taobaoalliance.view.ITicketPagerCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private ITickPresenter mTicketPresenter;
    @BindView(R.id.ticket_cover)
    ImageView mCover;
    @BindView(R.id.ticket_back_press)
    View mBackPress;
    @BindView(R.id.ticket_code)
    EditText mTicketCode;
    @BindView(R.id.ticket_copy_open_btn)
    TextView mOpenCopyBtn;
    @BindView(R.id.ticket_cover_loading)
    View mLoadingView;
    @BindView(R.id.ticket_load_retry)
    View mRetryLoadText;

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
    protected void initEvent() {
        mBackPress.setOnClickListener(v -> finish());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String coverPath = UrlUtils.getTicketUrl(cover);
            Glide.with(this).load(coverPath).error(R.mipmap.no_image).placeholder(R.mipmap.no_image).into(mCover);
        }
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            mTicketCode.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mRetryLoadText != null) {
            mRetryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (mRetryLoadText != null) {
            mRetryLoadText.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
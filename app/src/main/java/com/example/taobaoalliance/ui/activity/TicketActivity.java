package com.example.taobaoalliance.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseActivity;
import com.example.taobaoalliance.model.domain.TicketResult;
import com.example.taobaoalliance.presenter.ITickPresenter;
import com.example.taobaoalliance.utils.CornerTransform;
import com.example.taobaoalliance.utils.LogUtils;
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
    private boolean mHasTaoBaoApp = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (mTicketPresenter != null) {
            mTicketPresenter.registerViewCallback(this);
        }
        //判断是否有安装淘宝
        // act=android.intent.action.MAIN
        // cat[android.intent.category.LAUNCHER]
        // flag=0x10200000
        // cmp=com.taobao.taobao/com.taobao.taobao.welcome.Welcome
        //包名是这个：com.taobao.taobao
        //检查是否有安装淘宝应用
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaoBaoApp = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaoBaoApp = false;
        }
        //根据这个值去修改UI
        mOpenCopyBtn.setText(mHasTaoBaoApp ? "打开淘宝领券" : "复制淘口令");
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
        mOpenCopyBtn.setOnClickListener(v -> {
            //复制淘口令
            //拿到内容
            String ticketCode = mTicketCode.getText().toString().trim();
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            //复制到粘贴板
            ClipData clipData = ClipData.newPlainText("taobao_ticket_code", ticketCode);
            cm.setPrimaryClip(clipData);
            //判断有没有淘宝
            if (mHasTaoBaoApp) {
                Intent taoBaoIntent = new Intent();
                //taoBaoIntent.setAction("android.intent.action.MAIN");
                //taoBaoIntent.addCategory("android.intent.category.LAUNCHER");
                //com.taobao.taobao/com.taobao.tao.TBMainActivity
                ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                taoBaoIntent.setComponent(componentName);
                startActivity(taoBaoIntent);
            } else {
                //没有就提示复制成功
                ToastUtils.showShort("已经复制，粘贴分享，或打开淘宝");
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        //设置图片封面
        if (mCover != null && !TextUtils.isEmpty(cover)) {
            String coverPath = UrlUtils.getTicketUrl(cover);
            CornerTransform cornerTransform = new CornerTransform(this, ConvertUtils.dp2px(8))
                    .setExceptCorner(false, false, false, false);
            Glide.with(this).load(coverPath).error(R.mipmap.no_image).placeholder(R.mipmap.no_image)
                    .transform(cornerTransform).into(mCover);
        }
        //设置Code
        if (result != null && result.getData().getTbk_tpwd_create_response() != null) {
            String ticketCode = null;
            try {
                ticketCode = result.getData().getTbk_tpwd_create_response().getData().getModel();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(ticketCode)) {
                int startIndex = ticketCode.indexOf("￥");
                int lastIndex = ticketCode.lastIndexOf("￥");
                LogUtils.d(this, "startIndex is ====> " + startIndex);
                LogUtils.d(this, "lastIndex is ====> " + lastIndex);
                try {
                    ticketCode = ticketCode.substring(startIndex, lastIndex + 1);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    LogUtils.e(this, "The ticketCode is error！");
                }
            }
            mTicketCode.setText(ticketCode == null ? "" : ticketCode);
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
package com.example.taobaoalliance.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.IBaseInfo;
import com.example.taobaoalliance.model.domain.OnSellContent;
import com.example.taobaoalliance.presenter.IOnSellPagePresenter;
import com.example.taobaoalliance.ui.adapter.OnSellContentAdapter;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.utils.TicketUtils;
import com.example.taobaoalliance.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment
        implements IOnSellPageCallback, OnSellContentAdapter.OnSellPageItemClickListener {

    private IOnSellPagePresenter mOnSellPagePresenter;
    public static final int DEFAULT_SPAN_COUNT = 2;
    @BindView(R.id.on_sell_content_list)
    RecyclerView mOnSellContentList;
    @BindView(R.id.on_sell_refresh_layout)
    TwinklingRefreshLayout mTwinklingRefreshLayout;
    @BindView(R.id.tv_fragment_bar_title)
    TextView mTvFragmentBarTitle;
    private OnSellContentAdapter mOnSellContentAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void initPresenter() {
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }

    @Override
    protected void release() {
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initListener() {
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多的内容
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loaderMore();
                }
            }
        });
        mOnSellContentAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    protected View loadRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        mTvFragmentBarTitle.setText(getResources().getString(R.string.text_on_sell_title));
        //设置布局管理器
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mOnSellContentList.setLayoutManager(mStaggeredGridLayoutManager);
        mOnSellContentAdapter = new OnSellContentAdapter();
        mOnSellContentList.setAdapter(mOnSellContentAdapter);
        mOnSellContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = ConvertUtils.dp2px(2.5f);
                outRect.bottom = ConvertUtils.dp2px(2.5f);
                outRect.left = ConvertUtils.dp2px(2.5f);
                outRect.right = ConvertUtils.dp2px(2.5f);
            }
        });
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableOverScroll(true);
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        //数据回来了
        setUpState(State.SUCCESS);
        //更新UI
        mOnSellContentAdapter.setData(result);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        mTwinklingRefreshLayout.finishLoadmore();
        //添加内容到适配器里
        mOnSellContentAdapter.onMoreLoaded(moreResult);
        int size = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtils.showShort(String.format("加载了%1s个宝贝", size));
    }

    @Override
    public void onMoreLoadedError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showShort("网络异常，请稍后重试~");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtils.showShort("没有更多内容...");
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onSellItemClick(IBaseInfo item) {
        //特惠列表内容被点击
        //处理数据
        TicketUtils.toTicketPage(item);
    }
}

package com.example.taobaoalliance.ui.fragment;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.OnSellContent;
import com.example.taobaoalliance.presenter.IOnSellPagePresenter;
import com.example.taobaoalliance.ui.adapter.OnSellContentAdapter;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.view.IOnSellPageCallback;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellPageCallback {

    private IOnSellPagePresenter mOnSellPagePresenter;
    public static final int DEFAULT_SPAN_COUNT = 2;
    @BindView(R.id.on_sell_content_list)
    RecyclerView mOnSellContentList;
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
    protected void initView(View rootView) {
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

    }

    @Override
    public void onMoreLoadedError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

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
}

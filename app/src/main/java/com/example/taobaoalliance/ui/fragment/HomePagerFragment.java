package com.example.taobaoalliance.ui.fragment;

import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.Categories;
import com.example.taobaoalliance.model.domain.HomePagerContent;
import com.example.taobaoalliance.presenter.ICategoryPagerPresenter;
import com.example.taobaoalliance.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobaoalliance.ui.adapter.HomePagerContentAdapter;
import com.example.taobaoalliance.ui.adapter.LooperPagerAdapter;
import com.example.taobaoalliance.ui.custom.MyNestedScrollView;
import com.example.taobaoalliance.utils.Constants;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.view.ICategoryCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryCallback {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;
    private HomePagerContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment fragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.home_pager_content_list)
    RecyclerView mContentList;
    @BindView(R.id.looper_pager)
    ViewPager looperPager;
    @BindView(R.id.home_pager_title)
    TextView currentCategoryTitle;
    @BindView(R.id.looper_point_container)
    LinearLayout looperPointContainer;
    //    @BindView(R.id.home_pager_refresh)
//    TwinklingRefreshLayout mTwinklingRefreshLayout;
    @BindView(R.id.home_pager_parent)
    LinearLayout mHomePagerParent;
    @BindView(R.id.home_pager_header_container)
    LinearLayout mHomePagerHeaderContainer;
    @BindView(R.id.home_pager_nested_scroller)
    MyNestedScrollView mHomePagerNestedView;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initListener() {
        mHomePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int headerHeight = mHomePagerHeaderContainer.getMeasuredHeight();
                mHomePagerNestedView.setHeaderHeight(headerHeight);
                int measuredHeight = mHomePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    mHomePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        looperPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int dataSize = mLooperPagerAdapter.getDataSize();
                if (dataSize == 0) return;
                int targetPosition = position % dataSize;
                //切换指示器
                updateLooperIndicator(targetPosition);
            }
        });
//        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                //去加载更多的内容
//                if (mCategoryPagerPresenter != null) {
//                    mCategoryPagerPresenter.loadMore(mMaterialId);
//                }
//            }
//        });
    }

    /**
     * 切换指示器
     *
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = ConvertUtils.dp2px(2);
                outRect.bottom = ConvertUtils.dp2px(2);
            }
        });
        //创建适配器
        mContentAdapter = new HomePagerContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentAdapter);
        //创建轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置适配器
        looperPager.setAdapter(mLooperPagerAdapter);
        //设置Refresh相关内容
//        mTwinklingRefreshLayout.setEnableRefresh(false);
//        mTwinklingRefreshLayout.setEnableLoadmore(true);
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
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        // 加载数据
        LogUtils.d(this, "=======title：" + title);
        LogUtils.d(this, "=======materialId：" + mMaterialId);
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }
        if (currentCategoryTitle != null) {
            currentCategoryTitle.setText(title);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //数据列表加载到了
        // TODO: 2020/6/21 更新UI
        mContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onError() {
        //网络错误
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {
        ToastUtils.showShort("网络异常，请稍后重试~");
//        if (mTwinklingRefreshLayout != null) {
//            mTwinklingRefreshLayout.finishLoadmore();
//        }
    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtils.showShort("没有更多的商品~");
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //添加到适配器数据的底部
        mContentAdapter.addData(contents);
//        if (mTwinklingRefreshLayout != null) {
//            mTwinklingRefreshLayout.finishLoadmore();
//        }
        ToastUtils.showShort("加载了" + contents.size() + "条数据");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        mLooperPagerAdapter.setData(contents);
        //中间点%数据的size不一定为0，所以显示的就不是第一个。
        //处理一下
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPosition = (Integer.MAX_VALUE / 2) - dx;
        //设置到中间点
        looperPager.setCurrentItem(targetCenterPosition);
        //添加点
        for (int i = 0; i < contents.size(); i++) {
            View view = new View(getContext());
            int size = ConvertUtils.dp2px(8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = ConvertUtils.dp2px(5);
            layoutParams.rightMargin = ConvertUtils.dp2px(5);
            view.setLayoutParams(layoutParams);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                view.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(view);
        }
    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }
}

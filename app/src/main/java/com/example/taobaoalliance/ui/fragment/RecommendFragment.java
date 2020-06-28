package com.example.taobaoalliance.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.RecommendContent;
import com.example.taobaoalliance.model.domain.RecommendPageCategory;
import com.example.taobaoalliance.presenter.IRecommendPagePresenter;
import com.example.taobaoalliance.presenter.ITickPresenter;
import com.example.taobaoalliance.ui.activity.TicketActivity;
import com.example.taobaoalliance.ui.adapter.RecommendPageContentAdapter;
import com.example.taobaoalliance.ui.adapter.RecommendPageLeftAdapter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.view.IRecommendPageCallback;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.util.List;

import butterknife.BindView;

public class RecommendFragment extends BaseFragment implements IRecommendPageCallback, RecommendPageLeftAdapter.OnLeftItemClickListener, RecommendPageContentAdapter.OnRecommendPageContentItemClickListener {

    @BindView(R.id.left_category_list)
    RecyclerView mLeftCategoryList;
    @BindView(R.id.right_content_list)
    RecyclerView mRightContentList;

    private IRecommendPagePresenter mRecommendPagePresenter;
    private RecommendPageLeftAdapter mLeftAdapter;
    private RecommendPageContentAdapter mRightAdapter;

    @Override
    protected void initPresenter() {
        mRecommendPagePresenter = PresenterManager.getInstance().getRecommendPagePresenter();
        mRecommendPagePresenter.registerViewCallback(this);
        mRecommendPagePresenter.getCategories();
    }

    @Override
    protected void release() {
        if (mRecommendPagePresenter != null) {
            mRecommendPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mLeftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new RecommendPageLeftAdapter();
        mLeftCategoryList.setAdapter(mLeftAdapter);
        mRightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new RecommendPageContentAdapter();
        mRightContentList.setAdapter(mRightAdapter);
        mRightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = ConvertUtils.dp2px(4);
                int leftAndRight = ConvertUtils.dp2px(6);
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom;
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
            }
        });
    }

    @Override
    protected void initListener() {
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnRecommendPageContentItemClickListener(this);
    }

    @Override
    public void onCategoriesLoaded(RecommendPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setData(categories);
        //分类内容
//        LogUtils.d(this,"onCategoriesLoaded ===> " +categories);
        //更新UI
        //根据当前选中的分类，获取分类详情内容
//        List<RecommendPageCategory.DataBean> data = categories.getData();
//        mRecommendPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(RecommendContent content) {
        mRightAdapter.setData(content);
        mRightContentList.scrollToPosition(0);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onLeftItemClick(RecommendPageCategory.DataBean item) {
        //左边的分类点击了
        mRecommendPagePresenter.getContentByCategory(item);
    }

    @Override
    public void onContentItemClick(RecommendContent.DataBean.TbkUatmFavoritesItemGetResponseBean.ResultsBean.UatmTbkItemBean item) {
        //内容点击了
        //处理数据
        String title = item.getTitle();
        String url = item.getCoupon_click_url();
        if (TextUtils.isEmpty(url)) {
            url = item.getClick_url();
        }
        String cover = item.getPict_url();
        //拿到TicketPresenter去加载数据
        ITickPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}

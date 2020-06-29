package com.example.taobaoalliance.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.Histories;
import com.example.taobaoalliance.model.domain.SearchRecommend;
import com.example.taobaoalliance.model.domain.SearchResult;
import com.example.taobaoalliance.presenter.ISearchPresenter;
import com.example.taobaoalliance.ui.custom.TextFlowLayout;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.view.ISearchPageCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchPageCallback {

    private ISearchPresenter mSearchPresenter;
    @BindView(R.id.search_history_view)
    TextFlowLayout mHistoriesView;
    @BindView(R.id.search_recommend_view)
    TextFlowLayout mRecommendView;
    @BindView(R.id.search_history_container)
    View mHistoryContainer;
    @BindView(R.id.search_recommend_container)
    View mRecommendContainer;
    @BindView(R.id.search_history_delete)
    View mHistoryDelete;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取搜索推荐词
        mSearchPresenter.getRecommendWords();
        //mSearchPresenter.doSearch("毛衣");
        mSearchPresenter.getHistories();
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected View loadRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initListener() {
        mHistoryDelete.setOnClickListener(v -> {
            //删除历史记录
            mSearchPresenter.delHistories();
        });
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);

    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
        if (ObjectUtils.isEmpty(histories) || CollectionUtils.isEmpty(histories.getHistories())) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistoriesView.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHistoriesDeleted() {
        //更新历史记录
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        //LogUtils.d(this,"result =====> " + result);
    }

    @Override
    public void onMoreLoaded(SearchResult result) {

    }

    @Override
    public void onMoreLoaderError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        //LogUtils.d(this, "recommendWords size =====> " + recommendWords.size());
        List<String> recommendKeywords = new ArrayList<>();
        for (SearchRecommend.DataBean item : recommendWords) {
            recommendKeywords.add(item.getKeyword());
        }
        if (CollectionUtils.isEmpty(recommendKeywords)) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendView.setTextList(recommendKeywords);
            mRecommendContainer.setVisibility(View.VISIBLE);
        }
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

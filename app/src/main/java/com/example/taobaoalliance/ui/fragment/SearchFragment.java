package com.example.taobaoalliance.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.Histories;
import com.example.taobaoalliance.model.domain.SearchRecommend;
import com.example.taobaoalliance.model.domain.SearchResult;
import com.example.taobaoalliance.presenter.ISearchPresenter;
import com.example.taobaoalliance.ui.adapter.LinearItemContentAdapter;
import com.example.taobaoalliance.ui.custom.TextFlowLayout;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.utils.TicketUtils;
import com.example.taobaoalliance.view.ISearchPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

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
    @BindView(R.id.search_result_list)
    RecyclerView mSearchList;
    @BindView(R.id.search_btn)
    TextView mSearchBtn;
    @BindView(R.id.search_clear_btn)
    ImageView mClearInputBtn;
    @BindView(R.id.search_input_box)
    EditText mSearchInputBox;
    @BindView(R.id.search_result_container)
    TwinklingRefreshLayout mRefreshContainer;
    private LinearItemContentAdapter mSearchResultAdapter;

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
    protected void onRetryClick() {
        //重新加载内容
        if (mSearchPresenter != null) {
            mSearchPresenter.research();
        }
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
        //发起搜索
        mSearchBtn.setOnClickListener(v -> {
            //如果有内容则搜索
            //如果输入框没有内容则取消
            if (hasInput(false)) {
                //发起搜索
                if (mSearchPresenter != null) {
                    mSearchPresenter.doSearch(mSearchInputBox.getText().toString().trim());
                }
            } else {
                //
            }
        });
        //清除输入框里的内容
        mClearInputBtn.setOnClickListener(v -> mSearchInputBox.setText(""));
        //监听输入框的内容变化
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化的时候通知
                //如果长度不为0，那么显示删除按钮
                //否则隐藏删除按钮
                mClearInputBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
                mSearchBtn.setText(hasInput(false) ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchInputBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                String keyword = v.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) return false;
                //判断拿到的内容是否为空

                //发起搜索
                mSearchPresenter.doSearch(keyword);
            }
            return false;
        });
        mHistoryDelete.setOnClickListener(v -> {
            //删除历史记录
            mSearchPresenter.delHistories();
        });
        mRefreshContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多内容
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });
        mSearchResultAdapter.setOnListItemClickListener(
                //搜素内容被点击了
                TicketUtils::toTicketPage);
    }

    private boolean hasInput(boolean containSpace) {
        if (containSpace) {
            return mSearchInputBox.getText().toString().trim().length() > 0;
        } else {
            return mSearchInputBox.getText().toString().length() > 0;
        }
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器
        mSearchResultAdapter = new LinearItemContentAdapter();
        mSearchList.setAdapter(mSearchResultAdapter);
        //设置刷新控件
        mRefreshContainer.setEnableLoadmore(true);
        mRefreshContainer.setEnableRefresh(false);
        mRefreshContainer.setEnableOverScroll(true);
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
        setUpState(State.SUCCESS);
        //LogUtils.d(this,"result =====> " + result);
        //隐藏掉历史记录和推荐
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        //显示搜索界面
        mRefreshContainer.setVisibility(View.VISIBLE);
        //设置数据
        try {
            mSearchResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        } catch (Exception e) {
            e.printStackTrace();
            //切换到搜索内容为空
            setUpState(State.EMPTY);
        }
        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = ConvertUtils.dp2px(1.5f);
                outRect.bottom = ConvertUtils.dp2px(1.5f);
            }
        });
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mRefreshContainer.finishLoadmore();
        //加载更多的结果
        //拿到结果，添加到适配器的尾部
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> moreData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(moreData);
        //提示用户加载到的内容
        ToastUtils.showShort(String.format("加载到了%1s个宝贝", moreData.size()));
    }

    @Override
    public void onMoreLoaderError() {
        mRefreshContainer.finishLoadmore();
        ToastUtils.showShort("网络异常，请稍后重试~");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mRefreshContainer.finishLoadmore();
        ToastUtils.showShort("没有更多数据了~");
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
            mRecommendContainer.setVisibility(View.VISIBLE);
            mRecommendView.setTextList(recommendKeywords);
        }
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

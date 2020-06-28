package com.example.taobaoalliance.ui.fragment;

import android.view.View;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.model.domain.RecommendContent;
import com.example.taobaoalliance.model.domain.RecommendPageCategory;
import com.example.taobaoalliance.presenter.IRecommendPagePresenter;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.PresenterManager;
import com.example.taobaoalliance.view.IRecommendPageCallback;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.util.List;

public class RecommendFragment extends BaseFragment implements IRecommendPageCallback {

    private IRecommendPagePresenter mRecommendPagePresenter;

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

    }

    @Override
    public void onCategoriesLoaded(RecommendPageCategory categories) {
        //分类内容
        LogUtils.d(this,"onCategoriesLoaded ===> " +categories);
        //更新UI
        //根据当前选中的分类，获取分类详情内容
        List<RecommendPageCategory.DataBean> data = categories.getData();
        mRecommendPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(RecommendContent content) {
        LogUtils.d(this,"onContentLoaded =====> "  + content.getData().getTbk_uatm_favorites_item_get_response().getResults().getUatm_tbk_item().get(0).getTitle());
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

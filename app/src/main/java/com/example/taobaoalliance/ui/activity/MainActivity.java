package com.example.taobaoalliance.ui.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseActivity;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.ui.fragment.HomeFragment;
import com.example.taobaoalliance.ui.fragment.RecommendFragment;
import com.example.taobaoalliance.ui.fragment.RedPacketFragment;
import com.example.taobaoalliance.ui.fragment.SearchFragment;
import com.example.taobaoalliance.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_navigation_bar)
    BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RedPacketFragment mRedPacketFragment;
    private RecommendFragment mRecommendFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;

    @Override
    protected void initEvent() {
        initListener();
    }

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRedPacketFragment = new RedPacketFragment();
        mRecommendFragment = new RecommendFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    LogUtils.d(this, "initListener: ============切换到了首页");
                    switchFragment(mHomeFragment);
                    break;
                case R.id.recommend:
                    LogUtils.d(this, "initListener: ============切换到了精选");
                    switchFragment(mRecommendFragment);
                    break;
                case R.id.red_packet:
                    LogUtils.d(this, "initListener: ============切换到了特惠");
                    switchFragment(mRedPacketFragment);
                    break;
                case R.id.search:
                    LogUtils.d(this, "initListener: ============切换到了搜索");
                    switchFragment(mSearchFragment);
                    break;
            }
            return true;
        });
    }

    //上一次显示的Fragment
    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        //修改成add和hide的方式来控制Fragment的切换
        FragmentTransaction fragmentTransaction = mFm.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            fragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
//        fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }
}

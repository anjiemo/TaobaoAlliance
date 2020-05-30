package com.example.taobaoalliance.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment;
import com.example.taobaoalliance.ui.fragment.HomeFragment;
import com.example.taobaoalliance.ui.fragment.RecommendFragment;
import com.example.taobaoalliance.ui.fragment.RedPacketFragment;
import com.example.taobaoalliance.ui.fragment.SearchFragment;
import com.example.taobaoalliance.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_navigation_bar)
    BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RedPacketFragment mRedPacketFragment;
    private RecommendFragment mRecommendFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
        initFragment();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
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

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction fragmentTransaction = mFm.beginTransaction();
        fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }
}

// Generated code from Butter Knife. Do not modify!
package com.example.taobaoalliance.ui.fragment;

import android.view.View;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.internal.Utils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment_ViewBinding;
import com.google.android.material.tabs.TabLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeFragment_ViewBinding extends BaseFragment_ViewBinding {
  private HomeFragment target;

  @UiThread
  public HomeFragment_ViewBinding(HomeFragment target, View source) {
    super(target, source);

    this.target = target;

    target.mTabLayout = Utils.findRequiredViewAsType(source, R.id.home_indicator, "field 'mTabLayout'", TabLayout.class);
    target.homePager = Utils.findRequiredViewAsType(source, R.id.home_pager, "field 'homePager'", ViewPager.class);
  }

  @Override
  public void unbind() {
    HomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTabLayout = null;
    target.homePager = null;

    super.unbind();
  }
}

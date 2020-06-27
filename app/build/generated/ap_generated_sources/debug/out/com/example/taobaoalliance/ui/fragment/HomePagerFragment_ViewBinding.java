// Generated code from Butter Knife. Do not modify!
package com.example.taobaoalliance.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.internal.Utils;
import com.example.taobaoalliance.R;
import com.example.taobaoalliance.base.BaseFragment_ViewBinding;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.MyNestedScrollView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomePagerFragment_ViewBinding extends BaseFragment_ViewBinding {
  private HomePagerFragment target;

  @UiThread
  public HomePagerFragment_ViewBinding(HomePagerFragment target, View source) {
    super(target, source);

    this.target = target;

    target.mContentList = Utils.findRequiredViewAsType(source, R.id.home_pager_content_list, "field 'mContentList'", RecyclerView.class);
    target.looperPager = Utils.findRequiredViewAsType(source, R.id.looper_pager, "field 'looperPager'", ViewPager.class);
    target.currentCategoryTitle = Utils.findRequiredViewAsType(source, R.id.home_pager_title, "field 'currentCategoryTitle'", TextView.class);
    target.looperPointContainer = Utils.findRequiredViewAsType(source, R.id.looper_point_container, "field 'looperPointContainer'", LinearLayout.class);
    target.mTwinklingRefreshLayout = Utils.findRequiredViewAsType(source, R.id.home_pager_refresh, "field 'mTwinklingRefreshLayout'", TwinklingRefreshLayout.class);
    target.mHomePagerParent = Utils.findRequiredViewAsType(source, R.id.home_pager_parent, "field 'mHomePagerParent'", LinearLayout.class);
    target.mHomePagerHeaderContainer = Utils.findRequiredViewAsType(source, R.id.home_pager_header_container, "field 'mHomePagerHeaderContainer'", LinearLayout.class);
    target.mHomePagerNestedView = Utils.findRequiredViewAsType(source, R.id.home_pager_nested_scroller, "field 'mHomePagerNestedView'", MyNestedScrollView.class);
  }

  @Override
  public void unbind() {
    HomePagerFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mContentList = null;
    target.looperPager = null;
    target.currentCategoryTitle = null;
    target.looperPointContainer = null;
    target.mTwinklingRefreshLayout = null;
    target.mHomePagerParent = null;
    target.mHomePagerHeaderContainer = null;
    target.mHomePagerNestedView = null;

    super.unbind();
  }
}

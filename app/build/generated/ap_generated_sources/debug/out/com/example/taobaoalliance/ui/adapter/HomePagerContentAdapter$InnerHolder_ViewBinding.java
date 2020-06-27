// Generated code from Butter Knife. Do not modify!
package com.example.taobaoalliance.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.taobaoalliance.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomePagerContentAdapter$InnerHolder_ViewBinding implements Unbinder {
  private HomePagerContentAdapter.InnerHolder target;

  @UiThread
  public HomePagerContentAdapter$InnerHolder_ViewBinding(HomePagerContentAdapter.InnerHolder target,
      View source) {
    this.target = target;

    target.ivCover = Utils.findRequiredViewAsType(source, R.id.goods_cover, "field 'ivCover'", ImageView.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.goods_title, "field 'tvTitle'", TextView.class);
    target.tvOffPrise = Utils.findRequiredViewAsType(source, R.id.goods_off_prise, "field 'tvOffPrise'", TextView.class);
    target.tvFinalPrise = Utils.findRequiredViewAsType(source, R.id.goods_after_off_prise, "field 'tvFinalPrise'", TextView.class);
    target.tvOriginalPrise = Utils.findRequiredViewAsType(source, R.id.goods_original_prise, "field 'tvOriginalPrise'", TextView.class);
    target.tvSellCount = Utils.findRequiredViewAsType(source, R.id.goods_sell_count, "field 'tvSellCount'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomePagerContentAdapter.InnerHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivCover = null;
    target.tvTitle = null;
    target.tvOffPrise = null;
    target.tvFinalPrise = null;
    target.tvOriginalPrise = null;
    target.tvSellCount = null;
  }
}

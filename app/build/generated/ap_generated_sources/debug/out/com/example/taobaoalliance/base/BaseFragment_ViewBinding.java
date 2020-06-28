// Generated code from Butter Knife. Do not modify!
package com.example.taobaoalliance.base;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.example.taobaoalliance.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BaseFragment_ViewBinding implements Unbinder {
  private BaseFragment target;

  private View view7f09013e;

  @UiThread
  public BaseFragment_ViewBinding(final BaseFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.network_error_tips, "method 'retry'");
    view7f09013e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.retry();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f09013e.setOnClickListener(null);
    view7f09013e = null;
  }
}

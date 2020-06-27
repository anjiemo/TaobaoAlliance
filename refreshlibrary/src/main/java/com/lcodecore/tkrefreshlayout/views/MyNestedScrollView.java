package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class MyNestedScrollView extends NestedScrollView {
    private int mHeaderHeight = 0;
    private int originScroll = 0;
    private RecyclerView mRecyclerView;

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderHeight(int headerHeight) {
        mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed,
                                  @ViewCompat.NestedScrollType int type) {
        if (target instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) target;
        }
        if (originScroll < mHeaderHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 判断子类是否已经滑动到了底部
     *
     * @return
     */
    public boolean isInBottom() {
        if (mRecyclerView != null) {
            return !mRecyclerView.canScrollVertically(1);
        }
        return false;
    }
}

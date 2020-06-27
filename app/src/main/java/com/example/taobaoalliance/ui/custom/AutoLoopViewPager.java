package com.example.taobaoalliance.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 功能：自动轮播
 */
public class AutoLoopViewPager extends ViewPager {
    public AutoLoopViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isLoop = false;

    /**
     * 开始轮播
     */
    public void startLoop() {
        isLoop = true;
        //先拿到的当前的位置
        post(mTask);
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (isLoop) {
                int currentItem = getCurrentItem();
                currentItem++;
                setCurrentItem(currentItem);
                postDelayed(this, 3000);
            }
        }
    };

    /**
     * 停止轮播
     */
    public void stopLoop() {
        isLoop = false;
        removeCallbacks(mTask);
    }
}

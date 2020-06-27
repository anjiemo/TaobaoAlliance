package com.example.taobaoalliance.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaoalliance.R;

/**
 * 功能：自动轮播
 */
@SuppressLint("CustomViewStyleable")
public class AutoLoopViewPager extends ViewPager {

    //切换间隔时长，单位毫秒
    public static final long DEFAULT_DURATION = 3000;

    private long mDuration = DEFAULT_DURATION;

    public AutoLoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLoopStyle);
        //获取属性
        mDuration = typedArray.getInteger(R.styleable.AutoLoopStyle_duration, (int) DEFAULT_DURATION);
        //回收
        typedArray.recycle();
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

    /**
     * 设置切换时长
     *
     * @param duration 时长，单位：毫秒
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (isLoop) {
                int currentItem = getCurrentItem();
                currentItem++;
                setCurrentItem(currentItem);
                postDelayed(this, mDuration);
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

package com.example.taobaoalliance.base;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //setQingMingFestivalAsh();
        mBind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }

    /**
     * 设置主题为清明节灰色
     */
    private void setQingMingFestivalAsh() {
        //==================清明节灰 start========================
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        View contentContainer = getWindow().getDecorView();
        contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        //==================清明节灰 end========================
    }

    protected abstract void initPresenter();

    /**
     * 需要的时候覆写
     */
    protected void initEvent() {

    }

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        release();
    }

    /**
     * 子类需要释放资源，覆盖即可
     */
    protected void release() {

    }

    protected abstract int getLayoutResId();
}

package com.example.taobaoalliance.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.taobaoalliance.model.domain.HomePagerContent;
import com.example.taobaoalliance.utils.LogUtils;
import com.example.taobaoalliance.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> mData = new ArrayList<>();

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        ImageView ivLooper = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ivLooper.setLayoutParams(layoutParams);
        ivLooper.setScaleType(ImageView.ScaleType.CENTER_CROP);
        HomePagerContent.DataBean dataBean = mData.get(position);
        String coverPath = UrlUtils.getCoverPath(dataBean.getPict_url());
        Glide.with(context).load(coverPath).into(ivLooper);
        container.addView(ivLooper);
        return ivLooper;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }
}

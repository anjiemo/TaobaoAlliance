package com.example.taobaoalliance.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.taobaoalliance.model.domain.Categories;
import com.example.taobaoalliance.ui.fragment.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataBean> categoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        return homePagerFragment;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();
    }

    public void setCategories(Categories categories) {
        categoryList.clear();
        List<Categories.DataBean> data = categories.getData();
        categoryList.addAll(data);
        notifyDataSetChanged();
    }
}

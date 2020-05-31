package com.example.taobaoalliance.view;

import com.example.taobaoalliance.model.domain.Categories;

public interface IHomeCallback {

    void onCategoriesLoaded(Categories categories);

    void onNetworkError();

    void onLoading();

    void onEmpty();

}

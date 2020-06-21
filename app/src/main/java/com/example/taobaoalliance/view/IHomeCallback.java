package com.example.taobaoalliance.view;

import com.example.taobaoalliance.base.IBaseCallback;
import com.example.taobaoalliance.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {

    void onCategoriesLoaded(Categories categories);

}

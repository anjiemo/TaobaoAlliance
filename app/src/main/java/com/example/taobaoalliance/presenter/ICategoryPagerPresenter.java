package com.example.taobaoalliance.presenter;

public interface ICategoryPagerPresenter  {

    /**
     * 根据分类Id去获取分类内容
     *
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void loadMore(int categoryId);

    void reload(int categoryId);
}

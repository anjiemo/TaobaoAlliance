package com.example.taobaoalliance.model;

import com.example.taobaoalliance.model.domain.Categories;
import com.example.taobaoalliance.model.domain.HomePagerContent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET("discovery/{materialId}/{page}")
    Call<HomePagerContent> getHomePagerContent(@Path("materialId") int materialId, @Path("page") int page);
}

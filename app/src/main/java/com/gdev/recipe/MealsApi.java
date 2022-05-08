package com.gdev.recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MealsApi {
    @GET("categories.php")
    Call<CategoriesModal> getCategories();

    @GET
    Call<MealsModal> getFilteredMeals(@Url String url);
}

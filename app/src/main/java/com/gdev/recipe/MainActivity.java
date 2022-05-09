package com.gdev.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    RecyclerView rv_categories, rv_meals;

    CategoriesAdapter categoriesAdapter;
    ArrayList<CategoriesList> categoriesList;

    MealsAdapter mealsAdapter;
    ArrayList<MealsList> mealsList;

    ProgressBar progressBar;

    int id = 52874;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealsList = new ArrayList<>();
        categoriesList = new ArrayList<>();

        progressBar = findViewById(R.id.progress_circular);

        // recycler views
        categoriesRecycler();
        mealsRecycler();

        retrofitClient("Lamb");
        mealsAdapter.notifyDataSetChanged();
    }

    public void retrofitClient(String category) {
        String categoryUrl = "filter.php?c=" + category;
        String idUrl = "lookup.php?i=" + Integer.toString(id);
        String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MealsApi mealsApi = retrofit.create(MealsApi.class);

        // adding categories
        Call<CategoriesModal> getCategories = mealsApi.getCategories();
        getCategories.enqueue(new Callback<CategoriesModal>() {
            @Override
            public void onResponse(Call<CategoriesModal> call, Response<CategoriesModal> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                CategoriesModal categoriesModal = response.body();
                ArrayList<CategoriesList> categories = categoriesModal.getCategories();

                for (int i = 0; i < categories.size(); i++)
                {
                    categoriesList.add(new CategoriesList(categories.get(i).getStrCategory(), categories.get(i).getStrCategoryThumb(), categories.get(i).getStrCategoryDescription()));
                }
                categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CategoriesModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // adding the meals
        Call<MealsModal> getFilteredMeals = mealsApi.getFilteredMeals(categoryUrl);
        getFilteredMeals.enqueue(new Callback<MealsModal>() {
            @Override
            public void onResponse(Call<MealsModal> call, Response<MealsModal> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                MealsModal mealsModal = response.body();
                ArrayList<MealsList> meals = mealsModal.getMeals();
                mealsList.clear();

                for (int i = 0; i < meals.size(); i++)
                {
                    mealsList.add(new MealsList(meals.get(i).getStrMeal(), meals.get(i).getStrMealThumb(), meals.get(i).getIdMeal()));
                }
                mealsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MealsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // viewing the meal
        /*Call<MealsModal> getMealById = mealsApi.getMealById(idUrl);
        getMealById.enqueue(new Callback<MealsModal>() {
            @Override
            public void onResponse(Call<MealsModal> call, Response<MealsModal> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                MealsModal mealsModal = response.body();
                ArrayList<MealsList> mealsListArrayList = mealsModal.getMeals();
                Intent viewMeal = new Intent(MainActivity.this, ViewMeal.class);
                viewMeal.putExtra("mealname", mealsListArrayList.get(0).getStrMeal());
                viewMeal.putExtra("instructions", mealsListArrayList.get(0).getStrInstructions());
                startActivity(viewMeal);
            }

            @Override
            public void onFailure(Call<MealsModal> call, Throwable t) {

            }
        });*/
        progressBar.setVisibility(View.GONE);

    }

    private void categoriesRecycler() {
        rv_categories = findViewById(R.id.rv_categories);
        categoriesAdapter = new CategoriesAdapter(categoriesList, MainActivity.this);
        rv_categories.setAdapter(categoriesAdapter);
    }
    private void mealsRecycler() {
        rv_meals = findViewById(R.id.rv_meals);
        mealsAdapter = new MealsAdapter(mealsList);
        rv_meals.setAdapter(mealsAdapter);
    }
    public void getMealId(int id)
    {
        this.id = id;
    }
}
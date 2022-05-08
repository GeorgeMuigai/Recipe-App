package com.gdev.recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv_categories, rv_meals;

    CategoriesAdapter categoriesAdapter;
    List<CategoriesList> categoriesList = new ArrayList<>();

    MealsAdapter mealsAdapter;
    List<MealsList> mealsList = new ArrayList<>();

    public String category;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        category = "";
        // recycler views
        categoriesRecycler();
        mealsRecycler();

        retrofitClient();
    }

    private void retrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
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
        Call<MealsModal> getFilteredMeals = mealsApi.getFilteredMeals();
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

    }

    private void categoriesRecycler() {
        rv_categories = findViewById(R.id.rv_categories);
        categoriesAdapter = new CategoriesAdapter(categoriesList);
        rv_categories.setAdapter(categoriesAdapter);
    }
    private void mealsRecycler() {
        rv_meals = findViewById(R.id.rv_meals);
        mealsAdapter = new MealsAdapter(mealsList);
        rv_meals.setAdapter(mealsAdapter);
    }
}
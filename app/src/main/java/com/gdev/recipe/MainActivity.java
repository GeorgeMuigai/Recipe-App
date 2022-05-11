package com.gdev.recipe;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gdev.recipe.nointernet.NoInternet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv_categories, rv_meals;

    CategoriesAdapter categoriesAdapter;
    ArrayList<CategoriesList> categoriesList;

    MealsAdapter mealsAdapter;
    ArrayList<MealsList> mealsList;

    ProgressBar progressBar;
    SearchView searchView;
    TextView active, category_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealsList = new ArrayList<>();
        categoriesList = new ArrayList<>();

        progressBar = findViewById(R.id.progress_circular);
        searchView = findViewById(R.id.search);
        active = findViewById(R.id.meals);
        category_txt = findViewById(R.id.txt_category_title);

        // recycler views
        categoriesRecycler();
        mealsRecycler();

        // checking for internet connection
        checkInternetConnection();

        retrofitClient("Beef");
        mealsAdapter.notifyDataSetChanged();
    }

    public void retrofitClient(String category) {
        String categoryUrl = "filter.php?c=" + category;
        active.setText("\"" + category + "\"" + " Meals");
        String searchUrl = "search.php?s=";
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
                unHideText();
                hideProgressBar();
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                CategoriesModal categoriesModal = response.body();
                ArrayList<CategoriesList> categories = categoriesModal.getCategories();

                for (int i = 0; i < categories.size(); i++) {
                    categoriesList.add(new CategoriesList(categories.get(i).getStrCategory(), categories.get(i).getStrCategoryThumb(), categories.get(i).getStrCategoryDescription()));
                }
                categoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<CategoriesModal> call, Throwable t) {
                hideProgressBar();
                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // adding the meals
        Call<MealsModal> getFilteredMeals = mealsApi.getFilteredMeals(categoryUrl);
        getFilteredMeals.enqueue(new Callback<MealsModal>() {
            @Override
            public void onResponse(Call<MealsModal> call, Response<MealsModal> response) {
                hideProgressBar();
                unHideText();
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                MealsModal mealsModal = response.body();
                ArrayList<MealsList> meals = mealsModal.getMeals();
                mealsList.clear();

                for (int i = 0; i < meals.size(); i++) {
                    mealsList.add(new MealsList(meals.get(i).getStrMeal(), meals.get(i).getStrMealThumb(), meals.get(i).getIdMeal()));
                }
                mealsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MealsModal> call, Throwable t) {
                hideProgressBar();
                //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // searching for meals
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    Call<MealsModal> getSearchedMeal = mealsApi.getSearchedMeal(searchUrl + s);
                    getSearchedMeal.enqueue(new Callback<MealsModal>() {
                        @Override
                        public void onResponse(Call<MealsModal> call, Response<MealsModal> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            MealsModal mealsModal = response.body();
                            assert mealsModal != null;
                            ArrayList<MealsList> searched = mealsModal.getMeals();
                            mealsList.clear();
                            if (response.body() == null) {
                                active.setText("No results for " + s);
                                return;
                            }

                            for (int i = 0; i < searched.size(); i++) {
                                mealsList.add(new MealsList(searched.get(i).getStrMeal(), searched.get(i).getStrMealThumb(), searched.get(i).getIdMeal()));
                            }
                            mealsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<MealsModal> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return true;
            }

        });

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

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void unHideText() {
        active.setVisibility(View.VISIBLE);
        category_txt.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
    }

    private void checkInternetConnection()    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo == null || !networkInfo.isConnected())
        {
            Intent intent = new Intent(MainActivity.this, NoInternet.class);
            startActivity(intent);
            finish();
        }
    }
}
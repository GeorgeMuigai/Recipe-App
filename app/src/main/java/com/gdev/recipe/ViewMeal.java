package com.gdev.recipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewMeal extends AppCompatActivity {

    ImageView img_meal_view;
    TextView txt_meal_name_view, txt_ingredients, txt_measures, txt_instructions, txt_link_yt;
    Button btn_read_more;

    List<MealsList> list;

    RelativeLayout parent_view_meal;
    ProgressBar progress_view_meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meal);

        list = new ArrayList<>();

        img_meal_view = findViewById(R.id.img_meal_view);
        txt_meal_name_view = findViewById(R.id.txt_meal_name_view);
        txt_ingredients = findViewById(R.id.txt_ingredients);
        txt_measures = findViewById(R.id.txt_measures);
        txt_instructions = findViewById(R.id.txt_instructions);
        txt_link_yt = findViewById(R.id.txt_link_yt);
        btn_read_more = findViewById(R.id.btn_read_more);

        parent_view_meal = findViewById(R.id.view_meal_parent);
        progress_view_meal = findViewById(R.id.progress_view_meal);

        txt_measures.setText("");
        txt_ingredients.setText("");

        // getting the meal id to display
        String id = getIntent().getStringExtra("id");

        // calling retrofit
        retrofitClient(id);
    }

    private void retrofitClient(String id) {
        String idUrl = "lookup.php?i=" + id;
        String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MealsApi mealsApi = retrofit.create(MealsApi.class);
        Call<MealsModal> getMealById = mealsApi.getMealById(idUrl);

        getMealById.enqueue(new Callback<MealsModal>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MealsModal> call, Response<MealsModal> response) {
                showHide();
                if (!response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ViewMeal.this, "Error code : " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                MealsModal mealsModal = response.body();
                ArrayList<MealsList> meals = mealsModal.getMeals();

                list.clear();

                for (int i = 0; i < meals.size(); i++) {
                    list.add(new MealsList(meals.get(i).getStrMeal(), meals.get(i).getStrMealThumb(), meals.get(i).getIdMeal()));
                }

                try {
                    Picasso.get().load(meals.get(0).getStrMealThumb()).into(img_meal_view);
                } catch (Exception e) {
                }
                txt_meal_name_view.setText(meals.get(0).getStrMeal());
                txt_instructions.setText(meals.get(0).getStrInstructions());
                if(!meals.get(0).getStrYoutube().isEmpty())
                {
                    txt_link_yt.setText(meals.get(0).getStrYoutube());
                }else {
                    txt_link_yt.setText("No youtube link");
                }
                String[] ingredients = {meals.get(0).getStrIngredient1(), meals.get(0).getStrIngredient2(),
                                                    meals.get(0).getStrIngredient3(), meals.get(0).getStrIngredient4(),
                                                    meals.get(0).getStrIngredient5(), meals.get(0).getStrIngredient6(),
                                                    meals.get(0).getStrIngredient7(), meals.get(0).getStrIngredient8(), meals.get(0).getStrIngredient9(),
                                                    meals.get(0).getStrIngredient10(), meals.get(0).getStrIngredient11(), meals.get(0).getStrIngredient12(),
                                                    meals.get(0).getStrIngredient13(), meals.get(0).getStrIngredient14(), meals.get(0).getStrIngredient15(),
                                                    meals.get(0).getStrIngredient16(), meals.get(0).getStrIngredient17(), meals.get(0).getStrIngredient18(),
                                                    meals.get(0).getStrIngredient19(), meals.get(0).getStrIngredient20()};
                String[] measures = {meals.get(0).getStrMeasure1(), meals.get(0).getStrMeasure2(),
                        meals.get(0).getStrMeasure3(), meals.get(0).getStrMeasure4(),
                        meals.get(0).getStrMeasure5(), meals.get(0).getStrMeasure6(),
                        meals.get(0).getStrMeasure7(), meals.get(0).getStrMeasure8(), meals.get(0).getStrMeasure9(),
                        meals.get(0).getStrMeasure10(), meals.get(0).getStrMeasure11(), meals.get(0).getStrMeasure12(),
                        meals.get(0).getStrMeasure13(), meals.get(0).getStrMeasure14(), meals.get(0).getStrMeasure15(),
                        meals.get(0).getStrMeasure16(), meals.get(0).getStrMeasure17(), meals.get(0).getStrMeasure18(),
                        meals.get(0).getStrMeasure19(), meals.get(0).getStrMeasure20()};
                try {
                    for (int i = 0; i < ingredients.length; i++) {
                        if(ingredients[i] != null && measures[i] != null)
                        {
                            txt_ingredients.append(ingredients[i] + "\n");
                            txt_measures.append(measures[i] + "\n");
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ViewMeal.this, "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                }

                // opening the website
                btn_read_more.setOnClickListener(View ->{
                    if(!meals.get(0).getStrSource().isEmpty())
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meals.get(0).getStrSource()));
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "No link found", Toast.LENGTH_SHORT).show();
                    }
                });

                // opening You Tube
                txt_link_yt.setOnClickListener(View ->{
                    if(!meals.get(0).getStrYoutube().isEmpty())
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(meals.get(0).getStrYoutube()));
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "No link found", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<MealsModal> call, Throwable t) {
                showHide();
                Toast.makeText(ViewMeal.this, "Something went wrong \nMake sure you have an active internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showHide()    {
        parent_view_meal.setVisibility(View.VISIBLE);
        progress_view_meal.setVisibility(View.GONE);
    }
}
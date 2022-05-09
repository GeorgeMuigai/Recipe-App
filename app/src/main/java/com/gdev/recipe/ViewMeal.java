package com.gdev.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        String id = getIntent().getStringExtra("id");
        //Log.d("checking", "onCreate: " + id);
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
                txt_link_yt.setText(meals.get(0).getStrYoutube());
                try {
                    txt_ingredients.setText(meals.get(0).getStrIngredient1() + "\n" + meals.get(0).getStrIngredient2() + "\n" +
                            meals.get(0).getStrIngredient3() + "\n" + meals.get(0).getStrIngredient4() + "\n" +
                            meals.get(0).getStrIngredient5() + "\n" + meals.get(0).getStrIngredient6() + "\n" +
                            meals.get(0).getStrIngredient7() + "\n" + meals.get(0).getStrIngredient8() + "\n" + meals.get(0).getStrIngredient9() + "\n" +
                            meals.get(0).getStrIngredient10() + "\n" + meals.get(0).getStrIngredient11() + "\n" + meals.get(0).getStrIngredient12() + "\n" +
                            meals.get(0).getStrIngredient13() + "\n" + meals.get(0).getStrIngredient14() + "\n" + meals.get(0).getStrIngredient15() + "\n" +
                            meals.get(0).getStrIngredient16() + "\n" + meals.get(0).getStrIngredient17() + "\n" + meals.get(0).getStrIngredient18() + "\n" +
                            meals.get(0).getStrIngredient19() + "\n" + meals.get(0).getStrIngredient20() + "\n");
                    txt_measures.setText(meals.get(0).getStrMeasure1() + "\n" + meals.get(0).getStrMeasure1() + "\n" + meals.get(0).getStrMeasure1() + "\n" +
                            meals.get(0).getStrMeasure1() + "\n" + meals.get(0).getStrMeasure2() + "\n" + meals.get(0).getStrMeasure3() + "\n" +
                            meals.get(0).getStrMeasure4() + "\n" + meals.get(0).getStrMeasure5() + "\n" + meals.get(0).getStrMeasure6() + "\n" + meals.get(0).getStrMeasure7() + "\n" +
                            meals.get(0).getStrMeasure8() + "\n" + meals.get(0).getStrMeasure9() + "\n" + meals.get(0).getStrMeasure10() + "\n" + meals.get(0).getStrMeasure11() + "\n" +
                            meals.get(0).getStrMeasure12() + "\n" + meals.get(0).getStrMeasure13() + "\n" + meals.get(0).getStrMeasure14() + "\n" + meals.get(0).getStrMeasure15() + "\n" +
                            meals.get(0).getStrMeasure16() + "\n" + meals.get(0).getStrMeasure17() + "\n" + meals.get(0).getStrMeasure18() + "\n" + meals.get(0).getStrMeasure19() + "\n" +
                            meals.get(0).getStrMeasure20() + "\n");
                } catch (Exception e) {
                    Toast.makeText(ViewMeal.this, "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MealsModal> call, Throwable t) {
                Toast.makeText(ViewMeal.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
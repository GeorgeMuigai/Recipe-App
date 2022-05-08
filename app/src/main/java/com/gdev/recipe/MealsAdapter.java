package com.gdev.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    List<MealsList> mealsList;

    public MealsAdapter(List<MealsList> mealsList) {
        this.mealsList = mealsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_meals, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealsList List = mealsList.get(position);
        try {
            Picasso.get().load(List.getStrMealThumb()).into(holder.img_meal);
        }catch (Exception e){}
        holder.txt_meal_name.setText(List.getStrMeal());
    }

    @Override
    public int getItemCount() {
        return mealsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_meal_name;
        ImageView img_meal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_meal_name = itemView.findViewById(R.id.txt_meal_name);
            img_meal = itemView.findViewById(R.id.img_meal);
        }
    }
}

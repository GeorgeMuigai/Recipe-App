package com.gdev.recipe;

import java.util.ArrayList;

public class MealsModal {
    ArrayList<MealsList> meals;

    public MealsModal(ArrayList<MealsList> meals) {
        this.meals = meals;
    }

    public ArrayList<MealsList> getMeals() {
        return meals;
    }
}

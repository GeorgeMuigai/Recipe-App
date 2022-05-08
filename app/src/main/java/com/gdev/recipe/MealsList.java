package com.gdev.recipe;

public class MealsList {
    String strMeal;
    String strMealThumb;
    int idMeal;

    public MealsList(String strMeal, String strMealThumb, int idMeal) {
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public int getIdMeal() {
        return idMeal;
    }
}

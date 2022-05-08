package com.gdev.recipe;

import java.util.ArrayList;

public class CategoriesModal {
    ArrayList<CategoriesList> categories;

    public CategoriesModal(ArrayList<CategoriesList> categories) {
        this.categories = categories;
    }

    public ArrayList<CategoriesList> getCategories() {
        return categories;
    }
}

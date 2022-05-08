package com.gdev.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    List<CategoriesList> categoriesLists;
    MainActivity mainActivity;

    public CategoriesAdapter(List<CategoriesList> categoriesLists, MainActivity mainActivity) {
        this.categoriesLists = categoriesLists;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesList list = categoriesLists.get(position);
        try {
            Picasso.get().load(list.getStrCategoryThumb()).into(holder.img_category);
        }catch (Exception e){}
        holder.txt_category.setText(list.getStrCategory());


        holder.itemView.setOnClickListener(View ->{
            //categoryItemClick.onCategoryClick(position);
            mainActivity.retrofitClient(list.getStrCategory());
        });
    }

    @Override
    public int getItemCount() {
        return categoriesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_category;
        TextView txt_category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_category = itemView.findViewById(R.id.img_category);
            txt_category = itemView.findViewById(R.id.txt_category);
        }
    }
}

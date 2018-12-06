package com.example.minjia.finalproject;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodNutrition_Fragement extends Fragment {
    private ListView favoriteListView;
    private List<Map<String, String>> favoriteList;
    private Context mainActivity;
    private MyAdapter favoriteAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteList = FoodNutrition_dbHelper.getHelper(mainActivity).getAllDate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View favoriteFragment = inflater.inflate(R.layout.activity_food_nutrition__fragement, null);
        favoriteListView = favoriteFragment.findViewById(R.id.food_Favorite_list);
        favoriteAdapter = new MyAdapter(mainActivity,R.layout.activity_food_nutrition__list_items, favoriteList);
        favoriteListView.setAdapter(favoriteAdapter);

        return favoriteFragment;
    }




    class MyAdapter extends ArrayAdapter<Map<String, String>>{
        int id;

        public MyAdapter(Context context, int resource,  List<Map<String, String>> food) {

            super(context, resource, food);

            id = resource;
        }


        @NonNull
        @Override
        public View getView(int position,  View convertView,  ViewGroup parent) {
            Map<String,String> food = getItem(position);
            View item = LayoutInflater.from(getContext()).inflate(id,null);
            TextView foodName = item.findViewById(R.id.food_item_name);
            foodName.setText(food.get("name"));

            return item;
        }
    }


}

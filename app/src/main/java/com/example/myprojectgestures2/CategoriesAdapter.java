package com.example.myprojectgestures2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
    public void refresh (List<Category> categories){
        data.clear();
        data.addAll(categories);
        notifyDataSetChanged();
    }
    private List<Category> data=new ArrayList<>();
    private Context context;
    public CategoriesAdapter(Context context){
        this.context=context;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return data.get(position).id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = data.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.category_list_item, null);
        }
        TextView aCategory=((TextView) convertView.findViewById(R.id.category_list_item_name_tv));
        aCategory.setText(category.name);
        aCategory.setBackgroundColor(Color.parseColor(category.color));
        return convertView;

    }
}




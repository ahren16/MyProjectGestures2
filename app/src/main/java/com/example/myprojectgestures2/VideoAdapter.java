package com.example.myprojectgestures2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends BaseAdapter {
    public void refresh (List<Video> video){
        videos.clear();
        videos.addAll(video);
        notifyDataSetChanged();
    }
    private List<Video> videos = new ArrayList<>();
    private Context context;
    public VideoAdapter (Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return videos.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Video video = videos.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.video_list_item, null);
        }
        TextView aCategory=((TextView) convertView.findViewById(R.id.video_list_item_name_tv));
        aCategory.setText(video.name);
        /*TextView bCategory=((TextView) convertView.findViewById(R.id.video_list_item_name_tv));
        bCategory.setText(video.code);
        TextView cCategory=((TextView) convertView.findViewById(R.id.video_list_item_name_tv));
        cCategory.setText(video.comments);
        TextView gCategory=((TextView) convertView.findViewById(R.id.video_list_item_name_tv));
        gCategory.setText(video.filename);*/

        return convertView;
    }
}


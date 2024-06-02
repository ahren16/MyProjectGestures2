package com.example.myprojectgestures2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class CategoryActivity extends AppCompatActivity {
    public static final String CATEGORY_ID_ARG="Category_ges";
    private long categoryId;
    private TextView fullCategoryName;
    private ListView listView;
    private VideoAdapter adapter;


    private static class CategoryLoader extends AsyncTask<Void, Void, Category>{
        private CategoryActivity categoryActivity;

        public CategoryLoader(CategoryActivity categoryActivity) {
            this.categoryActivity = categoryActivity;
        }


        @Override
        protected Category doInBackground(Void... voids) {
            DataBase dataBase = new DataBase(categoryActivity);
            return dataBase.getCategory(categoryActivity.categoryId);

        }

        @Override
        protected void onPostExecute(Category category) {
            if (category==null){
                return;
            }
            categoryActivity.fullCategoryName.setText(category.name);
        }
    }
    private static class VideosLoader extends AsyncTask<Void, Void, List<Video>>{
        public VideosLoader(CategoryActivity categoryActivity) {
            this.categoryActivity = categoryActivity;
        }

        private CategoryActivity categoryActivity;

        @Override
        protected List<Video> doInBackground(Void... voids) {
            DataBase dataBase = new DataBase(categoryActivity);
            List<Video> list = dataBase.CategoryAllVideo(categoryActivity.categoryId);
            return list;

        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            categoryActivity.adapter.refresh(videos);
            RefresherFromServer refresherFromServer = new RefresherFromServer(categoryActivity, videos);
            refresherFromServer.execute();
        }

    }
    private static class RefresherFromServer extends AsyncTask <Void, Void, Boolean>{
        private CategoryActivity categoryActivity;
        private List<Video> list;

        public RefresherFromServer(CategoryActivity categoryActivity, List<Video> list) {
            this.categoryActivity = categoryActivity;
            this.list = list;
        }

        @Override
        protected void onPostExecute(Boolean needRefresh) {
            if (needRefresh){
                categoryActivity.loadVideosFromDB();
            }

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            DataBase dataBase= new DataBase(categoryActivity);
            GesturesService gesturesService=RetrofitInstance.createService();
            boolean needRefresh = false;
            for (int i = 0; i< list.size(); i++){
                Video video=list.get(i);
                if (video.name!=null){
                    continue;
                }
                Call<VideoInfo> call = gesturesService.getVideoInfo(video.code);
                try {
                    VideoInfo videoInfo=call.execute().body();
                    if (videoInfo!=null){
                        video.comments = videoInfo.comments;
                        video.URL = videoInfo.url;
                        video.name=videoInfo.name;
                        video.version=videoInfo.version;
                        dataBase.update(video);
                        needRefresh = true;
                    }
                } catch (IOException e) {

                }


            }
            return needRefresh;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryId = getIntent().getLongExtra(CATEGORY_ID_ARG, 0);
        listView=(ListView)findViewById(R.id.category_videos_lv);
        fullCategoryName=(TextView)findViewById(R.id.full_category_name_tv);
        adapter= new VideoAdapter(this);
        listView.setAdapter(adapter);
        loadVideosFromDB();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(CategoryActivity.this, VideosActivity.class);
                intent.putExtra(VideosActivity.VIDEO_ID_ARG,id);
                startActivity(intent);
            }
        });




        CategoryLoader categoryLoader = new CategoryLoader(this);
        categoryLoader.execute();

    }
    private void loadVideosFromDB(){
        VideosLoader loader  =new VideosLoader(this);
        loader.execute();
    }

}
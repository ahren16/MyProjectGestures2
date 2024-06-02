package com.example.myprojectgestures2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private CategoriesAdapter adapter;
    private Button refreshButton;

    private static class DataBaseRefresher extends AsyncTask<Void, Void, Void>{
        private MainActivity activity;
        private List<CategoryInfo> categoryInfos;

        public DataBaseRefresher(MainActivity activity, List<CategoryInfo> categoryInfos) {
            this.activity = activity;
            this.categoryInfos = categoryInfos;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DataBase dataBase = new DataBase(activity);
            dataBase.deleteAllGesturesAndVideo();
            dataBase.deleteAllVideo();
            dataBase.deleteAllCategories();
            Map<String, Long> videoMap = new HashMap<>();
            for (int i=0; i<categoryInfos.size();i++){
                CategoryInfo ci= categoryInfos.get(i);
                long idCategory=dataBase.addCategory(ci.name, ci.shortName, ci.color);
                for (int j=0; j<ci.videos.size();j++){
                    String code=ci.videos.get(j);
                    if (videoMap.containsKey(code)){
                        dataBase.addVideoToCategory(idCategory,videoMap.get(code));

                    } else {
                        long idVideo=dataBase.addVideo(code);
                        videoMap.put(code, idVideo);
                        dataBase.addVideoToCategory(idCategory, idVideo);

                    }

                }
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            activity.loadCategoriesFromDB();
        }
    }

    private static class CategoriesLoader extends AsyncTask<Void, Void, List<Category>>{
        private Context context;
        private CategoriesAdapter categoriesAdapter;

        public CategoriesLoader(Context context, CategoriesAdapter categoriesAdapter) {
            this.context = context;
            this.categoriesAdapter = categoriesAdapter;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            DataBase db = new DataBase(context);
            return db.getAllCategories();
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            categoriesAdapter.refresh(categories);
        }
    }


    /*private static final List<Gesture> gestures = new ArrayList<Gesture>();

    static {
        gestures.add(new Gesture("Жесты при задержании", "полиция"));
        gestures.add(new Gesture("Оказание помощи", "Как себя чувствуешь"));
        gestures.add(new Gesture("Оказание помощи", "Вызвать ли скорую"));
        gestures.add(new Gesture("Жесты для сотрудников ДПС и ППС", "полиция"));
        gestures.add(new Gesture("Другое", "полиция"));

    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.list_view);
        adapter=new CategoriesAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent=new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra(CategoryActivity.CATEGORY_ID_ARG,id);
                startActivity(intent);
            }
        });


        loadCategoriesFromDB();

        refreshButton =findViewById(R.id.load_from_server_btn);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFromServer();
            }
        });





    }

    private void loadCategoriesFromDB(){
        CategoriesLoader loader= new CategoriesLoader(this, adapter);
        loader.execute();

    }
    private void refreshFromServer (){
        RetrofitInstance.createService().getAllCategories().enqueue(new Callback<List<CategoryInfo>>() {
            @Override
            public void onResponse(Call<List<CategoryInfo>> call, Response<List<CategoryInfo>> response) {
                List<CategoryInfo> categoryInfoList=response.body();
                if (categoryInfoList == null) {
                    Toast.makeText(MainActivity.this, "Ошибка получения данных", Toast.LENGTH_LONG).show();
                    return;
                }
                if (categoryInfoList.size()==0){
                    Toast.makeText(MainActivity.this, "Нет данных", Toast.LENGTH_LONG).show();
                    return;

                }
                DataBaseRefresher dataBaseRefresher= new DataBaseRefresher(MainActivity.this, categoryInfoList);
                dataBaseRefresher.execute();


            }

            @Override
            public void onFailure(Call<List<CategoryInfo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ошибка связи с сервером", Toast.LENGTH_LONG).show();
                t.printStackTrace();


            }
        });

    }



}
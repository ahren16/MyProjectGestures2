package com.example.myprojectgestures2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String DATABASE_NAME = "Gestures_of_the_police";
    private static final int DATABASE_VERSION = 2;
    private static final String CATEGORY_TABLE_NAME = "Category";
    private static final String CATEGORY_TABLE_COLUMN_ID = "_id";
    private static final String CATEGORY_TABLE_COLUMN_NAME="name";
    private static final String CATEGORY_TABLE_COLUMN_SHORT_NAME = "ShortName";
    private static final String CATEGORY_TABLE_COLUMN_COLOR ="color";




    private static final String VIDEO_TABLE_NAME = "Video";
    private static final String VIDEO_TABLE_COLUMN_ID = "_id";
    private static final String VIDEO_TABLE_COLUMN_URL = "url";
    private static final String VIDEO_TABLE_COLUMN_NAME = "name";
    private static final String VIDEO_TABLE_COMMENTS = "comments";
    private static final String VIDEO_TABLE_FILENAME = "filename";
    private static final String VIDEO_TABLE_VERSION = "version";
    private static final String VIDEO_TABLE_CODE ="code";





    private static final String GESTURES_AND_VIDEO_TABLE_NAME = "Gestures_and_video";
    private static final String GESTURES_AND_VIDEO_TABLE_COLUMN_CATEGORY_ID = "id_category";
   private static final String GESTURES_AND_VIDEO_TABLE_COLUMN_VIDEO_ID = "id_video";


    private SQLiteDatabase db;
    public DataBase (Context context){
        DataBaseOpenHelper openHelper= new DataBaseOpenHelper(context);
        db=openHelper.getWritableDatabase();



    }
    public List<Category> getAllCategories(){
        Cursor cursor= db.query(CATEGORY_TABLE_NAME, null,null, null, null, null, null);
        List<Category> answer= new ArrayList<>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            int idNum = cursor.getColumnIndex(CATEGORY_TABLE_COLUMN_ID);
            int NameNum = cursor.getColumnIndex(CATEGORY_TABLE_COLUMN_NAME);
            int ShortNameNum = cursor.getColumnIndex(CATEGORY_TABLE_COLUMN_SHORT_NAME);
            int ColorNum = cursor.getColumnIndex(CATEGORY_TABLE_COLUMN_COLOR);
            do{
                long id= cursor.getLong(idNum);

                String name= cursor.getString(NameNum);
                String shortName = cursor.getString(ShortNameNum);
                String color = cursor.getString(ColorNum);
                answer.add(new Category(id, name, shortName, color));
            }while (cursor.moveToNext());
        }
        return answer;
    }
    public List<Video> CategoryAllVideo(long id){
        Cursor cursor= db.query(GESTURES_AND_VIDEO_TABLE_NAME, null, GESTURES_AND_VIDEO_TABLE_COLUMN_CATEGORY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null );
        List<Long> idOfVideo=new ArrayList<>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            int idVideoNum = cursor.getColumnIndex(GESTURES_AND_VIDEO_TABLE_COLUMN_VIDEO_ID);
            do{
                long idVideo = cursor.getLong(idVideoNum);
                idOfVideo.add(idVideo);

            }while (cursor.moveToNext());


        }
        List<Video> answer = new ArrayList<>();
        if (idOfVideo.size()==0){
            return answer;
        }
        String questions = "?";
        for (int i=1; i<idOfVideo.size(); i++){
            questions = questions + ",?";

        }
        String [] stringIdOfVideos = new String[idOfVideo.size()];
        for (int i=0; i<idOfVideo.size(); i++){
            stringIdOfVideos[i]=String.valueOf(idOfVideo.get(i));


        }
        Cursor cursor1 = db.query(VIDEO_TABLE_NAME, null, VIDEO_TABLE_COLUMN_ID + " in ("+ questions +")", stringIdOfVideos,
                null, null, null);
        cursor1.moveToFirst();
        if (!cursor1.isAfterLast()){
            int numName = cursor1.getColumnIndex(VIDEO_TABLE_COLUMN_NAME);
            int numFileName= cursor1.getColumnIndex(VIDEO_TABLE_FILENAME);
            int numCode = cursor1.getColumnIndex(VIDEO_TABLE_CODE);
            int numId = cursor1.getColumnIndex(VIDEO_TABLE_COLUMN_ID);
            int numURL = cursor1.getColumnIndex(VIDEO_TABLE_COLUMN_URL);
            int numComments = cursor1.getColumnIndex(VIDEO_TABLE_COMMENTS);
            int numVersion = cursor1.getColumnIndex(VIDEO_TABLE_VERSION);

            do{
                String name= cursor1.getString(numName);
                String fileName= cursor1.getString(numFileName);
                String code = cursor1.getString(numCode);
                long videoId = cursor1.getLong(numId);
                String URL = cursor1.getString(numURL);
                String comments = cursor1.getString(numComments);
                int version = cursor1.getInt(numVersion);

                answer.add(new Video(name, code, videoId,comments, URL, version, fileName ));
            }while (cursor1.moveToNext());
        }
        return answer;

    }
    public Category getCategory(long id){
        Cursor cursor=db.query(CATEGORY_TABLE_NAME, null, CATEGORY_TABLE_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            int NameNum = cursor.getColumnIndex(CATEGORY_TABLE_COLUMN_NAME);
            int ShortNameNum = cursor.getColumnIndex(CATEGORY_TABLE_COLUMN_SHORT_NAME);
            int ColorNum = cursor.getColumnIndex(CATEGORY_TABLE_COLUMN_COLOR);
            String name= cursor.getString(NameNum);
            String shortName = cursor.getString(ShortNameNum);
            String color = cursor.getString(ColorNum);
            return new Category(id, name, shortName, color);

        }
        return null;

    }
    public Video getVideo(long id){
        Cursor cursor= db.query(VIDEO_TABLE_NAME, null, VIDEO_TABLE_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            int NameVideo = cursor.getColumnIndex(VIDEO_TABLE_COLUMN_NAME);
            int CodeVideo = cursor.getColumnIndex(VIDEO_TABLE_CODE);
            int CommentsVideo = cursor.getColumnIndex(VIDEO_TABLE_COMMENTS);
            int URLVideo = cursor.getColumnIndex(VIDEO_TABLE_COLUMN_URL);
            int VersionVideo = cursor.getColumnIndex(VIDEO_TABLE_VERSION);
            int FilenameVideo= cursor.getColumnIndex(VIDEO_TABLE_FILENAME);
            String name = cursor.getString(NameVideo);
            String code = cursor.getString(CodeVideo);
            String comments = cursor.getString(CommentsVideo);
            String url = cursor.getString(URLVideo);
            int version = cursor.getInt(VersionVideo);
            String fileName  = cursor.getString(FilenameVideo);
            return new Video(name, code, id, comments, url, version, fileName);
        }
        return null;
    }


    public void deleteAllCategories(){
        db.delete(CATEGORY_TABLE_NAME, null, null);

    }
    public void deleteAllVideo(){
        db.delete(VIDEO_TABLE_NAME,null, null);
    }
    public void deleteAllGesturesAndVideo(){
        db.delete(GESTURES_AND_VIDEO_TABLE_NAME, null, null);
    }


    public long addCategory (String name, String shortName, String color){
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_TABLE_COLUMN_NAME, name);
        cv.put(CATEGORY_TABLE_COLUMN_SHORT_NAME, shortName);
        cv.put(CATEGORY_TABLE_COLUMN_COLOR, color);
        return db.insert(CATEGORY_TABLE_NAME, null, cv );

    }
    public long addVideo (String code ){
        ContentValues cv = new ContentValues();
        cv.put(VIDEO_TABLE_CODE, code);
        return db.insert(VIDEO_TABLE_NAME, null, cv);
    }
    public void addVideoToCategory (long idCategory, long idVideo){
        ContentValues cv = new ContentValues();
        cv.put(GESTURES_AND_VIDEO_TABLE_COLUMN_CATEGORY_ID, idCategory);
        cv.put(GESTURES_AND_VIDEO_TABLE_COLUMN_VIDEO_ID, idVideo);
        db.insert(GESTURES_AND_VIDEO_TABLE_NAME, null, cv);

    }
    public int update (Video video){
        ContentValues cv = new ContentValues();
        cv.put(VIDEO_TABLE_COMMENTS, video.comments);
        cv.put(VIDEO_TABLE_CODE, video.code);
        cv.put(VIDEO_TABLE_FILENAME, video.filename);
        cv.put(VIDEO_TABLE_COLUMN_NAME, video.name);
        cv.put(VIDEO_TABLE_COLUMN_URL, video.URL);
        cv.put(VIDEO_TABLE_VERSION, video.version);
        return db.update(VIDEO_TABLE_NAME, cv, VIDEO_TABLE_COLUMN_ID + "=?",
                new String[]{String.valueOf(video.id)});

    }




    private static class DataBaseOpenHelper extends SQLiteOpenHelper {


        public DataBaseOpenHelper (Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }



    /*DataBaseOpenHelper (Context context){
        super(context, DATABASE1_NAME, null, DATABASE1_VERSION );
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super (context, DATABASE2_NAME, null, DATABASE2_VERSION );*/


        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + CATEGORY_TABLE_NAME + " (" +
                    CATEGORY_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORY_TABLE_COLUMN_NAME + " TEXT, " +
                    CATEGORY_TABLE_COLUMN_SHORT_NAME + " TEXT, " +
                    CATEGORY_TABLE_COLUMN_COLOR + " TEXT " +
                    ")";
            db.execSQL(query);


            String query1 = "CREATE TABLE " + VIDEO_TABLE_NAME + " (" +
                    VIDEO_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    VIDEO_TABLE_COLUMN_NAME + " TEXT, " +
                    VIDEO_TABLE_COLUMN_URL + " TEXT," +
                    VIDEO_TABLE_COMMENTS + " TEXT, "+
                    VIDEO_TABLE_FILENAME + " TEXT, "+
                    VIDEO_TABLE_VERSION + " TEXT, " +
                    VIDEO_TABLE_CODE + " TEXT "+
                    ")";
            db.execSQL(query1);


            String query2 = "CREATE TABLE " + GESTURES_AND_VIDEO_TABLE_NAME + " (" +
                    GESTURES_AND_VIDEO_TABLE_COLUMN_CATEGORY_ID + " INTEGER, " +
                    GESTURES_AND_VIDEO_TABLE_COLUMN_VIDEO_ID + " INTEGER "+
                    ")";
            db.execSQL(query2);

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);

            db.execSQL("DROP TABLE IF EXISTS " + VIDEO_TABLE_NAME);

            db.execSQL("DROP TABLE IF EXISTS " + GESTURES_AND_VIDEO_TABLE_NAME);
            onCreate(db);


        }

    }

}

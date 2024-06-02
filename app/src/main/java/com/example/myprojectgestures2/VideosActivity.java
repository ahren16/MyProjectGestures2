package com.example.myprojectgestures2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.nio.channels.AsynchronousChannelGroup;

public class VideosActivity extends AppCompatActivity {
    public static final String VIDEO_ID_ARG = "Video_ges";
    private TextView fullVideoName;
    private TextView comments;
    private ListView listView;
    private VideoView videoView;
    private long videoId;
    private ProgressBar progressBar;

    private static class VideoLoader extends AsyncTask<Void, Void, Video>{


        private VideosActivity videosActivity;
        public VideoLoader(VideosActivity videosActivity) {
            this.videosActivity = videosActivity;
        }

        @Override
        protected void onPostExecute(Video video) {
            if (video == null){
                return;
            }
            videosActivity.fullVideoName.setText(video.name);
            videosActivity.comments.setText(video.comments);

            videosActivity.videoView.setVideoURI(Uri.parse(video.URL));
            videosActivity.videoView.requestFocus();
        }

        @Override
        protected Video doInBackground(Void... voids) {
            DataBase db = new DataBase(videosActivity);
            return db.getVideo(videosActivity.videoId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_videos);

        videoId = getIntent().getLongExtra(VIDEO_ID_ARG, 0);
        comments=(TextView)findViewById(R.id.comments_video);
        videoView=(VideoView)findViewById(R.id.video);
        fullVideoName = (TextView) findViewById(R.id.full_video_name_tv);


        progressBar=(ProgressBar)findViewById(R.id.progress_bar_video);


        videoView.setMediaController(new MediaController(this));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.INVISIBLE);
                //videoView.setVisibility(View.VISIBLE);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressBar.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                Toast.makeText(VideosActivity.this, "Не получилось показать видео", Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        VideoLoader videoLoader = new VideoLoader(this);
        videoLoader.execute();
    }

}
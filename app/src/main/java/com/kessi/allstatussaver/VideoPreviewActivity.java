package com.kessi.allstatussaver;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.kessi.allstatussaver.utils.AdManager;


public class VideoPreviewActivity extends AppCompatActivity {

    VideoView displayVV;
    ImageView backIV;
    String videoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        videoPath = getIntent().getStringExtra("videoPath");
        displayVV = findViewById(R.id.displayVV);

        displayVV.setVideoPath(videoPath);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(displayVV);

        displayVV.setMediaController(mediaController);

        displayVV.start();

    }


    @Override
    protected void onResume() {
        super.onResume();
        displayVV.setVideoPath(videoPath);
        displayVV.start();
    }


}

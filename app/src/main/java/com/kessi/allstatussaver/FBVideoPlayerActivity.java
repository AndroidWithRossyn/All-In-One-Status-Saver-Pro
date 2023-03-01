package com.kessi.allstatussaver;

import java.io.IOException;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kessi.allstatussaver.utils.VideoControllerView;


public class FBVideoPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    private static boolean mFullScreen = true;
    SurfaceView videoSurface;
    static MediaPlayer player;
    VideoControllerView controller;
    String filepath;
    String fromStreaming;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        filepath = extras.getString("videofilename");
        fromStreaming = extras.getString("fromStreaming");
        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            if (fromStreaming == null) {
                player.setDataSource(filepath);
            } else {
                Uri video = Uri.parse(filepath);
                player.setDataSource(getApplicationContext(), video);
            }
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        videoSurface.setLayoutParams(lp);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(player!=null) {
            return player.getCurrentPosition();
        }
        else {
            return 0;
        }
    }

    @Override
    public int getDuration() {
        if(player!=null) {
            return player.getDuration();
        }
        else {
            return 0;
        }
    }

    @Override
    public boolean isPlaying() {
        if(player!=null) {
            return player.isPlaying();
        }
        else
        {
            return false;
        }
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void stop() {
        player.stop();
        player.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        { //Back key pressed
            //Things to Do
            if(player!= null)
            {
                player.stop();
                player=null;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean isFullScreen() {
        if (mFullScreen) {
            Log.v("FullScreen", "--set icon full screen--");
            return false;
        } else {
            Log.v("FullScreen", "--set icon small full screen--");
            return true;
        }
    }

    @Override
    public void toggleFullScreen() {
        Log.v("FullScreen", "-----------------click toggleFullScreen-----------");
        setFullScreen(isFullScreen());

    }
// End VideoMediaController.MediaPlayerControl

    public void setFullScreen(boolean fullScreen) {
        fullScreen = false;

        if (mFullScreen)

        {
            Log.v("FullScreen", "-----------Set full screen SCREEN_ORIENTATION_LANDSCAPE------------");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoSurface.getLayoutParams();
            params.width = width;
            params.height = height;
            params.setMargins(0, 0, 0, 0);
            //set icon is full screen
            mFullScreen = fullScreen;
        } else {
            Log.v("FullScreen", "-----------Set small screen SCREEN_ORIENTATION_PORTRAIT------------");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            final FrameLayout mFrame = (FrameLayout) findViewById(R.id.videoSurfaceContainer);
            // int height = displaymetrics.heightPixels;
            int height = mFrame.getHeight();//get height Frame Container video
            int width = displaymetrics.widthPixels;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoSurface.getLayoutParams();
            params.width = width;
            params.height = height;
            params.setMargins(0, 0, 0, 0);
            //set icon is small screen
            mFullScreen = !fullScreen;
        }
    }
}


package com.divinetechs.gotstart.YouTube;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.divinetechs.gotstart.R;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayer extends YouTubeActivity implements
        YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;
    String url;
    String[] Video_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.youtubeplayer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            Log.e("url", "" + url);
            if (!url.isEmpty()) {
                if (url.contains("watch")) {
                    Video_ID = url.split("v=");
                    Log.e("Video_ID[1]", "" + Video_ID[1]);
                } else if (url.contains("youtu.be")) {
                    Video_ID = url.split("//youtu.be/");
                    Log.e("Video_ID[1]", "" + Video_ID[1]);
                } else {
                    Toast.makeText(YoutubePlayer.this, "Youtube Video link is not Valid", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(YoutubePlayer.this, "Youtube Video link is not Valid", Toast.LENGTH_LONG).show();
            }
        }

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        // Initializing video player with developer key
        youTubeView
                .initialize(
                        getString(R.string.youtube_api_key),
                        this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        // TODO Auto-generated method stub
        if (!wasRestored) {
            if (!url.isEmpty()) {
                player.loadVideo(Video_ID[1]);
            }
        }

    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        // TODO Auto-generated method stub
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

}

package com.sanoj.devildeveloper.enoughmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gurudev.fullscreenvideowebview.FullScreenVideoWebView;

public class GeckoView extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecko_view);

        FullScreenVideoWebView fullScreenVideoWebView = findViewById(R.id.webView);
        fullScreenVideoWebView.loadUrl("https://www.2embed.ru/embed/imdb/movie?id=tt0369610");





    }
}
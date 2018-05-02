package com.hale.robert.discjockey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_about);
        final ImageView image = (ImageView) findViewById(R.id.profile_pic);
        Glide.with(getApplicationContext())
                .load("https://i.imgur.com/mf7JSRj.jpg")
                .centerCrop()
                .into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext())
                        .load("https://i.imgur.com/F5Y7b3M.jpg?2")
                        .centerCrop().into(image);
            }
        });
    }
}

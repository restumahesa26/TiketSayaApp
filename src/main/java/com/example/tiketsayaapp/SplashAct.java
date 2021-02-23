package com.example.tiketsayaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAct extends AppCompatActivity {

    Animation app_splash, app_splash_2;
    ImageView app_logo;
    TextView app_tagline;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getUsernameLocal();

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        app_splash_2 = AnimationUtils.loadAnimation(this, R.anim.app_splash_2);

        app_logo = findViewById(R.id.app_logo);
        app_tagline = findViewById(R.id.app_tagline);

        app_logo.startAnimation(app_splash);
        app_tagline.startAnimation(app_splash_2);

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
        if(username_key_new.isEmpty()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gogetstarted = new Intent(SplashAct.this, GetStartedAct.class);
                    startActivity(gogetstarted);
                    finish();
                }
            }, 2000);
        }else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gohome = new Intent(SplashAct.this, HomeAct.class);
                    startActivity(gohome);
                    finish();
                }
            }, 2000);
        }
    }
}
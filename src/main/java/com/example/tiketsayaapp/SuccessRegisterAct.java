package com.example.tiketsayaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessRegisterAct extends AppCompatActivity {

    Button btn_explore;
    ImageView icon_success_register;
    TextView text_title, text_desc;
    Animation app_splash, app_splash_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        app_splash_2 = AnimationUtils.loadAnimation(this, R.anim.app_splash_2);

        btn_explore = findViewById(R.id.btn_explore);
        icon_success_register = findViewById(R.id.icon_success_register);
        text_title = findViewById(R.id.text_title);
        text_desc = findViewById(R.id.text_desc);

        btn_explore.startAnimation(app_splash_2);
        icon_success_register.startAnimation(app_splash);
        text_title.startAnimation(app_splash);
        text_desc.startAnimation(app_splash);

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoexplore = new Intent(SuccessRegisterAct.this, HomeAct.class);
                startActivity(gotoexplore);
            }
        });
    }
}
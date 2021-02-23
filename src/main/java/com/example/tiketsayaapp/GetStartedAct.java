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

public class GetStartedAct extends AppCompatActivity {

    Button btn_sign_in, btn_new_account;
    ImageView app_emblem;
    TextView app_title;
    Animation app_get_started, app_splash_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        app_get_started = AnimationUtils.loadAnimation(this, R.anim.app_get_started);
        app_splash_2 = AnimationUtils.loadAnimation(this, R.anim.app_splash_2);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_new_account = findViewById(R.id.btn_new_account);

        app_emblem = findViewById(R.id.app_emblem);
        app_title = findViewById(R.id.app_title);

        app_emblem.startAnimation(app_get_started);
        app_title.startAnimation(app_get_started);
        btn_sign_in.startAnimation(app_splash_2);
        btn_new_account.startAnimation(app_splash_2);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosign = new Intent(GetStartedAct.this, SignInAct.class);
                startActivity(gotosign);
            }
        });

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(GetStartedAct   .this, RegisterOneAct.class);
                startActivity(gotoregisterone);
            }
        });

    }
}
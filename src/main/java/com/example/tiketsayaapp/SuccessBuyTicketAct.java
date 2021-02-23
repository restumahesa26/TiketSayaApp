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

public class SuccessBuyTicketAct extends AppCompatActivity {

    Button btn_view_ticket, btn_dashboard;
    TextView text_selamat_menikmati, text_berhasil_desc;
    ImageView icon_success_buy;
    Animation app_splash, app_splash_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        app_splash_2 = AnimationUtils.loadAnimation(this, R.anim.app_splash_2);

        btn_view_ticket = findViewById(R.id.btn_view_ticket);
        btn_dashboard = findViewById(R.id.btn_dashboard);
        text_selamat_menikmati = findViewById(R.id.text_selamat_menikmati);
        text_berhasil_desc = findViewById(R.id.text_berhasil_desc);
        icon_success_buy = findViewById(R.id.icon_success_buy);

        icon_success_buy.startAnimation(app_splash);
        text_selamat_menikmati.startAnimation(app_splash);
        text_berhasil_desc.startAnimation(app_splash);
        btn_view_ticket.startAnimation(app_splash_2);
        btn_dashboard.startAnimation(app_splash_2);

        btn_view_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketdetail = new Intent(SuccessBuyTicketAct.this, MyTicketDetailAct.class);
                startActivity(gototicketdetail);
            }
        });

        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(SuccessBuyTicketAct.this, HomeAct.class);
                startActivity(gotohome);
            }
        });

    }
}
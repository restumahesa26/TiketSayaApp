package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_pay_now, btn_mines, btn_plus;
    LinearLayout btn_back;
    ImageView icon_cancel_pay;
    TextView text_jumlah_tiket, jumlah_harga, nama_wisata, lokasi, ketentuan_tiket, user_balances;
    Integer value_jumlah_tiket = 1, u_balance = 350, harga_tiket_awal = 200, total_harga = 0;
    Animation low_opacity, app_slash_2;

    DatabaseReference reference, reference2, reference3;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    Integer nomor_tiket = new Random().nextInt(), user_balance_new;

    String time_wisata = "";
    String date_wisata = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        low_opacity = AnimationUtils.loadAnimation(this, R.anim.low_opacity);
        app_slash_2 = AnimationUtils.loadAnimation(this, R.anim.app_splash_2);

        btn_back = findViewById(R.id.btn_back);
        btn_pay_now = findViewById(R.id.btn_pay_now);
        btn_mines = findViewById(R.id.btn_mines);
        btn_plus = findViewById(R.id.btn_plus);
        text_jumlah_tiket = findViewById(R.id.text_jumlah_tiket);
        jumlah_harga = findViewById(R.id.jumlah_harga);
        icon_cancel_pay = findViewById(R.id.icon_cancel_pay);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan_tiket = findViewById(R.id.ketentuan_tiket);
        user_balances = findViewById(R.id.user_balances);

        icon_cancel_pay.animate().alpha(0).start();

        text_jumlah_tiket.setText(value_jumlah_tiket.toString());

        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u_balance = Integer.parseInt(dataSnapshot.child("user_balance").getValue().toString());
                user_balances.setText(" $ " +u_balance.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Integer harga_tiket = Integer.parseInt(dataSnapshot.child("harga_tiket").getValue().toString());

                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan_tiket.setText(dataSnapshot.child("ketentuan").getValue().toString());
                jumlah_harga.setText("US$ " + harga_tiket.toString());

                harga_tiket_awal = harga_tiket;
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();
                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_mines.setVisibility(View.INVISIBLE);

        btn_mines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(value_jumlah_tiket == 1) {
                    text_jumlah_tiket.setText(value_jumlah_tiket.toString());
                    jumlah_harga.setText("US$ "+ total_harga.toString());
                    btn_pay_now.animate().alpha(1).setDuration(350).start();
                    btn_mines.setVisibility(View.INVISIBLE);
                }else {
                    value_jumlah_tiket = value_jumlah_tiket - 1;
                    total_harga = harga_tiket_awal * value_jumlah_tiket;
                    text_jumlah_tiket.setText(value_jumlah_tiket.toString());
                    jumlah_harga.setText("US$ "+ total_harga.toString());
                    if(value_jumlah_tiket == 1) {
                        btn_mines.setVisibility(View.INVISIBLE);
                    }else {
                        btn_mines.setVisibility(View.VISIBLE);
                    }
                    if(total_harga > u_balance) {
                        btn_pay_now.animate().translationY(350).alpha(0).setDuration(300).start();
                        icon_cancel_pay.animate().alpha(1).setDuration(200).start();
                    }else {
                        btn_pay_now.animate().translationY(0).alpha(1).setDuration(300).start();
                        icon_cancel_pay.animate().alpha(0).setDuration(200).start();
                    }
                }
            }
        });

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_mines.setVisibility(View.VISIBLE);
                value_jumlah_tiket = value_jumlah_tiket + 1;
                total_harga = harga_tiket_awal * value_jumlah_tiket;
                text_jumlah_tiket.setText(value_jumlah_tiket.toString());
                jumlah_harga.setText("US$ "+ total_harga.toString());
                if(total_harga > u_balance) {
                    btn_pay_now.animate().translationY(350).alpha(0).setDuration(300).start();
                    icon_cancel_pay.animate().alpha(1).setDuration(200).start();
                }else {
                    btn_pay_now.animate().translationY(0).alpha(1).setDuration(300).start();
                    icon_cancel_pay.animate().alpha(0).setDuration(200).start();
                }
            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference3 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new).child(nama_wisata.getText().toString() + nomor_tiket);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + nomor_tiket);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan_tiket.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(value_jumlah_tiket.toString());
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                        reference3.getRef().child("date_wisata").setValue(date_wisata);

                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                u_balance = Integer.parseInt(dataSnapshot.child("user_balance").getValue().toString());
                                user_balance_new = u_balance - total_harga;
                                reference2.getRef().child("user_balance").setValue(user_balance_new);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent gotosuccessticket = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                        startActivity(gotosuccessticket);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoticketdetail = new Intent(TicketCheckoutAct.this, TicketDetailAct.class);
                startActivity(backtoticketdetail);
            }
        });

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
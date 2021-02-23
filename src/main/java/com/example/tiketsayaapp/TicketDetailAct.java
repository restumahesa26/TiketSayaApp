package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailAct extends AppCompatActivity {

    Button btn_buy_ticket;
    LinearLayout btn_back;
    TextView nama_wisata, lokasi, photo_spot_ticket, wifi_ticket, festival_ticket, short_desc_ticket;
    ImageView img_header;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btn_back = findViewById(R.id.btn_back);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        photo_spot_ticket = findViewById(R.id.photo_spot_ticket);
        wifi_ticket = findViewById(R.id.wifi_ticket);
        festival_ticket = findViewById(R.id.festival_ticket);
        short_desc_ticket = findViewById(R.id.short_desc_ticket);
        img_header = findViewById(R.id.img_header);

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String is_wifi = dataSnapshot.child("is_wifi").getValue().toString();
                String is_photo_spot = dataSnapshot.child("is_photo_spot").getValue().toString();
                String is_festival = dataSnapshot.child("is_festival").getValue().toString();
                String short_desc = dataSnapshot.child("short_desc").getValue().toString();

                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                photo_spot_ticket.setText(is_photo_spot);
                wifi_ticket.setText(is_wifi);
                festival_ticket.setText(is_festival);
                short_desc_ticket.setText(short_desc);
                Picasso.with(TicketDetailAct.this).load(dataSnapshot.child("url_thumbnail").getValue().toString()).centerCrop().fit().into(img_header);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotocheckout = new Intent(TicketDetailAct.this, TicketCheckoutAct.class);
                gotocheckout.putExtra("jenis_tiket", jenis_tiket_baru);
                startActivity(gotocheckout);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome = new Intent(TicketDetailAct.this, HomeAct.class);
                startActivity(backtohome);
            }
        });

    }
}
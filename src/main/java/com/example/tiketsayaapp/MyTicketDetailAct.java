package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetailAct extends AppCompatActivity {

    LinearLayout btn_back;
    TextView nama_wisata, lokasi, date_wisata, time_wisata, informasi_wisata;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        btn_back = findViewById(R.id.btn_back);
        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        date_wisata = findViewById(R.id.date_wisata);
        time_wisata = findViewById(R.id.time_wisata);
        informasi_wisata = findViewById(R.id.informasi_wisata);

        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_bundle = bundle.getString("nama_wisata");

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_bundle);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                date_wisata.setText(dataSnapshot.child("date_wisata").getValue().toString());
                time_wisata.setText(dataSnapshot.child("time_wisata").getValue().toString());
                informasi_wisata.setText(dataSnapshot.child("ketentuan").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtomyprofile = new Intent(MyTicketDetailAct.this, MyProfileAct.class);
                startActivity(backtomyprofile);
            }
        });

    }
}
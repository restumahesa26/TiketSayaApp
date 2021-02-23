package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInAct extends AppCompatActivity {

    TextView btn_new_account;
    Button btn_sign_in;
    EditText username, password;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_new_account = findViewById(R.id.btn_new_account);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(SignInAct.this, RegisterOneAct.class);
                startActivity(gotoregisterone);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Username masih kosong!", Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Password masih kosong!", Toast.LENGTH_SHORT).show();
                }else {
                    if(checkInternet()) {
                        btn_sign_in.setEnabled(false);
                        btn_sign_in.setText("Process...");
                        final String username_2 = username.getText().toString();
                        final String password_2 = password.getText().toString();

                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_2);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                    if(password_2.equals(passwordFromFirebase)) {
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, username.getText().toString());
                                        editor.apply();

                                        Intent gotodashboard = new Intent(SignInAct.this, HomeAct.class);
                                        startActivity(gotodashboard);
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Password tidak cocok :(", Toast.LENGTH_SHORT).show();
                                        btn_sign_in.setEnabled(true);
                                        btn_sign_in.setText("SIGN IN");
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(),"Username tidak tersedia!", Toast.LENGTH_SHORT).show();
                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Database Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(),"Periksa Koneksi Internet :(", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    public boolean checkInternet(){
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ) {
            connectStatus = true;
        }
        else {
            connectStatus = false;
        }
        return connectStatus;
    }
}
package com.example.tiketsayaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileAct extends AppCompatActivity {

    Button btn_save_profile, btn_add_photo;
    LinearLayout btn_back;
    ImageView pic_photo_register_user;
    TextView nama_lengkap, bio, username, password, email_address;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference, reference2;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUsernameLocal();

        btn_save_profile = findViewById(R.id.btn_save_profile);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        btn_back = findViewById(R.id.btn_back);
        pic_photo_register_user = findViewById(R.id.pic_photo_register_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                username.setText(dataSnapshot.child("username").getValue().toString());
                password.setText(dataSnapshot.child("password").getValue().toString());
                email_address.setText(dataSnapshot.child("email_address").getValue().toString());
                Picasso.with(EditProfileAct.this).load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop().fit().into(pic_photo_register_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama_lengkap.equals("")) {
                    Toast.makeText(getApplicationContext(),"Nama Lengkap Masih Kosong", Toast.LENGTH_SHORT).show();
                }else if(bio.equals("")) {
                    Toast.makeText(getApplicationContext(),"Bio Masih Kosong", Toast.LENGTH_SHORT).show();
                }else if(username.equals("")) {
                    Toast.makeText(getApplicationContext(),"Username Masih Kosong", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")) {
                    Toast.makeText(getApplicationContext(),"Password Masih Kosong", Toast.LENGTH_SHORT).show();
                }else if(email_address.equals("")) {
                    Toast.makeText(getApplicationContext(),"Email Address Masih Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    btn_save_profile.setEnabled(false);
                    btn_save_profile.setText("Loading...");

                    storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                            dataSnapshot.getRef().child("bio").setValue(bio.getText().toString());
                            dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                            dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                            dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    if(photo_location != null) {
                        final StorageReference storageReference = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                        storageReference.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();
                                        reference.getRef().child("url_photo_profile").setValue(url);
                                    }
                                });
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Intent gotosaveprofile = new Intent(EditProfileAct.this, MyProfileAct.class);
                                startActivity(gotosaveprofile);
                            }
                        });
                    }else {
                        Intent gotosaveprofile = new Intent(EditProfileAct.this, MyProfileAct.class);
                        startActivity(gotosaveprofile);
                    }
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtomyprofile = new Intent(EditProfileAct.this, MyProfileAct.class);
                startActivity(backtomyprofile);
            }
        });

    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register_user);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
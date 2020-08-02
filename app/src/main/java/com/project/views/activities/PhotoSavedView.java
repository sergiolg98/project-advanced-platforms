package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.R;
import com.project.config.ConstValue;

public class PhotoSavedView extends AppCompatActivity {

    ImageView previewPhoto;
    TextView txUsername;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_saved_view);
        initWidgets();

        pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);
        txUsername.setText("@" + pref.getString("username_short", ""));
        Glide.with(this).load(Environment.getExternalStorageDirectory() + "/" + ConstValue.getPhotoPath()).into(previewPhoto);

    }

    private void initWidgets(){
        previewPhoto = findViewById(R.id.img_preview);
        txUsername = findViewById(R.id.tx_username_preview);
    }
}

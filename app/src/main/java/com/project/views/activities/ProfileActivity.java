package com.project.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.project.R;

public class ProfileActivity extends AppCompatActivity {

    TextView txFullName, txEmail, txUsername;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initWidgets();
        setContents();




    }

    private void initWidgets(){
        txFullName = findViewById(R.id.tx_full_name);
        txEmail = findViewById(R.id.tx_mail);
        txUsername = findViewById(R.id.tx_username);
    }

    private void setContents(){
        pref = getSharedPreferences("userAuthenticated", Context.MODE_PRIVATE);
        txUsername.setText(pref.getString("username_short", ""));
        txFullName.setText(pref.getString("userName", "") + " " + pref.getString("userLastname", ""));
        txEmail.setText(pref.getString("userEmail", ""));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

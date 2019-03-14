package com.electriccouriers.bass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.electriccouriers.bass.R;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: First run detection

        // IF user is loggedin
        startActivity(new Intent(this, HomeActivity.class));

        // IF not
//        startActivity(new Intent(this, LoginActivity.class));

        finish();
    }
}

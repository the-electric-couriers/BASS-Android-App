package com.electriccouriers.bass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.preferences.PreferenceHelper;

public class InitActivity extends AppCompatActivity {

    private Intent initIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initIntent = new Intent(this, HomeActivity.class);

        if(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN, "").isEmpty()) {
            initIntent = new Intent(this, StartActivity.class);
        }

        startActivity(initIntent);
        finish();
    }
}

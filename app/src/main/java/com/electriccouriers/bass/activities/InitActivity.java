package com.electriccouriers.bass.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.preferences.PreferenceHelper;

import java.util.Random;

public class InitActivity extends AppCompatActivity {

    private Intent initIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bass_light_apache));

        initIntent = new Intent(this, HomeActivity.class);

        if(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN, "").isEmpty()) {
            initIntent = new Intent(this, StartActivity.class);
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(initIntent);
            finish();
        }, new Random().nextInt((3000 - 800) + 1) + 800);
    }
}

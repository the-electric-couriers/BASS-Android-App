package com.electriccouriers.bass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.electriccouriers.bass.R;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.loginButton1).setOnClickListener(v -> {
            onClickLoginButton1();
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_start;
    }

    @Override
    protected int getToolbarTitle() {
        return 0;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return 0;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }

    public void onClickLoginButton1(){
        openAcitivity(new Intent(StartActivity.this, LoginActivity.class), true);
    }
}

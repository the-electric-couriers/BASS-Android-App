package com.electriccouriers.bass.activities;

import com.electriccouriers.bass.R;

public class StartActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return 0;
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
        setContentView(R.layout.activity_login);
    }

    public void onClickMeerInfo(){
        setContentView(R.layout.activity_home);
    }
}

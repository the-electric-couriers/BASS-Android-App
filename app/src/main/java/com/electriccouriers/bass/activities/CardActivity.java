package com.electriccouriers.bass.activities;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.electriccouriers.bass.R;

public class CardActivity extends BaseActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bass_light_apache));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_kaart;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.home_toolbar_title;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_back_white;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }

    public void setSingleRow(){
        //TODO: create function(s) that take information and put it in the scrollview
        //TODO: rewrite the whole activity_kaart.xml because i just  remembered that you can use a tableview
    }


}
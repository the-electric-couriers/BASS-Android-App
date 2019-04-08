package com.electriccouriers.bass.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.helpers.API;
import com.electriccouriers.bass.helpers.BackgroundSoundService;
import com.electriccouriers.bass.helpers.ProgressDialogHelper;
import com.electriccouriers.bass.models.RoutePoint;
import com.electriccouriers.bass.models.User;
import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends BaseActivity {

    private Button request;
    private TextView beginLocationText, timeText, endLocationText;
    private ImageView shuttleImage;
    private ArrayList<RoutePoint> routePoints = new ArrayList<>();

    private Integer selectedStartLocation, selectedEndLocation;
    private User mainUser;
    private Calendar selectedTimestamp;
    private Integer clicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bass_light_apache));

        mainUser = User.create(PreferenceHelper.read(this, Globals.PrefKeys.MAIN_USER));
        clicks = 0;

        beginLocationText = findViewById(R.id.textview_request_begin_location);
        timeText = findViewById(R.id.textview_request_time);
        endLocationText = findViewById(R.id.textview_request_end_location);
        shuttleImage = findViewById(R.id.imageView_request_shuttle);

        getRoutePoints();
        initLocations();

        shuttleImage.setOnClickListener(v -> shuttleClickListener());

        findViewById(R.id.layout_request_begin_location).setOnClickListener(v -> {
            PopupMenu pm = new PopupMenu(RequestActivity.this, findViewById(R.id.layout_request_begin_location));
            pm.getMenuInflater().inflate(R.menu.popup_menu, pm.getMenu());
            for (RoutePoint item : routePoints) {
                pm.getMenu().add(item.getName());
            }

            pm.setOnMenuItemClickListener(item -> {
                for (RoutePoint routePoint : routePoints) {
                    if(item.getTitle() == routePoint.getName()) {
                        beginLocationText.setText(item.getTitle());
                        selectedStartLocation = routePoint.getID();
                    }
                }

                return true;
            });
            pm.show();
        });


        findViewById(R.id.layout_request_end_location).setOnClickListener(v -> {
            PopupMenu pm = new PopupMenu(RequestActivity.this, findViewById(R.id.layout_request_end_location));
            pm.getMenuInflater().inflate(R.menu.popup_menu, pm.getMenu());
            for (RoutePoint item : routePoints) {
                pm.getMenu().add(item.getName());
            }

            pm.setOnMenuItemClickListener(item -> {
                for (RoutePoint routePoint : routePoints) {
                    if(item.getTitle() == routePoint.getName()) {
                        endLocationText.setText(item.getTitle());
                        selectedEndLocation = routePoint.getID();
                    }
                }

                return true;
            });

            pm.show();
        });


        Calendar mcurrentTime = Calendar.getInstance();
        selectedTimestamp = (Calendar) mcurrentTime.clone();
        selectedTimestamp.add(Calendar.MINUTE, 15);
        selectedTimestamp.set(Calendar.AM_PM, 1);

        int hour = selectedTimestamp.get(Calendar.HOUR_OF_DAY);
        int minute = selectedTimestamp.get(Calendar.MINUTE);

        findViewById(R.id.layout_request_time).setOnClickListener(v -> {
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(RequestActivity.this, (view, selectedHour, selectedMinute) -> {
                timeText.setText(formatTime(selectedHour, selectedMinute));
                selectedTimestamp.set(Calendar.HOUR_OF_DAY, selectedHour);
                selectedTimestamp.set(Calendar.MINUTE, selectedMinute);
            }, hour, minute, true);
            mTimePicker.setTitle("Selecteer een tijdstip");
            mTimePicker.show();
        });

        timeText.setText(formatTime(hour, minute));

        request = findViewById(R.id.requestbutton);
        request.setOnClickListener(v -> requestRide());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_request;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.login_toolbar_title;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_back_white;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }

    private String formatTime(int hour, int minute) {
        String hourString = String.valueOf(hour);
        String minuteString = String.valueOf(minute);

        if(hourString.length() == 1)
            hourString = "0" + hourString;

        if(minuteString.length() == 1)
            minuteString = "0" + minuteString;


        return hourString + ":" + minuteString;
    }

    private void shuttleClickListener() {
        if(clicks < 5) {
            clicks += 1;
        } else {
            startService(new Intent(this, BackgroundSoundService.class));
        }

        System.out.println(clicks);


    }

    private void initLocations() {
        int lastStartLoc = PreferenceHelper.read(this, Globals.PrefKeys.LROUTE_START, 0);
        int lastEndLoc = PreferenceHelper.read(this, Globals.PrefKeys.LROUTE_END, 0);

        selectedStartLocation = lastStartLoc;

        if(lastEndLoc <= 1) {
            selectedEndLocation = mainUser.getCompany().getRoutePointID();
            endLocationText.setText(mainUser.getCompany().getName());

            selectedStartLocation = 1;
            beginLocationText.setText("Station");
        } else {
            selectedStartLocation = mainUser.getCompany().getCompanyID();
            beginLocationText.setText(mainUser.getCompany().getName());

            selectedEndLocation = 1;
            endLocationText.setText("Station");
        }
    }

    private void rideSuccessful(Integer routeID) {
        PreferenceHelper.save(this, Globals.PrefKeys.LROUTE_START, selectedStartLocation);
        PreferenceHelper.save(this, Globals.PrefKeys.LROUTE_END, selectedEndLocation);
        PreferenceHelper.save(this, Globals.PrefKeys.CROUTE_ID, routeID);
        PreferenceHelper.save(this, Globals.PrefKeys.CROUTE_STATE, Globals.RouteState.REQUESTED.ordinal());

        selectedTimestamp.add(Calendar.MINUTE, 10);
        int hour = selectedTimestamp.get(Calendar.HOUR_OF_DAY);
        int minute = selectedTimestamp.get(Calendar.MINUTE);

        PreferenceHelper.save(this, Globals.PrefKeys.CROUTE_ATIME, formatTime(hour, minute));

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            ProgressDialogHelper.getInstance(this).hide();
            onBackPressed();
        }, new Random().nextInt((1000 - 800) + 1) + 800);
    }

    private void requestRide() {
        ProgressDialogHelper.getInstance(this).show();
        API.service().requestRide(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN), mainUser.getUserID(), selectedStartLocation, selectedEndLocation).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                assert response.body() != null;
                if(response.isSuccessful() && response.body().getAsJsonObject().get("success").getAsBoolean()) {
                    rideSuccessful(response.body().getAsJsonObject().get("routeID").getAsInt());
                    return;
                }

                Log.e("Error", response.message());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("Error", t.getLocalizedMessage());
            }
        });
    }

    private void getRoutePoints() {
        API.service().routePoints(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN)).enqueue(new Callback<ArrayList<RoutePoint>>() {
            @Override
            public void onResponse(Call<ArrayList<RoutePoint>> call, Response<ArrayList<RoutePoint>> response) {
                if(response.isSuccessful()) {
                    routePoints = response.body();
                } else {
                    Log.e("ERROR", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RoutePoint>> call, Throwable t) {
                Log.e("ERROR", t.getLocalizedMessage());
            }
        });
    }
}

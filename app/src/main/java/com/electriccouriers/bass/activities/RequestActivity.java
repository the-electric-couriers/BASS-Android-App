package com.electriccouriers.bass.activities;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.helpers.API;
import com.electriccouriers.bass.helpers.ProgressDialogHelper;
import com.electriccouriers.bass.models.RoutePoint;
import com.electriccouriers.bass.models.User;
import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends BaseActivity {

    private Button request;
    private TextView beginLocationText, timeText, endLocationText;
    private ArrayList<RoutePoint> routePoints = new ArrayList<>();

    private Integer selectedStartLocation, selectedEndLocation;
    private User mainUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bass_light_apache));

        mainUser = User.create(PreferenceHelper.read(this, Globals.PrefKeys.MAIN_USER));

        beginLocationText = findViewById(R.id.textview_request_begin_location);
        timeText = findViewById(R.id.textview_request_time);
        endLocationText = findViewById(R.id.textview_request_end_location);

        getRoutePoints();
        initLocations();

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
        Calendar tmp = (Calendar) mcurrentTime.clone();
        tmp.add(Calendar.MINUTE, 15);
        tmp.set(Calendar.AM_PM, 1);

        int hour = tmp.get(Calendar.HOUR_OF_DAY);
        int minute = tmp.get(Calendar.MINUTE);

        findViewById(R.id.layout_request_time).setOnClickListener(v -> {
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(RequestActivity.this, (timePicker, selectedHour, selectedMinute) -> timeText.setText(selectedHour + ":" + selectedMinute), hour, minute, true);
            mTimePicker.setTitle("Selecteer een tijdstip");
            mTimePicker.show();
        });

        timeText.setText(String.valueOf(hour) + ":" + String.valueOf(minute));

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

    private void rideSuccesful(Integer routeID) {
        System.out.println(routeID);
        ProgressDialogHelper.getInstance(this).hide();

        PreferenceHelper.save(this, Globals.PrefKeys.LROUTE_START, selectedStartLocation);
        PreferenceHelper.save(this, Globals.PrefKeys.LROUTE_END, selectedEndLocation);
    }

    private void requestRide() {
        ProgressDialogHelper.getInstance(this).show();
        System.out.println(selectedStartLocation);
        System.out.println(selectedEndLocation);
        API.service().requestRide(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN), mainUser.getUserID(), selectedStartLocation, selectedEndLocation).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                assert response.body() != null;
                if(response.isSuccessful() && response.body().getAsJsonObject().get("success").getAsBoolean()) {
                    rideSuccesful(response.body().getAsJsonObject().get("routeID").getAsInt());
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

package com.electriccouriers.bass.activities;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.helpers.API;
import com.electriccouriers.bass.models.RoutePoint;
import com.electriccouriers.bass.preferences.PreferenceHelper;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;

public class HomeActivity extends BaseActivity {

    private static final String DOT_SOURCE_ID = "ic_shuttle";

    private int count = 0;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Handler handler;
    private Runnable runnable;
    private GeoJsonSource dotGeoJsonSource;
    private ValueAnimator markerIconAnimator;
    private LatLng markerIconCurrentLocation;
    private List<Point> routeCoordinateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, Globals.MAPBOX_API_KEY);

        super.onCreate(savedInstanceState);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            mapboxMap.setStyle(Style.LIGHT);
            mapboxMap.setCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(51.541519, 4.457777))
                    .zoom(14)
                    .build());

            HomeActivity.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(Style.LIGHT, style -> new LoadGeoJson(HomeActivity.this).execute());
        });

        findViewById(R.id.buttonAanvragen).setOnClickListener(v -> {
            onClickAanvragen();
        });

        findViewById(R.id.buttonKaart).setOnClickListener(v -> {
            onClickKaart();
        });

        // TEMP DEBUG CODE
        API.service().routePoints(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN)).enqueue(new Callback<List<RoutePoint>>() {
            @Override
            public void onResponse(Call<List<RoutePoint>> call, Response<List<RoutePoint>> response) {
                if(response.isSuccessful()) {
                    System.out.println(response.body().size());
                    System.out.println(response.body().get(0).getName());
                } else {
                    Log.e("ERROR", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<RoutePoint>> call, Throwable t) {
                Log.e("ERROR", t.getLocalizedMessage());
            }
        });
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.home_toolbar_title;
    }

    @Override
    protected int getToolbarNavigationIcon() {
        return R.drawable.ic_menu;
    }

    @Override
    protected int getOptionsMenu() {
        return 0;
    }

    public void onClickAanvragen(){
        //TODO: onClickAanvragen knop functies toevoegen.
        openAcitivity(new Intent(HomeActivity.this, RequestActivity.class), true);
    }

    private void onClickKaart() {
        //TODO: onClickKaart knop functies toevoegen.
        openAcitivity(new Intent(HomeActivity.this, CardActivity.class), true);
    }

    /**
     * Add data to the map once the GeoJSON has been loaded
     * @param featureCollection returned GeoJSON FeatureCollection from the async task
     */
    private void initData(@NonNull FeatureCollection featureCollection) {
        LineString lineString = (LineString) featureCollection.features().get(0).geometry();
        routeCoordinateList = lineString.coordinates();

        if(mapboxMap != null) {
            Style style = mapboxMap.getStyle();

            if(style != null) {
                initSources(style, featureCollection);
                initSymbolLayer(style);
                initRunnable();
            }
        }
    }

    /**
     * Set up the repeat logic for moving the icon along the route.
     */
    private void initRunnable() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(routeCoordinateList.size() - 1 > count) {
                    Point nextLocation = routeCoordinateList.get(count + 1);

                    if(markerIconAnimator != null && markerIconAnimator.isStarted()) {
                        markerIconCurrentLocation = (LatLng) markerIconAnimator.getAnimatedValue();
                        markerIconAnimator.cancel();
                    }

                    markerIconAnimator = ObjectAnimator.ofObject(latLngEvaluator, count == 0 ? new LatLng(51.541519, 4.457777): markerIconCurrentLocation, new LatLng(nextLocation.latitude(), nextLocation.longitude())).setDuration(1000);
                    markerIconAnimator.setInterpolator(new LinearInterpolator());

                    markerIconAnimator.addUpdateListener(animatorUpdateListener);
                    markerIconAnimator.start();

                    count++;

                    if(routeCoordinateList.size() - 1 == count) {
                        count = 0;
                    }

                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(runnable);
    }

    /**
     * Listener interface for when the ValueAnimator provides an updated value
     */
    private final ValueAnimator.AnimatorUpdateListener animatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LatLng animatedPosition = (LatLng) valueAnimator.getAnimatedValue();
                    if (dotGeoJsonSource != null) {
                        dotGeoJsonSource.setGeoJson(Point.fromLngLat(
                                animatedPosition.getLongitude(), animatedPosition.getLatitude()));
                    }
                }
            };

    /**
     * Add various sources to the map.
     */
    private void initSources(@NonNull Style loadedMapStyle, @NonNull FeatureCollection featureCollection) {
        dotGeoJsonSource = new GeoJsonSource(DOT_SOURCE_ID, featureCollection);
        loadedMapStyle.addSource(dotGeoJsonSource);
    }

    /**
     * Add the marker icon SymbolLayer.
     */
    private void initSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("ic_shuttle", Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_shuttle))));

        loadedMapStyle.addLayer(new SymbolLayer("symbol-layer-id", DOT_SOURCE_ID).withProperties(
                iconImage("ic_shuttle"),
                iconSize(0.5f),
                iconIgnorePlacement(true),
                iconAllowOverlap(true)
        ));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        if(handler != null && runnable != null) {
            handler.post(runnable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();

        if(handler != null && runnable != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if(markerIconAnimator != null) {
            markerIconAnimator.cancel();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private static class LoadGeoJson extends AsyncTask<Void, Void, FeatureCollection> {
        private WeakReference<HomeActivity> weakReference;

        LoadGeoJson(HomeActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... voids) {
            try {
                HomeActivity activity = weakReference.get();
                if (activity != null) {
                    InputStream inputStream = activity.getAssets().open("matched_route.geojson");
                    return FeatureCollection.fromJson(convertStreamToString(inputStream));
                }
            } catch (Exception exception) {
                Log.e("error", exception.toString());
            }
            return null;
        }

        static String convertStreamToString(InputStream is) {
            Scanner scanner = new Scanner(is).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }

        @Override
        protected void onPostExecute(@Nullable FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            HomeActivity activity = weakReference.get();
            if (activity != null && featureCollection != null) {
                activity.initData(featureCollection);
            }
        }
    }

    private static final TypeEvaluator<LatLng> latLngEvaluator = new TypeEvaluator<LatLng>() {
        private final LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    };
}

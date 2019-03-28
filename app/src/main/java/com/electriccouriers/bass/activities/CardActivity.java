package com.electriccouriers.bass.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.electriccouriers.bass.R;
import com.electriccouriers.bass.adapters.CardHistoryListAdapter;
import com.electriccouriers.bass.data.Globals;
import com.electriccouriers.bass.helpers.API;
import com.electriccouriers.bass.helpers.FlipAnimationHelper;
import com.electriccouriers.bass.models.History;
import com.electriccouriers.bass.models.User;
import com.electriccouriers.bass.preferences.PreferenceHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardActivity extends BaseActivity implements RequestListener {

    private ImageView kaartFront, kaartBack;

    public ArrayList<History> dataSet = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bass_light_apache));

        kaartFront = findViewById(R.id.imageKaart);
        kaartBack = findViewById(R.id.imageKaartBack);

        User mainUser = User.create(PreferenceHelper.read(this, Globals.PrefKeys.MAIN_USER));
        API.service().routeHistory(PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN), mainUser.getUserID()).enqueue(new Callback<ArrayList<History>>() {
            @Override
            public void onResponse(Call<ArrayList<History>> call, Response<ArrayList<History>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    dataSet.addAll(response.body());
                    setListView();
                } else {
                    Log.e("ERROR", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<History>> call, Throwable t) {
                Log.e("ERROR", String.valueOf(t.getLocalizedMessage()));
            }
        });

        String cardImageURL = Globals.API_BASEURL + "user/card/" + mainUser.getUserID() + "/" + mainUser.getFullName() + "/" + mainUser.getAccessCode();
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
        GlideUrl glideUrl = new GlideUrl(cardImageURL, new LazyHeaders.Builder()
                        .addHeader("Authorization", PreferenceHelper.read(this, Globals.PrefKeys.UTOKEN)).build());

        Glide.with(this).load(glideUrl).apply(options).listener(this).into(kaartFront);

        kaartFront.setOnClickListener(v -> flipCard());
        kaartBack.setOnClickListener(v -> flipCard());
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


    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        findViewById(R.id.cardProgressBar).setVisibility(View.GONE);
        return false;
    }

    private void setListView() {
        Collections.sort(dataSet, (o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
        CardHistoryListAdapter adapter = new CardHistoryListAdapter(dataSet, this);

        ListView listViewItems = findViewById(R.id.listview_card_history);
        listViewItems.setAdapter(adapter);
    }

    private void flipCard() {
        View rootLayout = findViewById(R.id.card_container);
        FlipAnimationHelper flipAnimationHelper = new FlipAnimationHelper(kaartFront, kaartBack);

        if(kaartFront.getVisibility() == View.GONE)
            flipAnimationHelper.reverse();

        rootLayout.startAnimation(flipAnimationHelper);
    }
}

package com.electriccouriers.bass.helpers;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.electriccouriers.bass.R;

public class BackgroundSoundService extends Service {

    private MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        player = MediaPlayer.create(this, R.raw.ee);
        player.setLooping(true);
        player.setVolume(100, 100);

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return 1;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }
}
// Juan Pablo Anaya
// MDF3 - 201608
// MediaActivity

package com.paix.jpam.anayajuan_ce05.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.paix.jpam.anayajuan_ce05.R;
import com.paix.jpam.anayajuan_ce05.receivers.NotificationReceiver;
import com.paix.jpam.anayajuan_ce05.services.AudioService;

public class MediaActivity extends AppCompatActivity implements ServiceConnection, OnMediaButtonClicked {

    //TAG
    private static final String TAG = "MediaActivity";
    //Media Player Service
    private AudioService mService;
    //SeekBar Flags (Fragment -> Activity. Interface Flags)
    private boolean seekBarWillChange;
    private int newSeekBarValue;

    /*LifeCycle*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        //Start Service (First Step so the service can live without the Activity)
        Intent startAudioService = new Intent(this, AudioService.class);
        startService(startAudioService);
        //Bind to Service (After Service has been started so it can live without the Activity)
        Intent intent = new Intent(this, AudioService.class);
        bindService(intent, this, BIND_AUTO_CREATE);

        //Set Media Player Fragment
        MediaFragment mPlayerFragment = new MediaFragment();
        setMediaPlayerFragment(mPlayerFragment);
        //Initialize SeekBar Flags & Value
        seekBarWillChange = false;
        newSeekBarValue = 0;
        //Register Local Broadcast Receivers -> Next Song, Previous Song & Song has finished playing
        IntentFilter filterNextPreviousFinished = new IntentFilter();
        filterNextPreviousFinished.addAction(AudioService.SONG_HAS_FINISHED_PLAY_NEXT);
        filterNextPreviousFinished.addAction(NotificationReceiver.CHANGE_NEXT);
        filterNextPreviousFinished.addAction(NotificationReceiver.CHANGE_PREVIOUS);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mNextSongReceiver,
                        filterNextPreviousFinished);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister Local Broadcast Receivers -> Next Song
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNextSongReceiver);
        //Unbind Service
        unbindService(this);
        //Service Stop check (don't kill service if it's playing)
        if (!mService.mRunning) {
            mService.stopSelf();
        }
    }

    /*Custom Methods*/
    //Set Media Player Fragment Method
    private void setMediaPlayerFragment(MediaFragment mPlayerFragment) {
        getSupportFragmentManager().beginTransaction().replace
                (R.id.FrameLayout_MediaActivity, mPlayerFragment).commit();
    }

    /*SERVICE*/
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        AudioService.AudioServiceBinder binder =
                (AudioService.AudioServiceBinder) iBinder;
        /*Once the Service is Connected , get the Service instance*/
        mService = binder.getService(); // Todo (If !service should be handled)
        //Broadcast for Current Song info to the Fragment (UPDATE UI)
        if (AudioService.currentSong != null) {
            Intent updateUi = new Intent(AudioService.SONG_INFORMATION);
            updateUi.putExtra("songInfo_key", AudioService.currentSong);
            //Shuffle & Looping Controls Check
            if (mService.mPlayer.isLooping()) {
                updateUi.putExtra("loop_key", true);
            }
            if (AudioService.isShuffling) {
                updateUi.putExtra("shuffle_key", true);
            }
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(updateUi);
        }
        //Dev
        Log.i(TAG, "onServiceConnected: " + "SERVICE CONNECTED");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        //Dev
        Log.i(TAG, "onServiceDisconnected: " + "SERVICE DISCONNECTED");
        //TODO Change UI (PlayList)
    }

    /*UI Fragment - INTERFACE*/
    //Loop
    @Override
    public void onLoopClicked() {
        mService.loop();
    }

    //Next
    @Override
    public void onNextClicked() {
        //Next
        mService.next();
    }

    //Pause
    @Override
    public void onPauseClicked() {
        mService.pause();
    }

    //Play
    @Override
    public void onPlayClicked() {
        mService.play();
    }

    //Previous
    @Override
    public void onPreviousClicked() {
        //Previous
        mService.previous();
    }

    //Shuffle
    @Override
    public void onShuffleClicked() {
        mService.shuffle();
    }

    //Stop
    @Override
    public void onStopClicked() {
        mService.stop();
    }

    /*SEEK_BAR*/
    //Tracking Changes
    @Override
    public void onSeekBarStartedTrackingChanges() {
        //SeekBarFlags
        seekBarWillChange = true;
    }

    //Stop Tracking Changes
    @Override
    public void onSeekBarStoppedTrackingChanges() {
        //SeekBarFlags
        seekBarWillChange = false;
    }

    //Value Changed
    @Override
    public void onSeekBarValueChanged(int value) {
        //Get New SeekBar Value
        newSeekBarValue = value;
        //Change Song Value
        if (seekBarWillChange && mService.mPlayer.isPlaying()) {
            mService.mPlayer.seekTo(newSeekBarValue * 1000);
            //Dev
            //Log.i(TAG, "onSeekBarValueChanged: " + "SONG SECOND: " + mService.mPlayer.getCurrentPosition());
        }
        //Dev
        //Log.i(TAG, "onSeekBarValueChanged: " + newSeekBarValue);
    }

    /*Broadcast Receivers*/
    private final BroadcastReceiver mNextSongReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Notification Broadcasts
            if (intent.getAction().equals(NotificationReceiver.CHANGE_NEXT)) {
                //Dev
                //Log.i(TAG, "onReceive: " + "CHANGE NEXT SONG ON MEDIA ACTIVITY");
                mService.next();
                mService.play();
            } else if (intent.getAction().equals(NotificationReceiver.CHANGE_PREVIOUS)) {
                //Dev
                //Log.i(TAG, "onReceive: " + "CHANGE PREVIOUS SONG ON MEDIA ACTIVITY");
                mService.previous();
                mService.play();
            } else if (intent.getAction().equals(AudioService.SONG_HAS_FINISHED_PLAY_NEXT)) {
                //Dev
                //Log.i(TAG, "onReceive: " + "SONG HAS FINISHED, PLAY NEXT");
                //Play Next Song
                mService.play();
            }
        }
    };


}

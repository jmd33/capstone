package com.capstone.hearingtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wazo on 5/28/13.
 */
public class HeadsetStateReceiver extends BroadcastReceiver {
    private String TAG = "HeadsetStateReceiver";
    private Boolean isPluggedIn = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            Log.d(TAG,"Recieved Headset action");
//            Toast.makeText(context, "AHAHAHA", Toast.LENGTH_LONG).show();
            int state = intent.getIntExtra("state", -1);
            switch (state) {
                case 0:
                    Log.d(TAG, "Headset is unplugged");
                    isPluggedIn = false;
                    if(AudioTrackTest.isRecording != null && AudioTrackTest.isRecording){
                        AudioTrackTest.isRecording = false;
                        AudioTrackTest.mRecordButton.setChecked(false);
                        Toast.makeText(context, context.getResources().getString(R.string.app_name) + " stopped.", Toast.LENGTH_LONG).show();
                     }
                    break;
                case 1:
                    Log.d(TAG, "Headset is plugged");
                    isPluggedIn = true;
                    break;
                default:
                    Log.d(TAG, "I have no idea what the headset state is");
            }
        }

//            Bundle extras = intent.getExtras();
//        if (extras != null) {
//            String state = extras.getString(AudioManager.EXTRA_SCO_AUDIO_STATE);
//            Log.w("MY_DEBUG_TAG", state);
//            if (state.equals(AudioManager.c)) {
//                String phoneNumber = extras
//                        .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                Log.w("MY_DEBUG_TAG", phoneNumber);
//            }
//        }
    }


    public boolean isPluggedIn(){
        return isPluggedIn;
    }
}

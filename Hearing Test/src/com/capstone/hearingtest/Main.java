package com.capstone.hearingtest;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class Main extends Activity{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helloworld);

		PlayFrequency.genTone(3, 1200);
		PlayFrequency.playSound();
	}

    @Override
    protected void onResume() {
        super.onResume();
    }
}

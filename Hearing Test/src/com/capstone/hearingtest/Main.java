package com.capstone.hearingtest;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helloworld);

		PlayFrequency.genTone(3, 1200);
//		PlayFrequency.playSound();
		
//		Plays the sound every time the button is pressed.
		Button b = (Button) findViewById(R.id.BTN_play);
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				PlayFrequency.playSound();
				Log.d("Main", "button pressed");//this shows up in the LogCat. Helpful for debugging.
			}
		});
		
	}

    @Override
    protected void onResume() {
        super.onResume();
    }
}

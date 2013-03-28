package com.capstone.hearingtest;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

public class HearingAidMain extends Activity {
	private static final String LOG_TAG = "AudioRecordTest";
	private static String mFileName = null;
	private Context ctx = this;
	private ToggleButton mRecordButton = null;
	private MediaRecorder mRecorder = null;

	private ToggleButton mPlayButton = null;
	private MediaPlayer mPlayer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
		mRecordButton = (ToggleButton) findViewById(R.id.tbtn_record);
		mPlayButton = (ToggleButton) findViewById(R.id.tbtn_play);

		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.3gp";

		mRecordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(LOG_TAG, mRecordButton.isChecked() + " rec btn");
				 onRecord(mRecordButton.isChecked());
			}
		});
		mPlayButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(LOG_TAG, mRecordButton.isChecked() + " play btn");
//				 onPlay(mPlayButton.isChecked());
				Intent intent = new Intent(ctx, AudioFxDemo.class);

				ctx.startActivity(intent);
			}
		});
		
		
		
		
	}

	private void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	private void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}

		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

}

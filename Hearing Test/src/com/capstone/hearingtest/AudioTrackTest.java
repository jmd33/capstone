package com.capstone.hearingtest;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class AudioTrackTest extends Activity {
	private static final String LOG_TAG = "AudioRecordTest";
	// private static String mFileName = null;
	private Context ctx = this;
	private ToggleButton mRecordButton = null;
	// private AudioRecord mRecorder = null;
	// public static final int FREQUENCY = 44100;
	// public static final int CHANNEL_CONFIGURATION =
	// AudioFormat.CHANNEL_CONFIGURATION_MONO;
	// public static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

	private ToggleButton mPlayButton = null;
	private MediaPlayer mPlayer = null;
	Boolean isRecording = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);
		mRecordButton = (ToggleButton) findViewById(R.id.tbtn_record);
		mPlayButton = (ToggleButton) findViewById(R.id.tbtn_play);

		// mFileName =
		// Environment.getExternalStorageDirectory().getAbsolutePath();
		// mFileName += "/audiorecordtest.3gp";
		mRecordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(LOG_TAG, mRecordButton.isChecked() + " rec btn");
				// onRecord(mRecordButton.isChecked());
				if (isRecording) {
					isRecording = false;
				} else {
					isRecording = true;
					Start();
				}
			}
		});
		mPlayButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(LOG_TAG, mRecordButton.isChecked() + " play btn");
				// onPlay(mPlayButton.isChecked());
				Intent intent = new Intent(ctx, AudioFxDemo.class);

				ctx.startActivity(intent);
			}
		});

	}

	private int freq = 44100;
	private AudioRecord audioRecord = null;
	private Thread Rthread = null;

	private AudioManager audioManager = null;
	private AudioTrack audioTrack = null;
	byte[] buffer = new byte[freq];
	public static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

// The different audio codecs
//int	AAC	AAC Low Complexity (AAC-LC) audio codec
//int	AAC_ELD	Enhanced Low Delay AAC (AAC-ELD) audio codec
//int	AMR_NB	AMR (Narrowband) audio codec
//int	AMR_WB	AMR (Wideband) audio codec
//int	DEFAULT	
//int	HE_AAC	High Efficiency AAC (HE-AAC) audio codec
	int AudioCodec = MediaRecorder.AudioEncoder.AMR_WB;

	protected void Start()

	{

		loopback();

	}

	protected void loopback() {

		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		final int bufferSize = AudioRecord.getMinBufferSize(freq,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		Log.i(LOG_TAG, "BufferSize: "+ bufferSize);

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, freq,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioCodec, bufferSize);
//		ENCODING_PCM_16BIT
		audioTrack = new AudioTrack(AudioManager.ROUTE_HEADSET, freq,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioCodec, bufferSize,
				AudioTrack.MODE_STREAM);

		audioTrack.setPlaybackRate(freq);
		final byte[] buffer = new byte[bufferSize];
		audioRecord.startRecording();
		Log.i(LOG_TAG, "Audio Recording started");
		audioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
//		audioTrack.attachAuxEffect(PresetReverb.PRESET_LARGEHALL);
//		audioTrack.setAuxEffectSendLevel(10);
		audioTrack.play();
		
		Log.i(LOG_TAG, "Audio Playing started");
		Rthread = new Thread(new Runnable() {
			public void run() {
				while (isRecording) {
					try {
						audioRecord.read(buffer, 0, bufferSize);
						audioTrack.write(buffer, 0, buffer.length);

					} catch (Throwable t) {
						Log.e("Error", "Read write failed");
						t.printStackTrace();
					}
				}
			}
		});
		Rthread.start();

	}
}
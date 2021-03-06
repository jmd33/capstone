/*
 * This class records audio from the device's mic to a buffer and uses AudioTrack to play that buffer.
 */

package com.capstone.hearingtest;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Equalizer;
import android.media.audiofx.NoiseSuppressor;
import android.media.audiofx.PresetReverb;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VerticalSeekBar;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class AudioTrackTest extends Activity {
	private static final String LOG_TAG = "AudioRecordTest";
	// private static String mFileName = null;
	private Context ctx = this;
	public static ToggleButton mRecordButton = null;
	private MediaPlayer mPlayer = null;
	public static Boolean isRecording = null;
	private Equalizer mEqualizer;
	private LinearLayout mLinearLayout;

	private int freq = 44100;
	private AudioRecord audioRecord = null;
	private Thread Rthread = null;
	private Thread Wthread = null;

	private AudioManager audioManager = null;
	private AudioTrack audioTrack = null;
	byte[] buffer = new byte[freq];
	public static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	public static final String KEY_STOP = "com.capstone.hearingtest.AudioTrackTest.stopListen";

	// The different audio codecs
	// int AAC AAC Low Complexity (AAC-LC) audio codec
	// int AAC_ELD Enhanced Low Delay AAC (AAC-ELD) audio codec
	// int AMR_NB AMR (Narrowband) audio codec
	// int AMR_WB AMR (Wideband) audio codec
	// int DEFAULT
	// int HE_AAC High Efficiency AAC (HE-AAC) audio codec
	private int AudioCodec = MediaRecorder.AudioEncoder.AMR_WB;
	private ViewSwitcher viewSwitcher;
	private LinearLayout myFirstView;
	private LinearLayout mySecondView;
	public BroadcastReceiver receiver = null;
    private int preset_active = 0;
    IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    private HeadsetStateReceiver headsetReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listen);
        registerHeadset();
        mRecordButton = (ToggleButton) findViewById(R.id.tbtn_record);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (savedInstanceState != null) {
			isRecording = savedInstanceState.getBoolean("isRecording", false);
			if(isRecording)
				StartListening();
		}
		// mLinearLayout = (LinearLayout) findViewById(R.id.ll_EQ);
		if (Build.VERSION.SDK_INT < 10)
			AudioCodec = MediaRecorder.AudioEncoder.AMR_NB;
		// else if(Build.VERSION.SDK_INT >= 16)
		// AudioCodec = MediaRecorder.AudioEncoder.HE_AAC;

		// mFileName =
		// Environment.getExternalStorageDirectory().getAbsolutePath();
		// mFileName += "/audiorecordtest.3gp";

		// = create();
		IntentFilter filter = new IntentFilter();
		filter.addAction(KEY_STOP);
		// Add other actions as needed

		receiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		        if (intent.getAction().equals(KEY_STOP)) {
                    ImageView iv = (ImageView) findViewById(R.id.notification_btn_pause);
                    TextView tv = (TextView) findViewById(R.id.notification_main_txt);
//                    Log.d("AudioTrackTest", tv.toString());
                    if(isRecording != null && isRecording){
                        stopListen();
//                        iv.setImageResource(R.drawable.ic_power_blue);
//                        tv.setText(getResources().getString(R.string.notification_pause_text));

                    }else{
                        StartListening();
//                        iv.setImageResource(R.drawable.ic_power_red);
//                        tv.setText(getResources().getString(R.string.notification_start_text));

                    }
		        }
		        // else if (...) {
//		        }
		    }
		};

		registerReceiver(receiver, filter);
		
		
		

		mRecordButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(LOG_TAG, mRecordButton.isChecked() + " rec btn");
				// onRecord(mRecordButton.isChecked());
                if (isRecording == null) {
					StartListening();
				} else if (isRecording) {
//					isRecording = false;
					stopListen();
					// Rthread.interrupt();
					// audioTrack.stop();
				} else {
                    StartListening();
                    // Rthread.notify();
					// Rthread.start();// = null;
					// audioTrack.play();
				}
			}
		});

		final ImageView btn_preset_1 = (ImageView) findViewById(R.id.btn_preset_1);
		final ImageView btn_preset_2 = (ImageView) findViewById(R.id.btn_preset_2);
		final ImageView btn_preset_3 = (ImageView) findViewById(R.id.btn_preset_3);
		btn_preset_1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
                if (preset_active != 1) {
                    applyPreset(1);
                } else {
                    applyPreset(0);
                }
			}
		});
		btn_preset_2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
                if (preset_active != 2) {
                    applyPreset(2);
                } else {
                    applyPreset(0);
                }
			}
		});
        btn_preset_3.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (preset_active != 3) {
                    applyPreset(3);
                } else {
                    applyPreset(0);
                }
            }
        });
		viewSwitcher = (ViewSwitcher) findViewById(R.id.vs_presets_eq);
		myFirstView = (LinearLayout) findViewById(R.id.view_presets);
		mySecondView = (LinearLayout) findViewById(R.id.view_eq);
		final Button btn_eq = (Button) findViewById(R.id.btn_eq);
		btn_eq.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.d("eq_btn", "view: " + v);
				if (viewSwitcher.getCurrentView() != myFirstView) {
					viewSwitcher.showPrevious();
				} else if (viewSwitcher.getCurrentView() != mySecondView) {
					viewSwitcher.showNext();
//					syncEqBars();
				}
			}
		});

	}
    private void registerHeadset(){
        if(headsetReceiver != null)
            unregisterReceiver(headsetReceiver);
        headsetReceiver = null;
        headsetReceiver = new HeadsetStateReceiver();
        registerReceiver(headsetReceiver, headsetFilter);
    }

	private void syncEqBars() {
		short bands = mEqualizer.getNumberOfBands();
		final short minEQLevel = mEqualizer.getBandLevelRange()[0];
		final short maxEQLevel = mEqualizer.getBandLevelRange()[1];
		Log.i(LOG_TAG, "minEQLevel: " + minEQLevel);
		Log.i(LOG_TAG, "maxEQLevel: " + maxEQLevel);

		for (short i = 0; i < bands && i < 5; i++) {
			final short band = i;
			Log.d("AFX", "band: " + band + "");

			// Log.i(LOG_TAG, "band: "+band+" "+mEqualizer.);
			SeekBar bar = null;
			switch (i) {
			case 0:
				bar = (SeekBar) findViewById(R.id.sb_1);
				break;
			case 1:
				bar = (SeekBar) findViewById(R.id.sb_2);
				break;
			case 2:
				bar = (SeekBar) findViewById(R.id.sb_3);
				break;
			case 3:
				bar = (SeekBar) findViewById(R.id.sb_4);
				break;
			case 4:
				bar = (SeekBar) findViewById(R.id.sb_5);
				break;
			}
			Log.d("EQ",
					"band level; " + band + " = "
							+ mEqualizer.getBandLevel(band));
			int level = mEqualizer.getBandLevel(band);
			level += 1500;
			bar.setProgress(level);
			Log.d("EQ", "bar getProgress() = " + bar.getProgress());
			Log.d("EQ", "band = level: " + band + " = " + level);

		}

	}

	private void resetEQ() {
		short bands = mEqualizer.getNumberOfBands();
		final short maxEQLevel = mEqualizer.getBandLevelRange()[1];
		for (short i = 0; i < bands; i++) {
			final short band = i;
			Log.d("AFX", "band: " + band + "");
			mEqualizer.setBandLevel(band, (short) (maxEQLevel * (0.50)));
		}
	}

	private void applyPreset(int preset_name) {
        if (isRecording == null){
            Toast.makeText(ctx, "click listen first",
                    Toast.LENGTH_SHORT).show();
        }else{
            int[] x = null;
            switch (preset_name) {
            case 0:
                x = getResources().getIntArray(R.array.flat);
                break;
            case 1:
                x = getResources().getIntArray(R.array.tv);
                break;
            case 2:
                x = getResources().getIntArray(R.array.conversation);
                break;
            case 3:
                x = getResources().getIntArray(R.array.crowded);
                break;
            }
            Log.d("applyPreset", "x length = "+ x.length);
            for (int i = 0; i < x.length; i++){
                short band = (short) i;
                mEqualizer.setBandLevel( band, (short) x[i]);
                Log.i("ApplyPreset","band: "+band+" level: "+ mEqualizer.getBandLevel(band));
            }
            syncEqBars();
            presetActiveStatus(preset_name);
        }
    }
    private void presetActiveStatus(int preset){
        preset_active = preset;
        ImageView iv;

        switch(preset){

            case(0):
                iv = (ImageView) findViewById(R.id.iv_preset_active_1);
                iv.setVisibility(View.INVISIBLE);
                iv = (ImageView) findViewById(R.id.iv_preset_active_2);
                iv.setVisibility(View.INVISIBLE);
                iv = (ImageView) findViewById(R.id.iv_preset_active_3);
                iv.setVisibility(View.INVISIBLE);
                break;
            case(1):
                iv = (ImageView) findViewById(R.id.iv_preset_active_1);
                iv.setVisibility(View.VISIBLE);
                break;
            case(2):
                iv = (ImageView) findViewById(R.id.iv_preset_active_2);
                iv.setVisibility(View.VISIBLE);
                break;
            case(3):
                iv = (ImageView) findViewById(R.id.iv_preset_active_3);
                iv.setVisibility(View.VISIBLE);
                break;

        }
    }

	protected void StartListening() {
        isRecording = false;
        if(headsetReceiver.isPluggedIn()){
            isRecording = true;
            if(audioRecord == null)
		        loopback();
        }else{
            Toast.makeText(ctx, "Please connect headphones first.", Toast.LENGTH_LONG).show();
            mRecordButton.setChecked(false);
        }
	}

	public void stopListen(){
		if(Rthread != null)
            Rthread.interrupt();
		Rthread = null;
		if (audioTrack != null)
			audioTrack.release();
		if (audioRecord != null)
			audioRecord.release();
        audioTrack = null;
        audioRecord = null;
		isRecording = null;
		mRecordButton.setChecked(false);
		
	}
	protected void loopback() {
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		final int bufferSize = AudioRecord.getMinBufferSize(freq,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		Log.i(LOG_TAG, "BufferSize: " + bufferSize);

		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, freq,
				AudioFormat.CHANNEL_IN_MONO, AudioCodec, bufferSize);
		// ENCODING_PCM_16BIT
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, freq,
				AudioFormat.CHANNEL_OUT_MONO, AudioCodec, bufferSize,
				AudioTrack.MODE_STREAM);

		audioTrack.setPlaybackRate(freq);
		final byte[] buffer = new byte[bufferSize];
		setupEqualizer();
		resetEQ();
		audioRecord.startRecording();
		Log.i(LOG_TAG, "Audio Recording started");
		audioTrack.setStereoVolume(AudioTrack.getMaxVolume(),
				AudioTrack.getMaxVolume());
		// audioTrack.attachAuxEffect(PresetReverb.PRESET_LARGEHALL);
		// audioTrack.setAuxEffectSendLevel(10);
		audioTrack.play();

		Log.i(LOG_TAG, "Audio Playing started");
		Rthread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.interrupted()) {
					if (isRecording != null && isRecording) {
						try {
							audioRecord.read(buffer, 0, bufferSize);
							// Log.i(LOG_TAG, "READ");
							audioTrack.write(buffer, 0, buffer.length);
							// Log.i(LOG_TAG, "       WRITE");

						} catch (Throwable t) {
							Log.e("Error", "Read write failed");
							t.printStackTrace();
						}
					}
				}
			}
		});
		// might need to read and write on different threads to get better
		// performance
		/*
		 * Wthread = new Thread(new Runnable() { public void run() { while
		 * (isRecording) { try { // audioRecord.read(buffer, 0, bufferSize);
		 * audioTrack.write(buffer, 0, buffer.length); Log.i(LOG_TAG,
		 * "       WRITE");
		 * 
		 * } catch (Throwable t) { Log.e("Error", "Read write failed");
		 * t.printStackTrace(); } } } });
		 */
		Rthread.start();
		// Wthread.start();

	}

	private void setupEqualizer() {
		// Create the Equalizer object (an AudioEffect subclass) and attach it
		// to our media player,
		// with a default priority (0).
		mEqualizer = new Equalizer(0, audioTrack.getAudioSessionId());
		mEqualizer.setEnabled(true);

		short bands = mEqualizer.getNumberOfBands();
//		Log.d("AFX", "BAnds" + bands + "");
		final short minEQLevel = mEqualizer.getBandLevelRange()[0];
		final short maxEQLevel = mEqualizer.getBandLevelRange()[1];
		Log.i(LOG_TAG, "minEQLevel: " + minEQLevel);
		Log.i(LOG_TAG, "maxEQLevel: " + maxEQLevel);

		for (short i = 0; i < bands && i < 5; i++) {
			final short band = i;
//			Log.d("AFX", "band: " + band + "");

			// Log.i(LOG_TAG, "band: "+band+" "+mEqualizer.);
			SeekBar bar = null;
			switch (i) {
			case 0:
				bar = (SeekBar) findViewById(R.id.sb_1);
				break;
			case 1:
				bar = (SeekBar) findViewById(R.id.sb_2);
				break;
			case 2:
				bar = (SeekBar) findViewById(R.id.sb_3);
				break;
			case 3:
				bar = (SeekBar) findViewById(R.id.sb_4);
				break;
			case 4:
				bar = (SeekBar) findViewById(R.id.sb_5);
				break;
			}

			// SeekBar bar = new SeekBar(this);
			bar.setMax(maxEQLevel - minEQLevel);
			bar.setProgress(mEqualizer.getBandLevel(band));
			bar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					mEqualizer.setBandLevel(band,
							(short) (progress + minEQLevel));
//					Log.d("EQ", "onchange. band: " + band + "  prog:"
//							+ progress);
                    presetActiveStatus(0);
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
//					Log.d("EQ",
//							"onstart. band: " + band + "  prog:"
//									+ seekBar.getProgress());
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
//					Log.d("EQ",
//							"onstop. band: " + band + "  prog:"
//									+ seekBar.getProgress());
				}
			});
		}
		applyPreset(0);
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("AudioTrackTest", "onPause() called");
		if(isRecording != null){
			if(isRecording)
				showNotification();
            else{
                isRecording = null;
            }
		}else{
            try{
			    unregisterReceiver(receiver);
                unregisterReceiver(headsetReceiver);
            }catch (Exception e){
                Log.e("AudioTrackTest", e.toString());
            }
		}

    }
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("AudioTrackTest", "onStop() called");
//		unregisterReceiver(receiver);

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (audioTrack != null)
			audioTrack.release();
		if (audioRecord != null)
			audioRecord.release();
        try{
            unregisterReceiver(receiver);
            unregisterReceiver(headsetReceiver);
        }catch (Exception e){
            Log.e("AudioTrackTest", e.toString());
        }

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("AudioTrackTest", "onResume() called");
		if(isRecording != null )
			if(isRecording && audioRecord == null)
				StartListening();
        try{
            registerHeadset();
        }catch(Exception e){
            Log.e("AudioTrackTest", "onResume error: "+ e.toString() );
        }
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
//		if(isRecording != null)
//			savedInstanceState.putBoolean("isRecording", isRecording);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// generate menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.hearing_aid_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle menu item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.help:
			intent = new Intent(ctx, About.class);
			ctx.startActivity(intent);
			return true;
		case R.id.home:
			intent = new Intent(ctx, Main.class);
			ctx.startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showNotification(){
//        Intent clickIntent = new Intent(Intent.ACTION_VIEW, );
//		PendingIntent myPauseIntent =  PendingIntent.getActivity(this, 0, clickIntent, 0);
		Intent nextIntent = new Intent(KEY_STOP);
		PendingIntent PendingPauseIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);
		RemoteViews rv = new RemoteViews(getPackageName(), R.layout.custom_notification);
        rv.setOnClickPendingIntent(R.id.notification_btn_pause, PendingPauseIntent);

		//		rv.setTextViewText(R.id.notification_title, "Hear Rite");
//		rv.setTextViewText(R.id.notification_text, "click to stop listening");
//		rv.setImageViewResource(R.id.notifiation_image, R.drawable.ic_launcher);
//		rv.
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
//		        .setContentTitle("Hear Rite")
		.setSmallIcon(R.drawable.ic_launcher)
		.setContent(rv);
//		        .setContentText("click to stop listening");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, AudioTrackTest.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Main.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(0, mBuilder.build());

		
		
		
		
	}
	
	/*
	 * creates EQ sliders on screen and attaches EQ to the AudioTrack
	 */
	private void setupEqualizerFxAndUI() {
		// Create the Equalizer object (an AudioEffect subclass) and attach it
		// to our media player,
		// with a default priority (0).
		mEqualizer = new Equalizer(0, audioTrack.getAudioSessionId());
		mEqualizer.setEnabled(true);
		// mLinearLayout.removeAllViews();
		TextView eqTextView = new TextView(this);
		// eqTextView.setText("Equalizer:");
		// mLinearLayout.addView(eqTextView);

		short bands = mEqualizer.getNumberOfBands();
		Log.d("AFX", "BAnds" + bands + "");
		final short minEQLevel = mEqualizer.getBandLevelRange()[0];
		final short maxEQLevel = mEqualizer.getBandLevelRange()[1];
		Log.i(LOG_TAG, "minEQLevel: " + minEQLevel);
		Log.i(LOG_TAG, "maxEQLevel: " + maxEQLevel);

		for (short i = 0; i < bands; i++) {
			final short band = i;
			Log.d("AFX", "band: " + band + "");

			TextView freqTextView = new TextView(this);
			freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			freqTextView.setText((mEqualizer.getCenterFreq(band) / 1000)
					+ " Hz");
			// mLinearLayout.addView(freqTextView);
			// Log.i(LOG_TAG, "band: "+band+" "+mEqualizer.);

			LinearLayout row = new LinearLayout(this);
			row.setOrientation(LinearLayout.HORIZONTAL);

			TextView minDbTextView = new TextView(this);
			minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			minDbTextView.setText((minEQLevel / 100) + " dB");

			TextView maxDbTextView = new TextView(this);
			maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			maxDbTextView.setText((maxEQLevel / 100) + " dB");

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.weight = 1;
			SeekBar bar = new SeekBar(this);
			bar.setLayoutParams(layoutParams);
			bar.setMax(maxEQLevel - minEQLevel);
			bar.setProgress(mEqualizer.getBandLevel(band));

			bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					mEqualizer.setBandLevel(band,
							(short) (progress + minEQLevel));
					// Log.d("EQ", "band: "+band+"  prog:"+progress);

				}

				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
					Log.d("EQ",
							"band: " + band + "  prog:" + seekBar.getProgress());

				}
			});

			// row.addView(minDbTextView);
			// row.addView(bar);
			// row.addView(maxDbTextView);

			// mLinearLayout.addView(row);
		}
	}

}

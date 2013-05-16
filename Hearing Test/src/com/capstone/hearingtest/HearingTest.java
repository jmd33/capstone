package com.capstone.hearingtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class HearingTest extends Activity {
	private static final String KEY_121 = "http://webhost.ischool.uw.edu/~jcz530/capstone/android/create-user.php";
	private SharedPreferences user_info;
	private SeekBar seekbar;
	private double num;
	private TextView value;
	private int[] freqs;
	private int pointer = 0;
	private Context ctx = this;
	private PlayFrequency pf = null;
	private int max_volume = 0;
	private boolean sound_playing = false;
	private boolean is_left_ear = true;
	private static final float VISUALIZER_HEIGHT_DIP = 50f;
	private MediaPlayer mMediaPlayer;
	private Visualizer mVisualizer;
	private Equalizer mEqualizer;
	private LinearLayout mLinearLayout;
	private VisualizerView mVisualizerView;
	private TextView mStatusTextView;
	private TextView mFrequencyTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hearing_test);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (savedInstanceState != null) {
			pointer = savedInstanceState.getInt("pointer");
			is_left_ear = savedInstanceState.getBoolean("is_left_ear");
		}
		freqs = getResources().getIntArray(R.array.frequencies);
		value = (TextView) findViewById(R.id.textview);
		mFrequencyTextView = (TextView) findViewById(R.id.frequency);
		mFrequencyTextView.setText("Current Frequency: " + freqs[pointer] + "hz");
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setProgress(75);
		num = 0;
		num = .07;
		final ProgressBar test_progress = (ProgressBar) findViewById(R.id.PB_test_progress);
		// final TextView tv_freq = (TextView) findViewById(R.id.tv_freq);
		// tv_freq.setText(freqs[pointer] + "");
		user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		pf = new PlayFrequency(6);
		setupVisualizerLayout();
		// setupVisualizer();
		// Plays the sound every time the button is pressed.
		Button b = (Button) findViewById(R.id.BTN_play);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.i("Main", "frequency: " + freqs[pointer]);
				// pf.playSound((float) num, (float) num);
				// if(sound_playing){
				// sound_playing = false;
				// pf.stop();
				// }else{
				// sound_playing = true;
				pf.genTone(6, freqs[pointer]);
				if (is_left_ear)
					pf.playSound(PlayFrequency.LEFT_EAR_ONLY);
				else
					pf.playSound(PlayFrequency.RIGHT_EAR_ONLY);
				// pf.start();
				// }
				// PlayFrequency.playSound((float)num, (float)num,
			}
		});
		Button btn_submit = (Button) findViewById(R.id.BTN_submit);
		btn_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				int num_int = (int) (num * 100);
				Log.d("HearingTest", "num = " + num + "  num_int = " + num_int);
				// TODO: Decide if we are still wanting to push results to the
				// db
				// new PushToDB().execute("testresult",
				// user_info.getString("account", "error"), freqs[pointer]
				// + "", num_int + "");// this pushed test result
				// to the db.

				try {
					FileOutputStream fos = ctx.openFileOutput("temp_data.txt",
							Context.MODE_APPEND);
					Writer out = new OutputStreamWriter(fos);
					// out.write(freqs[pointer] + "\n");
					out.write(seekbar.getProgress() + "\n");
					out.close();
					Log.i("HearingTest",
							"wrote to file: " + seekbar.getProgress() + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (is_left_ear) {
					test_progress.setProgress(test_progress.getProgress() + (6));
					is_left_ear = false;
				} else if (pointer < freqs.length - 1 && !is_left_ear) {
					pointer++;
					mFrequencyTextView.setText("Current Frequency: " + freqs[pointer] + "hz");
					test_progress.setProgress(test_progress.getProgress() + (6));
					is_left_ear = true;
				} else if (pointer == freqs.length - 1) {
					File file = getApplicationContext().getFileStreamPath(
							"account_data.txt");
					File temp = getApplicationContext().getFileStreamPath(
							"temp_data.txt");
					temp.renameTo(file);
					Intent intent = new Intent(ctx, AudioGram.class);
					ctx.startActivity(intent);
				}
				// tv_freq.setText(freqs[pointer] + "");
				Log.d("Main", "submit btn pressed");
			}
		});
		// this is an example of how to push a test result to our db.
		// new PushToDB().execute("testresult","1", "880", "4");//this pushed
		// test result to the db.
		final AudioManager am = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		max_volume = am.getStreamMaxVolume(am.STREAM_MUSIC);
		am.setStreamVolume(am.STREAM_MUSIC, 1, am.FLAG_VIBRATE);
		am.setStreamVolume(am.STREAM_MUSIC, max_volume / 4, am.FLAG_VIBRATE);
		seekbar.setProgress(25);
		Log.d("HearingTest",
				"max stream: " + am.getStreamMaxVolume(am.STREAM_MUSIC));
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int start_level = 0;

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (start_level != seekbar.getProgress() / (100 / max_volume)) {
					am.setStreamVolume(am.STREAM_MUSIC, seekbar.getProgress()
							/ (100 / max_volume), am.FLAG_VIBRATE);
					Log.d("HearingTest", "start_level = " + start_level
							+ "  vol set = " + seekbar.getProgress()
							/ (100 / max_volume));
					start_level = seekbar.getProgress() / (100 / max_volume);
				}
				num = (float) progress / 1000;
				value.setText("Volume is at " + progress + "%");
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				start_level = seekbar.getProgress() / (100 / max_volume);
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// generate menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.hearing_test_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle menu item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.help:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AudioManager am = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		seekbar.setProgress(100 * (am.getStreamVolume(am.STREAM_MUSIC))
				/ max_volume);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		pf.audioTrack.release();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("pointer", pointer);
		savedInstanceState.putBoolean("is_left_ear", is_left_ear);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			if (action == KeyEvent.ACTION_UP) {
				seekbar.setProgress(seekbar.getProgress() + (100 / max_volume));
			}
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			if (action == KeyEvent.ACTION_DOWN) {
				seekbar.setProgress(seekbar.getProgress() - (100 / max_volume));
			}
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}

	private void setupVisualizerLayout() {
		// Create a VisualizerView (defined below), which will render the
		// simplified audio
		// wave form to a Canvas.
		if (Build.VERSION.SDK_INT > 8) {

			mLinearLayout = (LinearLayout) findViewById(R.id.LL_vis);
			mVisualizerView = new VisualizerView(this);
			mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					(int) (VISUALIZER_HEIGHT_DIP * getResources()
							.getDisplayMetrics().density)));
			mLinearLayout.addView(mVisualizerView);

			// }
			//
			// private void setupVisualizer() {
			mLinearLayout.removeAllViews();
			mLinearLayout.addView(mVisualizerView);
			// Create the Visualizer object and attach it to our media player.
			mVisualizer = new Visualizer(pf.audioTrack.getAudioSessionId());
			mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
			mVisualizer.setDataCaptureListener(
					new Visualizer.OnDataCaptureListener() {
						public void onWaveFormDataCapture(
								Visualizer visualizer, byte[] bytes,
								int samplingRate) {
							mVisualizerView.updateVisualizer(bytes);
						}

						public void onFftDataCapture(Visualizer visualizer,
								byte[] bytes, int samplingRate) {
						}
					}, Visualizer.getMaxCaptureRate() / 2, true, false);
			mVisualizer.setEnabled(true);

		}
	}

	class VisualizerView extends View {
		private byte[] mBytes;
		private float[] mPoints;
		private Rect mRect = new Rect();

		private Paint mForePaint = new Paint();

		public VisualizerView(Context context) {
			super(context);
			init();
		}

		private void init() {
			mBytes = null;

			mForePaint.setStrokeWidth(4f);
			mForePaint.setAntiAlias(true);
			mForePaint.setColor(Color.parseColor("#33b5e5"));
		}

		public void updateVisualizer(byte[] bytes) {
			mBytes = bytes;
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if (mBytes == null) {
				return;
			}

			if (mPoints == null || mPoints.length < mBytes.length * 4) {
				mPoints = new float[mBytes.length * 4];
			}

			mRect.set(0, 0, getWidth(), getHeight());

			for (int i = 0; i < mBytes.length - 1; i++) {
				mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);

				mPoints[i * 4 + 1] = mRect.height() / 2
						+ ((byte) (mBytes[i] + 128)) * (mRect.height() / 2)
						/ 128;

				mPoints[i * 4 + 2] = mRect.width() * (i + 1)
						/ (mBytes.length - 1);

				mPoints[i * 4 + 3] = mRect.height() / 2
						+ ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2)
						/ 128;

			}
			canvas.drawLines(mPoints, mForePaint);
		}
	}
}

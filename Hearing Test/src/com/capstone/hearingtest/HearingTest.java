package com.capstone.hearingtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class HearingTest extends Activity {
	private static final String KEY_121 = "http://webhost.ischool.uw.edu/~jcz530/capstone/android/create-user.php";
	private SharedPreferences user_info;
	private SeekBar seekbar;
	private double num;
	private TextView value;
	private int[] freqs;
	private int pointer = 0;
	private Context ctx = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hearing_test);
		if (savedInstanceState != null) {
			pointer = savedInstanceState.getInt("pointer");
		}
		freqs = getResources().getIntArray(R.array.frequencies);
		value = (TextView) findViewById(R.id.textview);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setProgress(75);
		num = 0;
		num = .07;
		// final TextView tv_freq = (TextView) findViewById(R.id.tv_freq);
		// tv_freq.setText(freqs[pointer] + "");
		user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		// PlayFrequency.genTone(3, 1200);
		// PlayFrequency.playSound();
		// int x = getResources().

		// Plays the sound every time the button is pressed.
		Button b = (Button) findViewById(R.id.BTN_play);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.d("Main", "button pressed");// this shows up in the LogCat.
												// Helpful for debugging.
				Log.i("Main", "frequency: " + freqs[pointer]);
				PlayFrequency.genTone(3, freqs[pointer]);
				PlayFrequency.playSound((float) num, (float) num);
				// PlayFrequency.playSound((float)num, (float)num,
				// PlayFrequency.LEFT_EAR_ONLY);

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
				if (pointer < freqs.length - 1) {
					pointer++;
				} else if (pointer == freqs.length - 1) {
					Intent intent = new Intent(ctx, HearingAidMain.class);
					ctx.startActivity(intent);
				}
				// tv_freq.setText(freqs[pointer] + "");
				Log.d("Main", "submit btn pressed");// this shows up in the
													// LogCat. Helpful for
													// debugging.
			}
		});
		// this is an example of how to push a test result to our db.
		// new PushToDB().execute("testresult","1", "880", "4");//this pushed
		// test result to the db.

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				num = (float) progress / 1000;
				value.setText("Volume is at" + progress + "%");
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
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
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt("pointer", pointer);
		super.onSaveInstanceState(savedInstanceState);
	}

}

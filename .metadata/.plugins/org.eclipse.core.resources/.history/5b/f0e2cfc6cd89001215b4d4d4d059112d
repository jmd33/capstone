package com.capstone.hearingtest;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class HearingTest extends Activity{
	  private static final String KEY_121 = "http://webhost.ischool.uw.edu/~jcz530/capstone/android/create-user.php";
	  private SharedPreferences user_info;

	  SeekBar seekbar;
	  double num;
	  TextView value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helloworld);

		value = (TextView) findViewById(R.id.textview);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		num = 0;
//		PlayFrequency.genTone(3, 1200);
//		PlayFrequency.playSound();
		
//		Plays the sound every time the button is pressed.
		Button b = (Button) findViewById(R.id.BTN_play);
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				Log.d("Main", "button pressed");//this shows up in the LogCat. Helpful for debugging.
				int input = 0;
				EditText et = (EditText) findViewById(R.id.ET_frequency);
				String s = et.getText().toString();
				input = Integer.parseInt(s);//retrieve the input and parse as INT
				Log.d("Main", "freq"+input);//this shows up in the LogCat. Helpful for debugging.

				PlayFrequency.genTone(3, input);
				PlayFrequency.playSound((float)num, (float)num);
			}
		});
//		this is an example of how to push a test result to our db. 
//		new PushToDB().execute("testresult","1", "880", "4");//this pushed test result to the db.
		
		seekbar.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
		{
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				num = (float)progress / 1000;
				value.setText("Volume is at" + progress + "%");
			}

		    public void onStartTrackingTouch(SeekBar seekBar)
		    {
		    // TODO Auto-generated method stub
		    }

		    public void onStopTrackingTouch(SeekBar seekBar)
		    {
		    // TODO Auto-generated method stub
		    }
		});
		
	}

    @Override
    protected void onResume() {
        super.onResume();
    }
}

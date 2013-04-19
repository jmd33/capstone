package com.capstone.hearingtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity{
    private static final String KEY_121 = "http://webhost.ischool.uw.edu/~jcz530/capstone/android/create-user.php";
    private SharedPreferences user_info;
    Context ctx = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	
		Button b = (Button) findViewById(R.id.btn_getstarted);
		b.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent intent = new Intent(ctx, CreateAccount.class);
//				Intent intent = new Intent(ctx, HearingAidMain.class);
//				Intent intent = new Intent(ctx, AudioTrackTest.class);
//				Intent intent = new Intent(ctx, AudioGram.class);

				
				ctx.startActivity(intent);
			}
		});
	}

	@Override
    protected void onResume() {
        super.onResume();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// generate menu
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle menu item selection
		Intent intent;
	    switch (item.getItemId()) {
	    	case R.id.audiogram:
				intent = new Intent(ctx, AudioGram.class);
				ctx.startActivity(intent);
	            return true;
	        case R.id.help:
				intent = new Intent(ctx, AudioTrackTest.class);
				ctx.startActivity(intent);
	        	return true;
	        case R.id.share:
	        	share();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void share(){
		// pop up selector for how to share
		Intent i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
		startActivity(Intent.createChooser(i, getString(R.string.share_chooser_text)));
	}
      
}

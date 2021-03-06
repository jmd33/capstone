package com.capstone.hearingtest;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class Main extends Activity{
    private static final String KEY_121 = "http://webhost.ischool.uw.edu/~jcz530/capstone/android/create-user.php";
    private SharedPreferences user_info;
    Context ctx = this;
    int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File dir = getFilesDir();
		File temp = new File(dir, "temp_data.txt");
		boolean deleted = temp.delete();
//		ActionBar ab = getActionBar();
		// If opening app for the first time, create the account file.
		File file = getApplicationContext().getFileStreamPath("account_data.txt");
		if ( ! file.exists()) {
			saveFile(getApplicationContext());
		} 
		
		try {
	        FileInputStream fis = getApplicationContext().openFileInput("account_data" + ".txt");
	        BufferedReader r = new BufferedReader(new InputStreamReader(fis));
	        while (r.readLine() != null) {
	        	count++;
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.i("TESTE", "FILE - false");
	    }
		
		// If count is not 11 then retake hearing test, otherwise show main menu
        if (count < 18) {
        	setContentView(R.layout.main_no_account);
    		Button b = (Button) findViewById(R.id.btn_getstarted);
    		b.setOnClickListener(new OnClickListener(){
    			public void onClick(View arg0) {
    				// Wipe / create new file so old data gets thrown out
    				saveFile(getApplicationContext());
    				Intent intent = new Intent(ctx, CreateAccount.class);
    				ctx.startActivity(intent);
    			}
    		});
        } else {
        	setContentView(R.layout.main_with_account);
    		Button hearing_aid = (Button) findViewById(R.id.btn_hearing_aid);
    		Button retake_test = (Button) findViewById(R.id.btn_retake_test);
    		hearing_aid.setOnClickListener(new OnClickListener(){
    			public void onClick(View arg0) {
    				Intent intent = new Intent(ctx, AudioTrackTest.class);
    				ctx.startActivity(intent);
    			}
    		});
    		retake_test.setOnClickListener(new OnClickListener(){
    			public void onClick(View arg0) {
					Intent intent = new Intent(ctx, CreateAccount.class);
    				ctx.startActivity(intent);
    			}
    		});
        }
		
//		Intent intent = new Intent(ctx, HearingAidMain.class);
//		Intent intent = new Intent(ctx, AudioTrackTest.class);
//		Intent intent = new Intent(ctx, AudioGram.class);
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
//				intent = new Intent(ctx, About.class);

				ctx.startActivity(intent);
	            return true;
	        case R.id.help:
				 intent = new Intent(ctx, Help.class);
				ctx.startActivity(intent);
	        	return true;
	        case R.id.about:
				 intent = new Intent(ctx, About.class);
				ctx.startActivity(intent);
	        	return true;
	        case R.id.share:
	        	share();
	        	return true;
//	        case R.id.exit:
//	        	intent = new Intent(Intent.ACTION_MAIN);
//	        	intent.addCategory(Intent.CATEGORY_HOME);
//	        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        	startActivity(intent);
//	        	return true;
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
	
	public boolean saveFile(Context context){
	    Log.i("TESTE", "SAVE");
	    try {
	        FileOutputStream fos = context.openFileOutput("account_data"+".txt",Context.MODE_PRIVATE);
	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
      
}
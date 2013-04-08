package com.capstone.hearingtest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

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
//				Intent intent = new Intent(ctx, CreateAccount.class);
//				Intent intent = new Intent(ctx, HearingAidMain.class);
				Intent intent = new Intent(ctx, AudioTrackTest.class);
//				Intent intent = new Intent(ctx, AudioGram.class);

				
				ctx.startActivity(intent);
			}
		});
	}

	@Override
    protected void onResume() {
        super.onResume();
    }
    
      
}

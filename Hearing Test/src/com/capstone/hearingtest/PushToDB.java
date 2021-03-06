package com.capstone.hearingtest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This class pushes data to our database asynchronously. It can be expanded to
 * push other data if need be. It can take any number of parameters. The
 * following parameters are what's needed to push a test result.
 * 
 * @param 0 (needs to be 'testresult')
 * @param user_id
 * @param frequency
 * @param volume
 * 
 *            * @author Joe
 */
public class PushToDB extends AsyncTask<String, Integer, Long> {
	private static final String KEY_161 = "http://webhost.ischool.uw.edu/~jcz530/capstone/android/add-test-result.php";
	private static final String KEY_171 = "http://webhost.ischool.uw.edu/~jcz530/capstone/android/create-user.php";

	// private SharedPreferences user_info;

	@Override
	protected Long doInBackground(String... params) {

		if (params[0].equalsIgnoreCase("testresult")) {
			String email = params[1];
			String frequency = params[2];
			String volume = params[3];

			addResult(email, frequency, volume);
		} else if (params[0].equalsIgnoreCase("adduser")) {
			String email = params[1];
			String gender = params[2];
			String age = params[3];

			addUser(email, gender, age);
		}
		return null;
	}
/**
 * push new user to db
 * @param email
 * @param gender
 * @param age
 */
	private void addUser(String email, String gender, String age) {
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(KEY_171);
		if(gender.equalsIgnoreCase("male"))// convert gender to int for db.
			gender = "1";
		else
			gender = "0";
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("gender", gender));
			nameValuePairs.add(new BasicNameValuePair("age", age));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			// HttpEntity entity = response.getEntity();
			// is = entity.getContent();
			Log.d("PushToDB", "Pushed new user");
			
		} catch (Exception e) {
			Log.e("PushToDB", "addUser. Error in http connection " + e.toString());

		}

	}

	// public static SharedPreferences getSharedPreferences(Context ctxt) {
	// return ctxt.getSharedPreferences("user_info", 0);
	// }

	protected void addResult(String email, String frequency, String volume) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(KEY_161);
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("frequency", frequency));
			nameValuePairs.add(new BasicNameValuePair("volume", volume));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			// HttpEntity entity = response.getEntity();
			// is = entity.getContent();
			Log.d("PushToDB", "Pushed Result");

		} catch (Exception e) {
			Log.e("PushToDB", "addResult. Error in http connection " + e.toString());

		}
	}

}

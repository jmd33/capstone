package com.capstone.hearingtest;

import android.R.color;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

public class AudioGram extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiogram);
		int w = 200, h = 200;

		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
//		Canvas canvas = new Canvas(bmp);
		
		Canvas c = new Canvas();
		c.setBitmap(bmp);
		Paint p = new Paint();
        p.setStrokeWidth(4f);
        p.setAntiAlias(true);
		p.setColor(Color.BLUE);//color.holo_blue_bright);
		c.drawCircle(10, 10, 7f, p);
		
		
	}
	
}
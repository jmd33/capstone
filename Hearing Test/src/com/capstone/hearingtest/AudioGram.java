package com.capstone.hearingtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class AudioGram extends Activity {
	private XYPlot mySimpleXYPlot;
	private Number[] series1Numbers;
	private Number[] series2Numbers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiogram);
		// int w = 200, h = 200;
		//
		// Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		// Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a
		// MUTABLE bitmap
		// // Canvas canvas = new Canvas(bmp);
		//
		// Canvas c = new Canvas();
		// c.setBitmap(bmp);
		// Paint p = new Paint();
		// p.setStrokeWidth(4f);
		// p.setAntiAlias(true);
		// p.setColor(Color.BLUE);//color.holo_blue_bright);
		// c.drawCircle(10, 10, 7f, p);
		// initialize our XYPlot reference:
		mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
		mySimpleXYPlot.setTitle("Audiogram");
		mySimpleXYPlot.setRangeLabel("Hearing Level");
		mySimpleXYPlot.setDomainLabel("Frequency Hz");
		mySimpleXYPlot.setDomainBoundaries(125, 8000, BoundaryMode.FIXED);
		mySimpleXYPlot.setRangeBoundaries(0, 10, BoundaryMode.FIXED);
		mySimpleXYPlot.getBackgroundPaint().setColor(Color.WHITE);
		// mySimpleXYPlot.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
		mySimpleXYPlot.getGraphWidget().getBackgroundPaint()
				.setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint()
				.setColor(Color.WHITE);
		mySimpleXYPlot.getGraphWidget().getDomainLabelPaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getRangeLabelPaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getDomainOriginLabelPaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint()
				.setColor(Color.BLACK);
		mySimpleXYPlot.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
		File file = getApplicationContext().getFileStreamPath(
				"account_data.txt");
		if (!file.exists()) {
			// don't populate the graph. maybe display a different message
		} else {

			int[] user_results = new int[20];
			// read data from user's hearing test.
			InputStream is;
			try {
				// File file = getApplicationContext().getFileStreamPath(
				// "account_data.txt");
				is = openFileInput("account_data.txt");
				Log.d("AudioGram", "is = " + is.toString());
				// InputStreamReader inputStreamReader = new
				// InputStreamReader(is);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));
				int count = 0;
				for (;;) {
					String line = in.readLine();
					if (line == null)
						break;
					else {
						Log.d("AudioGram", "line = " + line.toString());
						try {
							user_results[count] = Integer.parseInt(line
									.toString()) / 10;
							count++;
						} catch (Exception e) {

						}
					}

				}
				int[] freqs = getResources().getIntArray(R.array.frequencies);

				Number[] left_ear = { freqs[0], user_results[0], freqs[1],
						user_results[2], freqs[2], user_results[4], freqs[3],
						user_results[6], freqs[4], user_results[8], freqs[5],
						user_results[10], freqs[6], user_results[12], freqs[7],
						user_results[14] };
				// this is wrong it's using the same results for both left and
				// right
				Number[] right_ear = { freqs[0], user_results[1], freqs[1],
						user_results[3], freqs[2], user_results[5], freqs[3],
						user_results[7], freqs[4], user_results[9], freqs[5],
						user_results[11], freqs[6], user_results[13], freqs[7],
						user_results[15] };
				series1Numbers = left_ear;
				series2Numbers = right_ear;
				// BufferedReader bufferedReader = new
				// BufferedReader(inputStreamReader);
				// Log.d("AudioGram",
				// "bufferedReader = "+bufferedReader.toString());

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Turn the above arrays into XYSeries':
			// SimpleXYSeries takes a List so turn our array into a List
			XYSeries series1 = new SimpleXYSeries(
					Arrays.asList(series1Numbers),
					SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, // Y_VALS_ONLY,
																	// //
																	// Y_VALS_ONLY
																	// means use
					// the element index as
					// the x value
					"Left Ear"); // Set the display title of the series
			// same as above
			XYSeries series2 = new SimpleXYSeries(
					Arrays.asList(series2Numbers),
					SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,// Y_VALS_ONLY,
					"Right Ear");
			// Create a formatter to use for drawing a series using
			// LineAndPointRenderer:
			LineAndPointFormatter series1Format = new LineAndPointFormatter(
					Color.rgb(58, 170, 207), // line color
					Color.rgb(6, 121, 159), // point color
					null); // fill color (none)
			// add a new series' to the xyplot:
			mySimpleXYPlot.addSeries(series1, series1Format);

			// same as above:
			mySimpleXYPlot.addSeries(
					series2,
					new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(
							0, 0, 100), null));
		}
		// reduce the number of range labels
		mySimpleXYPlot.setTicksPerRangeLabel(3);

		// by default, AndroidPlot displays developer guides to aid in laying
		// out your plot.
		// To get rid of them call disableAllMarkup():
		mySimpleXYPlot.disableAllMarkup();

	}

}

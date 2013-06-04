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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidplot.series.XYSeries;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.xy.XYRegionFormatter;

import com.androidplot.ui.SizeMetrics;
import com.androidplot.xy.*;

public class AudioGram extends Activity {
	private XYPlot mySimpleXYPlot;
	private Number[] series1Numbers;
	private Number[] series2Numbers;
	private Context ctx = this;

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

        mySimpleXYPlot.setDomainBoundaries(0, 8000, BoundaryMode.FIXED);
		mySimpleXYPlot.setRangeBoundaries(0, 10, BoundaryMode.FIXED);
        mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1000);
        mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
//        mySimpleXYPlot.setRange

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
		mySimpleXYPlot.getTitleWidget().getLabelPaint().setColor(getResources().getColor(R.color.dark_blue));
        mySimpleXYPlot.getTitleWidget().getLabelPaint().setTextSize(50);
        mySimpleXYPlot.getTitleWidget().setHeight(100);
        mySimpleXYPlot.getTitleWidget().setWidth(400);

        mySimpleXYPlot.getGraphWidget().setPaddingTop(60);
        mySimpleXYPlot.getGraphWidget().setPaddingBottom(50);
        mySimpleXYPlot.getGraphWidget().setPaddingLeft(20);


//        mySimpleXYPlot.getGraphWidget().setPaddingBottom(50);
//        mySimpleXYPlot.getGraphWidget().setPaddingLeft(50);

        mySimpleXYPlot.getLegendWidget().setHeight(50);
        mySimpleXYPlot.getLegendWidget().setBackgroundPaint(new Paint(Color.BLUE));
        mySimpleXYPlot.getLegendWidget().setVisible(false);

        mySimpleXYPlot.getLegendWidget().getTextPaint().setTextSize(30);
//                setHeight(300, SizeLayoutType.ABSOLUTE);

        mySimpleXYPlot.getDomainLabelWidget().getLabelPaint().setTextSize(40);
        mySimpleXYPlot.getDomainLabelWidget().setWidth(500);
        mySimpleXYPlot.getDomainLabelWidget().setHeight(50);
        mySimpleXYPlot.getDomainLabelWidget().getLabelPaint().setColor(getResources().getColor(R.color.dark_blue));

        mySimpleXYPlot.getRangeLabelWidget().getLabelPaint().setTextSize(40);
        mySimpleXYPlot.getRangeLabelWidget().setWidth(50);
        mySimpleXYPlot.getRangeLabelWidget().setHeight(500);
        mySimpleXYPlot.getRangeLabelWidget().getLabelPaint().setColor(getResources().getColor(R.color.dark_blue));




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

            colorBackground();
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
		case R.id.home:
			intent = new Intent(ctx, Main.class);
			ctx.startActivity(intent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}



    private void colorBackground(){
        Number[] a = { 0, 4, 8000,
                4 };

        Number[] seriesANumbers = a;

        XYSeries seriesA = new SimpleXYSeries(
                Arrays.asList(seriesANumbers),
                SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,
                "");
        LineAndPointFormatter seriesAFormat = new LineAndPointFormatter(
                getResources().getColor(R.color.audiogram_green),                   // line color
                null,                   // point color
                 null
//              (getResources().getColor(R.color.audiogram_green))
);                            // fill color
        XYRegionFormatter regionAFormatter = new XYRegionFormatter(getResources().getColor(R.color.audiogram_green));
        seriesAFormat.addRegion(new RectRegion(0, 8000, 0, 4, "Positive"), regionAFormatter);



        Number[] b = { 0, 8, 8000,
                8 };

        Number[] seriesBNumbers = b;

        XYSeries seriesB = new SimpleXYSeries(
                Arrays.asList(seriesBNumbers),
                SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,
                "");
        LineAndPointFormatter seriesBFormat = new LineAndPointFormatter(
                getResources().getColor(R.color.audiogram_yellow),                   // line color
                null,                   // point color
                null
//        (getResources().getColor(R.color.audiogram_yellow))
        );
                                            // fill color
        XYRegionFormatter regionBFormatter = new XYRegionFormatter(getResources().getColor(R.color.audiogram_yellow));
        seriesBFormat.addRegion(new RectRegion(0, 8000, 4, 8, "Positive"), regionBFormatter);


        Number[] c = { 0, 10, 8000,
                10 };

        Number[] seriesCNumbers = c;

        XYSeries seriesC = new SimpleXYSeries(
                Arrays.asList(seriesCNumbers),
                SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,
                "");
        LineAndPointFormatter seriesCFormat = new LineAndPointFormatter(
                getResources().getColor(R.color.audiogram_red),                   // line color
                null,                   // point color
                null);
//                (getResources().getColor(R.color.audiogram_red)));                            // fill color

        XYRegionFormatter regionFormatter = new XYRegionFormatter(getResources().getColor(R.color.audiogram_red));
        seriesCFormat.addRegion(new RectRegion(0, 8000, 8, 10, "Positive"), regionFormatter);


//        mySimpleXYPlot.getLegendWidget().setSize(new SizeMetrics(3000, SizeLayoutType.ABSOLUTE, 3000, SizeLayoutType.ABSOLUTE));

//        mySimpleXYPlot.getLegendWidget().getIconSizeMetrics().s.setPadding(10, 1, 1, 1);

        mySimpleXYPlot.addSeries(seriesC, seriesCFormat);
        mySimpleXYPlot.addSeries(seriesB, seriesBFormat);
        mySimpleXYPlot.addSeries(seriesA, seriesAFormat);


//
//        Number[] seriesXNumbers = {1, 2, 3, 4, 2, 3, 4, 2, 2, 2, 3, 4, 2, 3, 2, 2};
//
//        // create our series from our array of nums:
//        XYSeries seriesX = new SimpleXYSeries(
//                Arrays.asList(seriesXNumbers),
//                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
//                "Thread #1");
//
//
//
//        LineAndPointFormatter seriesXFormat = new LineAndPointFormatter(
//                Color.rgb(0, 100, 0),                   // line color
//                Color.rgb(0, 100, 0),                   // point color
//                Color.rgb(100, 200, 0));                // fill color
//
//        // setup our line fill paint to be a slightly transparent gradient:
//        Paint lineFill = new Paint();
//        lineFill.setAlpha(200);
//        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.WHITE, Color.BLUE, Shader.TileMode.MIRROR));
//
//        StepFormatter stepFormatter  = new StepFormatter(Color.rgb(0, 0,0), Color.BLUE);
//        stepFormatter.getLinePaint().setStrokeWidth(1);
//
//        stepFormatter.getLinePaint().setAntiAlias(false);
//        stepFormatter.setFillPaint(lineFill);
//        mySimpleXYPlot.addSeries(seriesX, stepFormatter);

        // adjust the domain/range ticks to make more sense; label per tick for range and label per 5 ticks domain:
//        mySimpleXYPlot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
//        mySimpleXYPlot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1);
//        mySimpleXYPlot.setTicksPerRangeLabel(1);
//        mySimpleXYPlot.setTicksPerDomainLabel(5);


    }
}

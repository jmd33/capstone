package com.capstone.hearingtest;

import java.util.Arrays;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import android.R.color;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

public class AudioGram extends Activity {
    private XYPlot mySimpleXYPlot;

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
		mySimpleXYPlot.setRangeBoundaries(0,10, BoundaryMode.FIXED);
	    mySimpleXYPlot.getBackgroundPaint().setColor(Color.WHITE);
//	    mySimpleXYPlot.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
	    mySimpleXYPlot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
	    mySimpleXYPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
//		mySimpleXYPlot.getGraphWidget().set
	    mySimpleXYPlot.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
	    mySimpleXYPlot.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);

	    mySimpleXYPlot.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
	    mySimpleXYPlot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
	    mySimpleXYPlot.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
		Number[] series1Numbers = { 300, 2, 1000, 4, 3000, 6, 5000, 6, 7000, 3 };
		Number[] series2Numbers = { 300, 4, 1000, 5, 3000, 5, 5000, 4, 7000, 2 };

		// Turn the above arrays into XYSeries':
		XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Numbers), // SimpleXYSeries
																				// takes
																				// a
																				// List
																				// so
																				// turn
																				// our
																				// array
																				// into
																				// a
																				// List
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,    //Y_VALS_ONLY, // Y_VALS_ONLY means use
														// the element index as
														// the x value
				"Left Ear"); // Set the display title of the series

		// same as above
		XYSeries series2 = new SimpleXYSeries(Arrays.asList(series2Numbers),
				SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,//Y_VALS_ONLY,
				"Right Ear");

		// Create a formatter to use for drawing a series using
		// LineAndPointRenderer:
		LineAndPointFormatter series1Format = new LineAndPointFormatter(
				Color.rgb(58,170,207), // line color
				Color.rgb(6,121,159), // point color
				null); // fill color (none)

		// add a new series' to the xyplot:
		mySimpleXYPlot.addSeries(series1, series1Format);

		// same as above:
		mySimpleXYPlot.addSeries(
				series2,
				new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0,
						100), null));

		// reduce the number of range labels
		mySimpleXYPlot.setTicksPerRangeLabel(3);
		
		// by default, AndroidPlot displays developer guides to aid in laying
		// out your plot.
		// To get rid of them call disableAllMarkup():
		mySimpleXYPlot.disableAllMarkup();

	}

}

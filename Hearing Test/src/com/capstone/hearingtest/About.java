package com.capstone.hearingtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class About extends Activity {
	private ViewSwitcher viewSwitcher;
	private LinearLayout myFirstView;
	private LinearLayout mySecondView;
	private Context ctx = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		// String designed_by = " " +
		// getResources().getString(R.string.designed_by) + " ";
		// String tnp = " " + getResources().getString(R.string.tnp);

		// String attribution = "";
		// TextView tv = (TextView) findViewById(R.id.tv_ic_tv_attr);
		// attribution += getResources().getString(R.string.ic_tv_icon_link);
		// attribution += designed_by;
		// attribution += getResources().getString(R.string.ic_tv_author_link);
		// attribution += tnp;

		// tv.setMovementMethod(LinkMovementMethod.getInstance());

		// tv.setText(attribution);
		// tv.setLinksClickable(true);
		// TODO: linkify

		viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher1);
		myFirstView = (LinearLayout) findViewById(R.id.view1);
		mySecondView = (LinearLayout) findViewById(R.id.view2);
		final Button btn_terms = (Button) findViewById(R.id.btn_terms);
		Button btn_artwork = (Button) findViewById(R.id.btn_artwork);
		final Button btn_privacy = (Button) findViewById(R.id.btn_privacy);
		final Button btn_libraries = (Button) findViewById(R.id.btn_libraries);
		OnClickListener ocl = new OnClickListener() {

			public void onClick(View v) {
				Log.d("About", "view: " + v);
				if (viewSwitcher.getCurrentView() != myFirstView) {
					viewSwitcher.showPrevious();
				} else if (viewSwitcher.getCurrentView() != mySecondView) {

					viewSwitcher.showNext();
					final TextView tv_content = (TextView) findViewById(R.id.content);
					final TextView tv_title = (TextView) findViewById(R.id.title);

					if (v.getId() == btn_terms.getId()) {
						tv_content.setText(Html.fromHtml(getResources()
								.getString(R.string.terms_of_service_content)));
						tv_title.setText((getResources()
								.getString(R.string.terms_of_service)));
					} else if (v.getId() == btn_privacy.getId()) {
						tv_content.setText(Html.fromHtml(getResources()
								.getString(R.string.privacy_policy_content)));
						tv_title.setText((getResources()
								.getString(R.string.privacy_policy)));
					} else if (v.getId() == btn_libraries.getId()) {
						tv_content.setText((getResources()
								.getString(R.string.libraries)));
						tv_title.setText((getResources()
								.getString(R.string.libraries)));
					}
				}
			}
		};
		btn_terms.setOnClickListener(ocl);
		btn_privacy.setOnClickListener(ocl);
		btn_libraries.setOnClickListener(ocl);
		btn_artwork.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ctx, AboutArtwork.class);
				ctx.startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (viewSwitcher.getCurrentView() != myFirstView)
			viewSwitcher.showPrevious();
		else
			super.onBackPressed();
	}
}

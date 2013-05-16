package com.capstone.hearingtest;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		String designed_by = " " + getResources().getString(R.string.designed_by) + " ";
		String tnp = " " + getResources().getString(R.string.tnp);

		String attribution = "";
		TextView tv = (TextView) findViewById(R.id.tv_ic_tv_attr);
//			attribution +=	getResources().getString(R.string.ic_tv_icon_link);
//			attribution += designed_by;
//			attribution +=	getResources().getString(R.string.ic_tv_author_link);
//			attribution += tnp;
			
//			tv.setText(attribution);
			tv.setMovementMethod(LinkMovementMethod.getInstance());
//			tv.setLinksClickable(true);
//			TODO: linkify

		
	}
}

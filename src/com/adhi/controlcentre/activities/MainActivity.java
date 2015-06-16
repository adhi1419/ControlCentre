package com.adhi.controlcentre.activities;

import com.adhi.controlcentre.R;

import android.app.TabActivity;
import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

//		Resources res = getResources();

//		Drawable general = res.getDrawable(R.drawable.icon_general_config);
//		Drawable personalisation = res.getDrawable(R.drawable.icon_personalise_config);
//		Drawable tweaks = res.getDrawable(R.drawable.icon_tweaks_config);
//		Drawable theme = res.getDrawable(R.drawable.icon_theme_config);

		TabSpec tab1 = tabHost.newTabSpec("First Tab");
		TabSpec tab2 = tabHost.newTabSpec("Second Tab");
		TabSpec tab3 = tabHost.newTabSpec("Third tab");
		TabSpec tab4 = tabHost.newTabSpec("Fourth tab");

		tab1.setIndicator("General");
		tab1.setContent(new Intent(this, General.class));

		tab2.setIndicator("UI");
		tab2.setContent(new Intent(this, Personalisation.class));

		tab3.setIndicator("Tweaks");
		tab3.setContent(new Intent(this, Tweaks.class));

		tab4.setIndicator("Theme");
		tab4.setContent(new Intent(this, Theme.class));

		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);
		tabHost.addTab(tab4);

	}
}
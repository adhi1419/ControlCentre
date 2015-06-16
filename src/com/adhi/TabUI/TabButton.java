package com.adhi.TabUI;

import com.adhi.controlcentre.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabButton extends LinearLayout {
	TextView tab1;
	View view1;
	TextView tab2;
	View view2;
	TextView tab3;
	View view3;

	public TabButton(final Context context, AttributeSet attributeset) {
		super(context, attributeset);
		float f = getContext().getResources().getDisplayMetrics().density;
		int i = (int) (0.5F + 8F * f);
		android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams(
				1, -1);
		layoutparams.topMargin = i;
		layoutparams.bottomMargin = i;
		android.widget.LinearLayout.LayoutParams layoutparams1 = new android.widget.LinearLayout.LayoutParams(
				0, -1);
		layoutparams1.weight = 1.0F;
		layoutparams1.gravity = 17;
		tab1 = new TextView(context);
		tab1.setText("Myriad");
		tab1.setBackgroundResource(R.drawable.flipper_bg);
		tab1.setGravity(17);
		tab1.setTextSize(20);
		tab1.setLayoutParams(layoutparams1);
		tab1.setTextColor(0xff33b5e5);
		tab2 = new TextView(context);
		tab2.setText("Developers");
		tab2.setBackgroundResource(R.drawable.flipper_bg);
		tab2.setGravity(17);
		tab2.setTextSize(20);
		tab2.setLayoutParams(layoutparams1);
		tab2.setTextColor(Color.BLACK);
		tab3 = new TextView(context);
		tab3.setBackgroundResource(R.drawable.flipper_bg);
		tab3.setText("Credits");
		tab3.setGravity(17);
		tab3.setTextSize(20);
		tab3.setLayoutParams(layoutparams1);
		tab3.setTextColor(Color.BLACK);
		view1 = new View(context);
		view1.setLayoutParams(layoutparams);
		view2 = new View(context);
		view2.setLayoutParams(layoutparams);
		view3 = new View(context);
		view3.setLayoutParams(layoutparams);
		addView(tab1);
		addView(view1);
		addView(tab2);
		addView(view2);
		addView(tab3);
		addView(view3);

		tab1.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				view.setSelected(true);
				tab2.setSelected(false);
				tab3.setSelected(false);
				Intent intent = new Intent();
				intent.setAction("pineappleTabUI1");
				context.sendBroadcast(intent);
				tab1.setTextColor(0xff33b5e5);
				tab2.setTextColor(Color.BLACK);
				tab3.setTextColor(Color.BLACK);
			}
		});

		tab2.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				view.setSelected(true);
				tab1.setSelected(false);
				tab3.setSelected(false);
				Intent intent = new Intent();
				intent.setAction("pineappleTabUI2");
				context.sendBroadcast(intent);
				tab2.setTextColor(0xff33b5e5);
				tab1.setTextColor(Color.BLACK);
				tab3.setTextColor(Color.BLACK);
			}
		});

		tab3.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View view) {
				view.setSelected(true);
				tab1.setSelected(false);
				tab2.setSelected(false);
				Intent intent = new Intent();
				intent.setAction("pineappleTabUI3");
				context.sendBroadcast(intent);
				tab3.setTextColor(0xff33b5e5);
				tab2.setTextColor(Color.BLACK);
				tab1.setTextColor(Color.BLACK);
			}
		});
	}
}
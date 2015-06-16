package com.adhi.controlcentre.activities;

import com.adhi.controlcentre.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutRom extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_rom);

		View mMyriadBanner = (View) findViewById(R.id.banner_myriad);
		View mDeveloperShikar = (View) findViewById(R.id.dev_shikar);
		View mDeveloperAdhi = (View) findViewById(R.id.dev_adhi);

		mMyriadBanner.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchWebSite("http://tiny.cc/myriad");

			}
		});
		mDeveloperShikar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchWebSite("http://forum.xda-developers.com/member.php?u=5194357");

			}
		});
		mDeveloperAdhi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				launchWebSite("http://forum.xda-developers.com/member.php?u=5021809");

			}
		});
	}

	public void launchWebSite(String webAdress) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(webAdress));
		startActivity(i);
	}
}

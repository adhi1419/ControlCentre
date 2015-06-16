package com.adhi.controlcentre.activities;

import com.adhi.controlcentre.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class MyriadPlatActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myriad_plat);
		final VideoView mVideoView = (VideoView) findViewById(R.id.vid_myriad_plat);
		String uri = "android.resource://" + getPackageName() + "/"
				+ R.raw.myriad_plat;
		mVideoView.setVideoURI(Uri.parse(uri));
		// mVideoView.setMediaController(new MediaController(this));
		mVideoView.requestFocus();
		mVideoView.start();

	}

}

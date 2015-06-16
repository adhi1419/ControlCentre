package com.adhi.controlcentre.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adhi.controlcentre.CMDProcessor;
import com.adhi.controlcentre.R;
import com.adhi.quantumthemer.aFileChooser.utils.FileUtils;

@SuppressLint("SdCardPath")
public class Theme extends Activity {

	private static final int REQUEST_CODE = 6384;

	public static final String THEME_EXTENSION = ".qrt";
	public static final String PROGRESS_DIALOG_ROOT_CHECK = "Checking Root..";
	public static final String PROGRESS_DIALOG_COPYING_FILES = "Copying necessary files..";

	SharedPreferences prefs = null;

	public String themeDir;
	public String themeName;

	public boolean mInitD;

	Handler handler;

	public ProgressDialog mApplyingDialog;

	CMDProcessor mShell = new CMDProcessor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_selector);

		initializeEnvironment();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		;

		mInitD = prefs.getBoolean("theme_initd", false);

		Button mSelect = (Button) findViewById(R.id.select);
		Button mApply = (Button) findViewById(R.id.apply);

		handler = new Handler();

		cleanUp(true);

		mSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showChooser();

			}
		});

		mApply.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mApplyingDialog = ProgressDialog.show(Theme.this, "",
						"Applying " + themeName + "...");

				Thread th = new Thread(new Runnable() {

					@Override
					public void run() {
						mShell.su.runWaitFor("mount -o remount rw /system/");
						mShell.su.runWaitFor("busybox cp -vr " + themeDir
								+ "/system/" + " /sdcard/vrtheme/");
						mShell.su
								.runWaitFor("chmod 0755 /sdcard/vrtheme/installtheme.sh");
						mShell.su.runWaitFor("chmod 0755 /sdcard/vrtheme/zip");
						mShell.su
								.runWaitFor("chmod 0755 /sdcard/vrtheme/cleanup.sh");
						mShell.su
								.runWaitFor("chmod 0755 /sdcard/vrtheme/zipalign");

						copyBinaries();
						if (!mInitD)
							mShell.su
									.runWaitFor("sh /sdcard/vrtheme/07qrtheme");
						alertDialog(0);

					}
				});
				th.start();

			}
		});

	}

	private void showChooser() {
		Intent intent;
		if (!prefs.getBoolean("ext_file_browser", false)) {
			Intent target = FileUtils.createGetContentIntent();
			intent = Intent.createChooser(target,
					getString(R.string.choose_file));
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("file/*");
			startActivityForResult(intent, 1);
		}

		try {
			startActivityForResult(intent, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
		}
	}

	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				if (data != null) {
					final Uri uri = data.getData();

					try {
						final File file = FileUtils.getFile(uri);
						String themePath = file.getAbsolutePath();
						themeName = file.getName();
						String extractDir = "/sdcard/QuantumThemer/"
								+ themeName;
						themeDir = extractDir;
						ImageView mImageView = (ImageView) findViewById(R.id.image);
						View mPreviewImg = (View) findViewById(R.id.image);
						Button mApply = (Button) findViewById(R.id.apply);
						TextView mChooseTheme = (TextView) findViewById(R.id.text_select_theme);
						mChooseTheme.setText(R.string.no_preview);

						if (themeName.endsWith(THEME_EXTENSION)) {
							mShell.su
									.runWaitFor("mount -o remount rw /system/");
							mShell.su
									.runWaitFor("busybox mkdir /sdcard/QuantumThemer/");
							mShell.su.runWaitFor("busybox mkdir " + extractDir
									+ "/");
							mShell.su.runWaitFor("unzip -oq " + themePath
									+ " -d " + extractDir);
							// mPreviewImg.setVisibility(View.INVISIBLE);
							File image1 = new File(extractDir + "/preview.jpg"), image2 = new File(
									extractDir + "/preview.png");
							if (image1.exists()) {
								mPreviewImg.setVisibility(View.VISIBLE);
								mImageView.setImageBitmap(BitmapFactory
										.decodeFile(image1.getAbsolutePath()));
								mChooseTheme.setText("");
							} else if (image2.exists()) {
								mPreviewImg.setVisibility(View.VISIBLE);
								mImageView.setImageBitmap(BitmapFactory
										.decodeFile(image2.getAbsolutePath()));
								mChooseTheme.setText("");

							}
							mApply.setEnabled(true);
							copyBinaries();

						} else {
							Toast.makeText(Theme.this, R.string.invalid_theme,
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error",
								e);
					}
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean alertDialog(int paramInt) {
		return handler.postDelayed(new Runnable() {
			public void run() {
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(
						Theme.this);
				localBuilder.setTitle(getString(R.string.reboot_dialog_title));
				localBuilder
						.setMessage(
								getString(R.string.reboot_dialog_description))
						.setCancelable(false)
						.setNegativeButton(
								getString(R.string.reboot_dialog_restart_now),
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface paramAnonymous2DialogInterface,
											int paramAnonymous2Int) {
										new CMDProcessor().su
												.runWaitFor("mount -o remount rw /system/");
										new CMDProcessor().su
												.runWaitFor("/system/bin/reboot");
									}
								})
						.setPositiveButton(
								getString(R.string.reboot_dialog_restart_later),
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface paramAnonymous2DialogInterface,
											int paramAnonymous2Int) {
										paramAnonymous2DialogInterface.cancel();
									}
								});
				mApplyingDialog.dismiss();
				localBuilder.create().show();
			}
		}, paramInt * 10);
	}

	public void initializeEnvironment() {

		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("Files");
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}

		for (String filename : files) {
			System.out.println("File name => " + filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open("Files/" + filename); // if files resides
																// inside the
																// "Files"
																// directory
																// itself
				mShell.su.runWaitFor("busybox mkdir /sdcard/QuantumThemer/");
				out = new FileOutputStream("/sdcard/QuantumThemer/" + filename);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
				mShell.su.runWaitFor("mount -o remount rw /system/");
				mShell.su.runWaitFor("busybox mkdir /sdcard/vrtheme/");
				mShell.su
						.runWaitFor("unzip -oq /sdcard/QuantumThemer/vrtheme.zip -d /sdcard/vrtheme");
				copyBinaries();
			} catch (Exception e) {
				Log.e("tag", e.getMessage());
			} finally {
			}

		}
	}

	public void copyBinaries() {
		mShell.su.runWaitFor("mount -o remount rw /system/");

		mShell.su.runWaitFor("busybox cp /sdcard/vrtheme/zip /system/bin/zip");
		mShell.su
				.runWaitFor("busybox cp /sdcard/vrtheme/zipalign /system/bin/zipalign");
		mShell.su
				.runWaitFor("busybox cp /sdcard/vrtheme/07qrtheme /system/etc/init.d/07qrtheme");

		mShell.su.runWaitFor("chmod 0777 /system/bin/zip");
		mShell.su.runWaitFor("chmod 0777 /system/bin/zipalign");
		mShell.su.runWaitFor("chmod 0777 /system/init.d/07qrtheme");

	}

	public void cleanUp(boolean cleanQuantumFolder) {
		mShell.su.runWaitFor("rm -R /sdcard/vrtheme");
		mShell.su.runWaitFor("rm -R /sdcard/vrtheme-backup");
		if (cleanQuantumFolder)
			mShell.su.runWaitFor("rm -R /sdcard/QuantumThemer/");

	}

	public void appExit() {

		this.finish();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

		// Uncomment to Kill Process
		// int pid = android.os.Process.myPid();
		// intent android.os.Process.killProcess(pid);

	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

}

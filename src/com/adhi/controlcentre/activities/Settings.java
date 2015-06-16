package com.adhi.controlcentre.activities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.adhi.controlcentre.CMDProcessor;
import com.adhi.controlcentre.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

@SuppressLint("SdCardPath")
public class Settings extends PreferenceActivity {
	CMDProcessor mShell = new CMDProcessor();

	Handler hander;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Preference rSetup, rCleanup, rRestore, rVersion;

		hander = new Handler();

		addPreferencesFromResource(R.xml.settings);
		rSetup = (Preference) findPreference("init_setup");
		rCleanup = (Preference) findPreference("clean_up_files");
		rRestore = (Preference) findPreference("restore_backup");
		rVersion = (Preference) findPreference("app_version");

		rSetup.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				initializeEnvironment();
				return true;
			}
		});
		rCleanup.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				cleanUp(true);
				return true;
			}
		});
		rRestore.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				mShell.su.runWaitFor("mount -o remount rw /system/");
				mShell.su
						.runWaitFor("busybox cp /sdcard/vrtheme-backup/system/app/* /system/app/");
				mShell.su
						.runWaitFor("busybox cp /sdcard/vrtheme-backup/system/framework/* /system/framework/");
				mShell.su
						.runWaitFor("busybox cp /sdcard/vrtheme-backup/data/app/* /data/app/");
				alertDialog(1);
				return true;
			}
		});
		rVersion.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				launchWebSite("https://bit.ly/QuantumThemer");
				return true;
			}
		});

	}

	private void initializeEnvironment() {

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

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
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

	private boolean alertDialog(int paramInt) {
		return hander.postDelayed(new Runnable() {
			public void run() {
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(
						Settings.this);
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
				localBuilder.create().show();
			}
		}, paramInt * 10);
	}

	public void launchWebSite(String webAdress) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(webAdress));
		startActivity(i);
	}
}

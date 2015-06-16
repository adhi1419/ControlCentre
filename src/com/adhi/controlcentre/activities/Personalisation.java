package com.adhi.controlcentre.activities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.adhi.controlcentre.CMDProcessor;
import com.adhi.controlcentre.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class Personalisation extends PreferenceActivity {
	final CMDProcessor mShell = new CMDProcessor();
	Handler handler;
	public ProgressDialog mApplyingDialog;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.personalisation);

		handler = new Handler();
		initializeEnvironment();

		Preference PREF_BATTERY_ICON = (Preference) findPreference("pref_battery_icon");
		Preference PREF_LOCKSCREEN = (Preference) findPreference("pref_lockscreen");

		ListPreference PREF_SYSTEM_FONTS = (ListPreference) findPreference("fonts_list");
		ListPreference PREF_FRAMEWORK_ANIMATIONS = (ListPreference) findPreference("anims_list");
		ListPreference PREF_RECENT_PANEL = (ListPreference) findPreference("recent_panel_style_list");

		PREF_SYSTEM_FONTS
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (newValue.toString().equals("0")) {
							setFont("default");
						} else if (newValue.toString().equals("1")) {
							setFont("centurygothic");
						} else if (newValue.toString().equals("2")) {
							setFont("chococooky");
						} else if (newValue.toString().equals("3")) {
							setFont("segoe");
						} else if (newValue.toString().equals("4")) {
							setFont("ubuntu");
						} else if (newValue.toString().equals("5")) {
							setFont("zekton");
						}
						return true;
					}
				});
		PREF_FRAMEWORK_ANIMATIONS
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (newValue.toString().equals("0")) {
							setAnimation("blur");
						} else if (newValue.toString().equals("1")) {
							setAnimation("bounce");
						} else if (newValue.toString().equals("2")) {
							setAnimation("chaos");
						} else if (newValue.toString().equals("3")) {
							setAnimation("chaosrevenge");
						} else if (newValue.toString().equals("4")) {
							setAnimation("drop_in");
						} else if (newValue.toString().equals("5")) {
							setAnimation("flip");
						} else if (newValue.toString().equals("6")) {
							setAnimation("fly_in");
						} else if (newValue.toString().equals("7")) {
							setAnimation("fly_in_and_flip");
						} else if (newValue.toString().equals("8")) {
							setAnimation("fold");
						} else if (newValue.toString().equals("9")) {
							setAnimation("gingerbread");
						} else if (newValue.toString().equals("10")) {
							setAnimation("rubik");
						} else if (newValue.toString().equals("11")) {
							setAnimation("stock_ics");
						} else if (newValue.toString().equals("12")) {
							setAnimation("thepsynflip");
						} else if (newValue.toString().equals("13")) {
							setAnimation("thepsynshift");
						} else if (newValue.toString().equals("14")) {
							setAnimation("twisted");
						} else if (newValue.toString().equals("15")) {
							setAnimation("twisted_r1");
						} else if (newValue.toString().equals("16")) {
							setAnimation("twistedrubik");
						} else if (newValue.toString().equals("17")) {
							setAnimation("twistedrubikr2");
						} else if (newValue.toString().equals("18")) {
							setAnimation("vortex");
						} else if (newValue.toString().equals("19")) {
							setAnimation("default");
						}
						return true;
					}
				});
		PREF_RECENT_PANEL
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (newValue.toString().equals("0")) {
							mShell.su
									.runWaitFor("mount -o remount rw /system/");
							mShell.su
									.runWaitFor("busybox mkdir /sdcard/QuantumThemer/");
							mShell.su
									.runWaitFor("busybox mkdir /sdcard/QuantumThemer/default/");
							mShell.su
									.runWaitFor("unzip -oq /system/quantum/recent_panel/default.qrt -d /sdcard/QuantumThemer/default");
							mShell.su
									.runWaitFor("busybox cp -vr /sdcard/QuantumThemer/default/system/ /sdcard/vrtheme/");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/installtheme.sh");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/zip");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/cleanup.sh");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/zipalign");
							copyBinaries();
							mShell.su
									.runWaitFor("sh /sdcard/vrtheme/07qrtheme");
							reboot(0);
						} else if (newValue.toString().equals("1")) {
							mShell.su
									.runWaitFor("mount -o remount rw /system/");
							mShell.su
									.runWaitFor("busybox mkdir /sdcard/QuantumThemer/");
							mShell.su
									.runWaitFor("busybox mkdir /sdcard/QuantumThemer/sense/");
							mShell.su
									.runWaitFor("unzip -oq /system/quantum/recent_panel/sense.qrt -d /sdcard/QuantumThemer/sense");
							mShell.su
									.runWaitFor("busybox cp -vr /sdcard/QuantumThemer/sense/system/ /sdcard/vrtheme/");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/installtheme.sh");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/zip");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/cleanup.sh");
							mShell.su
									.runWaitFor("chmod 0755 /sdcard/vrtheme/zipalign");
							copyBinaries();
							mShell.su
									.runWaitFor("sh /sdcard/vrtheme/07qrtheme");
							reboot(0);

						}

						return true;
					}
				});

		PREF_BATTERY_ICON
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						try {
							Intent myIntent = new Intent();
							myIntent.setClassName("com.ghareeb.battery",
									"com.ghareeb.battery.Settings");
							startActivity(myIntent);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"This App is not Installed",
									Toast.LENGTH_SHORT).show();
						}
						return true;
					}

				});
		PREF_LOCKSCREEN
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						try {
							Intent myIntent = new Intent();
							myIntent.setClassName(
									"com.acer.android.breeze.personalization",
									"com.acer.android.breeze.personalization.PersonalizationActivity");
							startActivity(myIntent);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"This App is not Installed",
									Toast.LENGTH_SHORT).show();
						}
						return true;
					}

				});

	}

	public void setAnimation(String mAnimName) {
		mShell.su.runWaitFor("mount -o remount rw /system/");
		mShell.su.runWaitFor("busybox mkdir /sdcard/QuantumThemer/");
		mShell.su.runWaitFor("busybox mkdir /sdcard/QuantumThemer/" + mAnimName
				+ "/");
		mShell.su.runWaitFor("unzip -oq /system/quantum/anims/" + mAnimName
				+ ".qrt -d /sdcard/QuantumThemer/" + mAnimName);
		mShell.su.runWaitFor("busybox cp -vr /sdcard/QuantumThemer/"
				+ mAnimName + "/system/ /sdcard/vrtheme/");
		mShell.su.runWaitFor("chmod 0755 /sdcard/vrtheme/installtheme.sh");
		mShell.su.runWaitFor("chmod 0755 /sdcard/vrtheme/zip");
		mShell.su.runWaitFor("chmod 0755 /sdcard/vrtheme/cleanup.sh");
		mShell.su.runWaitFor("chmod 0755 /sdcard/vrtheme/zipalign");
		copyBinaries();
		mShell.su.runWaitFor("sh /sdcard/vrtheme/07qrtheme");
		reboot(0);
	}

	public void setFont(String mFontName) {
		mShell.su.runWaitFor("mount -o remount rw /system/");
		mShell.su.runWaitFor("busybox cp -vr /system/quantum/fonts/"
				+ mFontName + "/* /system/fonts/");
		mShell.su.runWaitFor("chmod 0755 /system/fonts/*");
		reboot(0);
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

	private boolean reboot(int mRebootDelay) {
		return handler.postDelayed(new Runnable() {
			public void run() {
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(
						Personalisation.this);
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
		}, mRebootDelay * 1000);
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

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
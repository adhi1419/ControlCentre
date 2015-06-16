package com.adhi.controlcentre.activities;

import com.adhi.controlcentre.CMDProcessor;
import com.adhi.controlcentre.R;
import com.adhi.controlcentre.SystemProperties;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class Tweaks extends PreferenceActivity {
	Handler handler;
	public ProgressDialog mApplyingDialog;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.tweaks);

		handler = new Handler();

		Preference PREF_LCD_DENSITY = (Preference) findPreference("pref_lcd_density");

		// add gerPreferenceManager(). if FC
		CheckBoxPreference PREF_ZIPALIGN = (CheckBoxPreference) findPreference("pref_zipalign");
		CheckBoxPreference PREF_OPTMIZE_RAM = (CheckBoxPreference) findPreference("pref_optimize_ram");
		CheckBoxPreference PREF_LOOPY_SCROLLING = (CheckBoxPreference) findPreference("pref_loopy_scrolling");
		CheckBoxPreference PREF_INTERNET = (CheckBoxPreference) findPreference("pref_internet");
		CheckBoxPreference PREF_SQLITE = (CheckBoxPreference) findPreference("pref_sqlite");
		CheckBoxPreference PREF_SD_TWEAKS = (CheckBoxPreference) findPreference("pref_sd_tweaks");
		CheckBoxPreference PREF_GRAPHICS = (CheckBoxPreference) findPreference("pref_graphics");
		CheckBoxPreference PREF_BUILD_PROP_TWEAKS = (CheckBoxPreference) findPreference("pref_build_prop_tweaks");
		CheckBoxPreference PREF_ADBLOCK = (CheckBoxPreference) findPreference("pref_adblock");

		rAddTweak(PREF_ZIPALIGN, "03darky_zipalign");
		rAddTweak(PREF_OPTMIZE_RAM, "05cleanup_init_ram");
		rAddTweak(PREF_LOOPY_SCROLLING, "06loopy");
		rAddTweak(PREF_INTERNET, "07netspeed");
		rAddTweak(PREF_SQLITE, "09sqlite_optimize");
		rAddTweak(PREF_SD_TWEAKS, "77sdcardspeedfix");
		rAddTweak(PREF_GRAPHICS, "01gpu_touchrender");

		PREF_BUILD_PROP_TWEAKS
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (newValue.toString().equals("true")) {
							new CMDProcessor().su
									.runWaitFor("mount -o remount rw /system/");
							new CMDProcessor().su
									.runWaitFor("busybox cp -f /system/quantum/tweaks/Ebuild.prop /system/build.prop");
						} else {
							new CMDProcessor().su
									.runWaitFor("mount -o remount rw /system/");
							new CMDProcessor().su
									.runWaitFor("busybox cp -f /system/quantum/tweaks/Dbuild.prop /system/build.prop");

						}
						new CMDProcessor().su
								.runWaitFor("chmod 0755 /system/build.prop");
						reboot(0);
						return true;
					}
				});
		PREF_ADBLOCK
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (newValue.toString().equals("true")) {
							new CMDProcessor().su
									.runWaitFor("mount -o remount rw /system/");
							new CMDProcessor().su
									.runWaitFor("busybox cp -f /system/quantum/tweaks/Ehosts /system/etc/hosts");
						} else {
							new CMDProcessor().su
									.runWaitFor("mount -o remount rw /system/");
							new CMDProcessor().su
									.runWaitFor("busybox cp -f /system/quantum/tweaks/Dhosts /system/etc/hosts");

						}
						reboot(0);
						return true;
					}
				});

		PREF_LCD_DENSITY
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String input = newValue.toString();
						String curValue = SystemProperties.get(Tweaks.this,
								"ro.sf.lcd_density");
						new CMDProcessor().su
								.runWaitFor("mount -o remount rw /system/");

						new CMDProcessor().su
								.runWaitFor("sed -i 's/ro.sf.lcd_density="
										+ curValue + "/ro.sf.lcd_density="
										+ input + "/g' /system/build.prop");
						reboot(0);

						return true;
					}
				});

	}

	public static void rAddTweak(CheckBoxPreference rCheckBoxPreference,
			final String rFileName) {
		rCheckBoxPreference
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (newValue.toString().equals("true")) {
							new CMDProcessor().su
									.runWaitFor("mount -o remount rw /system/");
							new CMDProcessor().su
									.runWaitFor("busybox cp /system/quantum/tweaks/"
											+ rFileName
											+ " /system/etc/init.d/"
											+ rFileName);
						} else {
							new CMDProcessor().su
									.runWaitFor("mount -o remount rw /system/");
							new CMDProcessor().su
									.runWaitFor("busybox rm /system/etc/init.d/"
											+ rFileName);

						}
						return true;
					}
				});

	}

	private boolean reboot(int mRebootDelay) {
		return handler.postDelayed(new Runnable() {
			public void run() {
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(
						Tweaks.this);
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

}
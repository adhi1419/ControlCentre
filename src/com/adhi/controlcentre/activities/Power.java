package com.adhi.controlcentre.activities;

import java.io.DataOutputStream;
import java.io.IOException;
import com.adhi.controlcentre.CMDProcessor;
import com.adhi.controlcentre.R;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class Power extends PreferenceActivity {
	final Context context = this;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle Adhi) {
		super.onCreate(Adhi);
		addPreferencesFromResource(R.xml.power);
		doNecessaryStuff();
	}

	@SuppressWarnings("deprecation")
	public void doNecessaryStuff() {
		Preference reboot = (Preference) findPreference("reboot");
		Preference reboot_quick = (Preference) findPreference("reboot_quick");
		Preference reboot_recovery = (Preference) findPreference("reboot_recovery");
		Preference power_off = (Preference) findPreference("power_off");
		Preference restart_systemui = (Preference) findPreference("restart_systemui");

		reboot.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				new CMDProcessor().su
						.runWaitFor("mount -o remount rw /system/");
				new CMDProcessor().su.runWaitFor("/system/bin/reboot");

				return true;
			}
		});
		reboot_quick
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						new CMDProcessor().su
								.runWaitFor("mount -o remount rw /system/");
						new CMDProcessor().su
								.runWaitFor("busybox killall system_server");
						return true;
					}
				});
		reboot_recovery
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						new CMDProcessor().su
								.runWaitFor("mount -o remount rw /system/");
						new CMDProcessor().su.runWaitFor("reboot recovery");
						return true;
					}
				});
		power_off.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				new CMDProcessor().su
						.runWaitFor("mount -o remount rw /system/");
				new CMDProcessor().su.runWaitFor("reboot -p");
				return true;
			}
		});
		restart_systemui
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						killPackage("com.android.systemui");
						return true;
					}
				});
	}

	private void killPackage(String packageToKill) {

		Process su = null;

		// get superuser
		try {

			su = Runtime.getRuntime().exec("su");

		} catch (IOException e) {

			e.printStackTrace();

		}

		// kill given package
		if (su != null) {

			try {

				DataOutputStream os = new DataOutputStream(su.getOutputStream());
				os.writeBytes("pkill " + packageToKill + "\n");
				os.flush();
				os.writeBytes("exit\n");
				os.flush();
				su.waitFor();

			} catch (IOException e) {

				e.printStackTrace();

			} catch (InterruptedException e) {

				e.printStackTrace();

			}
		}
	}
}

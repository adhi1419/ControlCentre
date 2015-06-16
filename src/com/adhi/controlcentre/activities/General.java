package com.adhi.controlcentre.activities;

import com.adhi.controlcentre.R;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class General extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.general);

		Preference PREF_VIPERFX = (Preference) findPreference("pref_viperfx");
		Preference PREF_GRAVITYBOX = (Preference) findPreference("pref_gb");
		Preference PREF_ENGINEERING_MODE = (Preference) findPreference("pref_engineering_mode");
		Preference PREF_POWER = (Preference) findPreference("pref_power");
		Preference PREF_QUANTUM_THEMER_SETTINGS = (Preference) findPreference("pref_quantum_themer_settings");
		Preference PREF_ABOUT_ROM = (Preference) findPreference("pref_about_rom");
		Preference PREF_ROM_FAQ = (Preference) findPreference("pref_rom_faq");

		PREF_VIPERFX
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						launchActivityIfFound("com.vipercn.viper4android_v2");
						return true;
					};
				});
		PREF_GRAVITYBOX
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						launchActivityIfFound("com.ceco.gm2.gravitybox");
						return true;
					};
				});

		PREF_ENGINEERING_MODE
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						try {
							Intent myIntent = new Intent();
							myIntent.setClassName("com.mediatek.engineermode",
									"com.mediatek.engineermode.EngineerMode");
							startActivity(myIntent);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"This App is not Installed",
									Toast.LENGTH_SHORT).show();
						}
						return true;
					}

				});

		PREF_POWER
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						try {
							Intent myIntent = new Intent();
							myIntent.setClassName("com.adhi.controlcentre",
									"com.adhi.controlcentre.activities.Power");
							startActivity(myIntent);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"This App is not Installed",
									Toast.LENGTH_SHORT).show();
						}
						return true;
					}

				});
		PREF_QUANTUM_THEMER_SETTINGS
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						try {
							Intent i = new Intent(General.this, Settings.class);
							startActivity(i);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"This App is not Installed",
									Toast.LENGTH_SHORT).show();
						}
						return true;
					}

				});
		PREF_ABOUT_ROM
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						try {
							Intent i = new Intent(General.this, AboutRom.class);
							startActivity(i);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"This App is not Installed",
									Toast.LENGTH_SHORT).show();
						}
						return true;
					}

				});
		PREF_ROM_FAQ
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						try {
							Intent i = new Intent(General.this, FAQ.class);
							startActivity(i);
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"This App is not Installed",
									Toast.LENGTH_SHORT).show();
						}
						return true;
					}

				});

	}

	public void launchActivityIfFound(String processName) {
		try {
			Intent intent = getPackageManager().getLaunchIntentForPackage(
					processName);
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"This App is not Installed", Toast.LENGTH_SHORT).show();
		}
	}
}
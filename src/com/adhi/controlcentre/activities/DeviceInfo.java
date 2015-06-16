package com.adhi.controlcentre.activities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.adhi.controlcentre.R;
import com.adhi.controlcentre.SystemProperties;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class DeviceInfo extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.device_info_settings);

		Preference firmware = (Preference) findPreference("firmware");
		firmware.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			long[] mHits = new long[3];
			private static final String LOG_TAG = "DeviceInfoSettings";

			public boolean onPreferenceClick(Preference preference) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.setClassName(DeviceInfo.this,
							PlatLogoActivity.class.getName());
					try {
						startActivity(intent);
					} catch (Exception e) {
						Log.e(LOG_TAG,
								"Unable to start activity " + intent.toString());
					}
				}
				return true;
			}
		});

		Preference mQuantumBanner = findPreference("quantum_banner");
		mQuantumBanner
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						launchWebSite("http://tiny.cc/myriad");
						return true;
					}
				});

		Preference findPreference2 = findPreference("mod_version");
		Preference mod_version = (Preference) findPreference2;
		mod_version
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					long[] mHits = new long[3];

					public boolean onPreferenceClick(Preference preference) {
						System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
						mHits[mHits.length - 1] = SystemClock.uptimeMillis();
						if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.setClassName(DeviceInfo.this,
									MyriadPlatActivity.class.getName());
							try {
								startActivity(intent);
							} catch (Exception e) {
								Log.e("Myriad Plat Logo", "Unable to start activity "
										+ intent.toString());

							}
						}
						return true;
					}
				});

		setStringSummary("device_cpu", getCPUInfo());

		addStringPreference("product_model",
				SystemProperties.get(DeviceInfo.this, "ro.product.model"));

		addStringPreference("build_date",
				SystemProperties.get(DeviceInfo.this, "ro.quantum.date"));

		addStringPreference("mod_version",
				SystemProperties.get(DeviceInfo.this, "ro.quantum.version"));

		setStringSummary("firmware", Build.VERSION.RELEASE);

		setStringSummary("baseband_version", Build.getRadioVersion());

		setStringSummary("display", Build.FINGERPRINT);

		setStringSummary("hardware", Build.HARDWARE);

		setStringSummary("kernel", getFormattedKernelVersion());

	}

	private String getCPUInfo() {
		String[] info = null;
		BufferedReader reader = null;

		try {
			// Grab a reader to /proc/cpuinfo
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("/proc/cpuinfo")), 1000);

			// Grab a single line from cpuinfo
			String line = reader.readLine();

			// Split on the colon, we need info to the right of colon
			info = line.split(":");
		} catch (IOException io) {
			io.printStackTrace();
			info = new String[1];
			info[1] = "error";
		} finally {
			// Make sure the reader is closed no matter what
			try {
				reader.close();
			} catch (Exception e) {
			}
			reader = null;
		}

		return info[1];
	}

	private void addStringPreference(String paramString1, String paramString2) {
		if (paramString2 != null)
			setStringSummary(paramString1, paramString2);
		while (true) {
			return;
		}
	}

	@SuppressWarnings("deprecation")
	private void setStringSummary(String preference, String value) {
		try {
			findPreference(preference).setSummary(value);
		} catch (RuntimeException e) {
			findPreference(preference).setSummary(
					getResources().getString(R.string.device_info_default));
		}
	}

	private String getFormattedKernelVersion() {
		String procVersionStr;

		try {
			procVersionStr = readLine("/proc/version");

			final String PROC_VERSION_REGEX = "\\w+\\s+" + /* ignore: Linux */
			"\\w+\\s+" + /* ignore: version */
			"([^\\s]+)\\s+" + /* group 1: 2.6.22-omap1 */
			"\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+" + /*
														 * group 2:
														 * (xxxxxx@xxxxx
														 * .constant)
														 */
			"\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+" + /* ignore: (gcc ..) */
			"([^\\s]+)\\s+" + /* group 3: #26 */
			"(?:PREEMPT\\s+)?" + /* ignore: PREEMPT (optional) */
			"(.+)"; /* group 4: date */

			Pattern p = Pattern.compile(PROC_VERSION_REGEX);
			Matcher m = p.matcher(procVersionStr);

			if (!m.matches()) {
				Log.e("DeviceInfoSettings",
						"Regex did not match on /proc/version: "
								+ procVersionStr);
				return "Unavailable";
			} else if (m.groupCount() < 4) {
				Log.e("DeviceInfoSettings",
						"Regex match on /proc/version only returned "
								+ m.groupCount() + " groups");
				return "Unavailable";
			} else {
				return (new StringBuilder(m.group(1)).append("\n")
						.append(m.group(2)).append(" ").append(m.group(3))
						.append("\n").append(m.group(4))).toString();
			}
		} catch (IOException e) {
			Log.e("DeviceInfoSettings",
					"IO Exception when getting kernel version for Device Info screen",
					e);

			return "Unavailable";
		}
	}

	private String readLine(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename),
				256);
		try {
			return reader.readLine();
		} finally {
			reader.close();
		}
	}

	public void launchWebSite(String webAdress) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(webAdress));
		startActivity(i);
	}
}

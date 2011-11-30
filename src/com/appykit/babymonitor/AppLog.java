package com.appykit.babymonitor;

import android.util.Log;

public class AppLog {
	public static final String APP_TAG = "BabyMon";

	public static int logString(String message) {
		return Log.i(APP_TAG, message);
	}
}
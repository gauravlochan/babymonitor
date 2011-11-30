package com.appykit.babymonitor;

import android.media.AudioFormat;
import android.media.AudioRecord;

public class Settings {
	public static final int RECORDER_BPP = 16;
	public static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
	public static final String AUDIO_RECORDER_FOLDER = "BabyMonitor";
	public static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
	public static final int RECORDER_SAMPLERATE = 11025;
	public static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	public static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	
	public static final int PLAYER_CHANNELS = AudioFormat.CHANNEL_OUT_MONO;

	public static int getBufferSize() {
		return AudioRecord.getMinBufferSize(Settings.RECORDER_SAMPLERATE,
				Settings.RECORDER_CHANNELS, Settings.RECORDER_AUDIO_ENCODING);

	}
}

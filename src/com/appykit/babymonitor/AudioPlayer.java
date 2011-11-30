package com.appykit.babymonitor;

import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Lame wrapper around AudioTrack
 * 
 * @author gauravlochan
 *
 */
public class AudioPlayer {
	private AudioTrack track;
	int bufferSize;

	public AudioPlayer() {
		bufferSize = Settings.getBufferSize();

		track = new AudioTrack(AudioManager.STREAM_MUSIC, Settings.RECORDER_SAMPLERATE,
			Settings.PLAYER_CHANNELS, Settings.RECORDER_AUDIO_ENCODING, bufferSize,
			AudioTrack.MODE_STREAM);
		
	}
	
	public void play() {
		track.play();
	}

	public void write(byte[] data) {
		track.write(data, 0, bufferSize);
	}

	public void stop() {
		track.stop();
	}

}

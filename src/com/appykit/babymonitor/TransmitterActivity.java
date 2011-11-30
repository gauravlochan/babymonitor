package com.appykit.babymonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedOutputStream;

import android.app.Activity;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class TransmitterActivity extends Activity {
	private AudioRecord recorder = null;
	private int bufferSize = 0;
	private Thread recordingThread = null;
	
	// hack around the issue that the activity gets recreated on orientation change
	private static boolean isRecording = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transmitter);

		setButtonHandlers();
		setButtonsEnabled(isRecording);

		bufferSize = Settings.getBufferSize();
	}

	private void setButtonHandlers() {
		((Button) findViewById(R.id.btnStart)).setOnClickListener(btnClick);
		((Button) findViewById(R.id.btnStop)).setOnClickListener(btnClick);
	}

	private void enableButton(int id, boolean isEnable) {
		((Button) findViewById(id)).setEnabled(isEnable);
	}

	private void setButtonsEnabled(boolean isRecording) {
		enableButton(R.id.btnStart, !isRecording);
		enableButton(R.id.btnStop, isRecording);
	}
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnStart: {
				AppLog.logString("Start Recording");

				setButtonsEnabled(true);
				startRecording();

				break;
			}
			case R.id.btnStop: {
				AppLog.logString("Start Recording");

				setButtonsEnabled(false);
				stopRecording();

				break;
			}
			}
		}
	};



	private String getTempFilename() {
		String filepath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(filepath, Settings.AUDIO_RECORDER_FOLDER);

		if (!file.exists()) {
			file.mkdirs();
		}

		File tempFile = new File(filepath, Settings.AUDIO_RECORDER_TEMP_FILE);

		if (tempFile.exists())
			tempFile.delete();

		return (file.getAbsolutePath() + "/" + Settings.AUDIO_RECORDER_TEMP_FILE);
	}

	private void startRecording() {
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				Settings.RECORDER_SAMPLERATE, Settings.RECORDER_CHANNELS,
				Settings.RECORDER_AUDIO_ENCODING, bufferSize);

		recorder.startRecording();

		isRecording = true;

		recordingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				writeAudioDataToFile();
			}
		}, "AudioRecorder Thread");
		
		

		recordingThread.start();
	}

	private void writeAudioDataToFile() {
		byte data[] = new byte[bufferSize];
		String filename = getTempFilename();
		FileOutputStream os = null;
		CircularBuffer cBuffer = new CircularBuffer(bufferSize, 100);
		BufferSendingThread sendingThread = new BufferSendingThread(cBuffer);
		sendingThread.start();

		try {
			os = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int read = 0;

		if (null != os) {
			while (isRecording) {
				read = recorder.read(data, 0, bufferSize);

				if (AudioRecord.ERROR_INVALID_OPERATION != read) {
					try {
						os.write(data);
						cBuffer.write(data);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				sendingThread.stopSending();
			}
			
		}
	}

	private void stopRecording() {
		if (null != recorder) {
			isRecording = false;

			recorder.stop();
			recorder.release();

			recorder = null;
			recordingThread = null;
		}
		
		WaveWriter waveWriter = new WaveWriter(bufferSize);
		waveWriter.copyWaveFile(getTempFilename());
		deleteTempFile();
	}

	private void deleteTempFile() {
		File file = new File(getTempFilename());
		file.delete();
	}


}

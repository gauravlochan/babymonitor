package com.appykit.babymonitor;

public class BufferSendingThread extends Thread {
	CircularBuffer cBuffer;
	boolean send = true;
	AudioPlayer player;
	
	public BufferSendingThread(CircularBuffer cBuffer) {
		this.cBuffer = cBuffer;
		player = new AudioPlayer();
	}
	
	public void stopSending() {
		send = false;
	}
	
	
	@Override
	public void run() {
		byte[] data;
		player.play();
		
		while (send) {
			data = cBuffer.read();
			player.write(data);
			
			if (data != null) {
				AppLog.logString("Got buffer to transmit");
			} else {
				AppLog.logString("Nothing to read, sleep");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		player.stop();
	}
}

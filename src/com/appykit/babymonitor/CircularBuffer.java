package com.appykit.babymonitor;

/**
 * This is a circular buffer.  A writer thread can keep adding samples,
 * a reader thread can read samples.  If the reader thread doesn't catch up, the
 * writer will overwrite older samples.
 * 
 * Only use a single reader thread, since this class will automatically move the
 * last-read pointer.
 * 
 * @author gauravlochan
 *
 */
public class CircularBuffer {
	byte[][] buffers;
	int writePointer = 0;
	int readPointer = 0;
	int unreadCount = 0;
	
	int numElements;
	int elementSize;
	
	public CircularBuffer(int elementSize, int numElements) {
		buffers = new byte[numElements][];
		this.numElements = numElements;
		this.elementSize = elementSize;
	}
	
	public void write(byte[] data) {
		synchronized (this) {
			buffers[writePointer] = data.clone();
			incrementWritePointer();
			AppLog.logString("WritePointer is "+writePointer);
			unreadCount++;

			// If overwriting the oldest buffer, then increment the read pointer
			if (writePointer == readPointer) {
				incrementReadPointer();
			}
			
		}
	}
	
	public byte[] read() {
		synchronized (this) {
			// Check if there is anything to read
			if (unreadCount == 0) { 
				AppLog.logString(
						"Nothing to read, readptr is "+readPointer+" and writepointer is "+writePointer);
				return null;
			}
			
			byte[] data = buffers[readPointer].clone();
			incrementReadPointer();
			AppLog.logString("ReadPointer is "+readPointer);
			unreadCount--;
			return data;
		}
	}
	
	private void incrementWritePointer() {
		writePointer++;
		if (writePointer == numElements) {
			writePointer = 0;
		}
	
	}
	
	private void incrementReadPointer() {
		readPointer++;
		if (readPointer == numElements) {
			readPointer = 0;
		}
	}

}

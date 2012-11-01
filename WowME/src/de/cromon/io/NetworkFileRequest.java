package de.cromon.io;

public class NetworkFileRequest {
	public NetworkFileRequest(String fileName) {
		mNotifierObject = new Object();
		FileName = fileName;
		ThrownException = null;
		DataStream = null;
	}
	
	public boolean awaitCompletion() {
		synchronized(mNotifierObject) {
			try {
				mNotifierObject.wait();
				return true;
			} catch (InterruptedException e) {
				return false;
			}
		}
	}
	
	public void signalCompletion() {
		synchronized(mNotifierObject) {
			mNotifierObject.notifyAll();
		}
	}
	
	public String FileName;
	public Exception ThrownException;
	public boolean FileAvailable;
	public int FileSize;
	public NetworkStream DataStream;
	
	private Object mNotifierObject;
}

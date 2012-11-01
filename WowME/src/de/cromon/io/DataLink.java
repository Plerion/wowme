package de.cromon.io;

public class DataLink {
	public DataLink() {
		mNetLink = new NetworkDataLink();
	}
	
	public DataLinkStream getFileStream(String fileName) {
		return mNetLink.requestFile(fileName);
	}
	
	private NetworkDataLink mNetLink = null;
}

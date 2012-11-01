package de.cromon.io;

import java.io.IOException;

public class NetworkStream extends DataLinkStream {

	public NetworkStream(NetworkFileRequest request) {
		mFileSize = request.FileSize;
		mFilePosition = 0;
		mDataArray = new byte[mFileSize];
	}
	
	@Override
	public void skip(int numBytes) {
		if(mFilePosition + numBytes > mFileSize || mFilePosition + numBytes < 0)
			throw new IndexOutOfBoundsException();
		
		mFilePosition += numBytes;
		
	}
	
	@Override
	public void setPosition(int streamPosition) throws IndexOutOfBoundsException {
		if(streamPosition > mFileSize)
			throw new IndexOutOfBoundsException();
		
		mFilePosition = streamPosition;
	}

	@Override
	public int getPosition() {
		// TODO Auto-generated method stub
		return mFilePosition;
	}

	@Override
	public int getLength() {
		return mFileSize;
	}

	@Override
	public int read() {
		int ret = 0;
		synchronized(this) {
			while(mFilePosition + 1 > mBytesAvailable) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			
			ret = mDataArray[mFilePosition];
			++mFilePosition;
		}
		
		return ret;
	}
	
	@Override
	public void readBytes(byte[] bytes) throws IOException {
		if(bytes.length == 0)
			return;
		
		synchronized(this) {
			while(mFilePosition + bytes.length > mBytesAvailable) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			
			System.arraycopy(mDataArray, mFilePosition, bytes, 0, bytes.length);
			mFilePosition += bytes.length;
		}
	}
	
	public void pushNetworkChunk(byte[] chunk) throws IOException {
		synchronized(this) {
			System.arraycopy(chunk, 0, mDataArray, mBytesAvailable, chunk.length);
			mBytesAvailable += chunk.length;
			notifyAll();
		}
	}
	
	public void signalReadFinished() {
		synchronized(this) {
			notifyAll();
		}
	}
	
	public boolean isComplete() {
		return mBytesAvailable == mFileSize;
	}

	private byte[] mDataArray;
	private int mBytesAvailable = 0;
	private int mFileSize;
	private int mFilePosition;
}

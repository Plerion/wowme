package de.cromon.io;

public class LittleEndianStream {
	public LittleEndianStream(byte[] chunkData) {
		mData = chunkData;
		mStreamSize = chunkData.length;
		mStreamPosition = 0;
	}
	
	public void readBytes(byte[] chunk) {
		if(mStreamPosition + chunk.length > mStreamSize)
			return;
		
		System.arraycopy(mData, mStreamPosition, chunk, 0, chunk.length);
		mStreamPosition += chunk.length;
	}
	
	public void skip(int numBytes) {
		if(mStreamPosition + numBytes > mStreamSize || mStreamPosition + numBytes < 0)
			throw new IndexOutOfBoundsException();
		
		mStreamPosition += numBytes;
		
	}
	
	public void setPosition(int streamPosition) throws IndexOutOfBoundsException {
		if(streamPosition > mStreamSize)
			throw new IndexOutOfBoundsException();
		
		mStreamPosition = streamPosition;
	}

	public int getPosition() {
		// TODO Auto-generated method stub
		return mStreamPosition;
	}

	public int getLength() {
		return mStreamSize;
	}
	
	public int readInt() {
		byte[] intData = new byte[4];
		readBytes(intData);
		
		int retVal = intData[0] & 0xFF;
		retVal |= ((intData[1] & 0xFF) << 8);
		retVal |= ((intData[2] & 0xFF) << 16);
		retVal |= ((intData[3] & 0xFF) << 24);
		
		return retVal;
	}
	
	public int readShort() {
		byte[] intData = new byte[2];
		readBytes(intData);
		
		int retVal = intData[0] & 0xFF;
		retVal |= ((intData[1] & 0xFF) << 8);
		
		return retVal;
	}
	
	public long readLong() {
		byte[] longData = new byte[8];
		readBytes(longData);
		
		long retVal = longData[0] & 0xFF;
		retVal |= ((longData[1] & 0xFF) << 8);
		retVal |= ((longData[2] & 0xFF) << 16);
		retVal |= ((longData[3] & 0xFF) << 24);
		retVal |= ((longData[4] & 0xFF) << 32);
		retVal |= ((longData[5] & 0xFF) << 40);
		retVal |= ((longData[6] & 0xFF) << 48);
		retVal |= ((longData[7] & 0xFF) << 56);
		
		return retVal;
	}
	
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}
	
	public int readByte() {
		if(mStreamPosition + 1 > mStreamSize)
			return 0;
		
		int value = mData[mStreamPosition] & 0xFF;
		++mStreamPosition;
		return value;
	}
	
	private byte[] mData;
	private int mStreamSize;
	private int mStreamPosition;
}

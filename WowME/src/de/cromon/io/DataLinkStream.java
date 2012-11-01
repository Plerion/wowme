package de.cromon.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class DataLinkStream extends InputStream {
	
	public abstract void setPosition(int streamPosition);
	public abstract int getPosition();
	public abstract int getLength();
	public abstract void readBytes(byte[] bytes) throws IOException;
	public abstract int read();
	
	public abstract void skip(int numBytes);
	
	/**
	 * @return The next byte on the socket.
	 * @throws IOException
	 */
	public int readByte() throws IOException {
		return read();
	}
	
	/**
	 * @return An integer read from the socket in little endian format.
	 * @throws IOException
	 */
	public int readInt32() throws IOException {
		byte[] intData = new byte[4];
		readBytes(intData);
		
		int retVal = (intData[0] & 0xFF);
		retVal |= ((intData[1] & 0xFF) << 8);
		retVal |= ((intData[2] & 0xFF) << 16);
		retVal |= ((intData[3] & 0xFF) << 24);
		
		return retVal;
	}
	
	public short readShort() throws IOException {
		byte[] intData = new byte[2];
		readBytes(intData);
		
		short retVal = intData[0];
		retVal |= (((short)intData[1]) << 8);
		
		return retVal;
	}
}

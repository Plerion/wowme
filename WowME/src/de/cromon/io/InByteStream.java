package de.cromon.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author Cromon
 *
 */
public class InByteStream {
	public InByteStream(InputStream destination) {
		mStream = destination;
		
	}
	
	public String readString() throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		int b = readByte();
		do {
			outStream.write(b);
		} while(b != 0);
		
		byte[] strArray = outStream.toByteArray();
		try	{
			return new String(strArray, mStringEncoding);	
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	
	/**
	 * @return The next byte on the socket.
	 * @throws IOException
	 */
	public int readByte() throws IOException {
		return mStream.read();
	}
	
	/**
	 * @return An integer read from the socket in little endian format.
	 * @throws IOException
	 */
	public int readInt32() throws IOException {
		byte[] intData = new byte[4];
		mStream.read(intData);
		
		int retVal = intData[0] & 0xFF;
		retVal |= ((intData[1] & 0xFF) << 8);
		retVal |= ((intData[2] & 0xFF) << 16);
		retVal |= ((intData[3] & 0xFF) << 24);
		
		return retVal;
	}
	
	public short readShort() throws IOException {
		byte[] intData = new byte[2];
		mStream.read(intData);
		
		short retVal = intData[0];
		retVal |= (((short)intData[1]) << 8);
		
		return retVal;
	}
	
	
	/**
	 * @param bytes 
	 * @throws IOException
	 */
	public void readBytes(byte[] bytes) throws IOException {
		int numRead = mStream.read(bytes);
		while(numRead < bytes.length) {
			int curChunk = mStream.read(bytes, numRead, (bytes.length - numRead));
			numRead += curChunk;
		}
	}
	
	public byte[] getStringBytes(String str) {
		try {
			return str.getBytes(mStringEncoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	private final String mStringEncoding = "UTF-8";
	
	private InputStream mStream;
}

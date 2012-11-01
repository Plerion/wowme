package de.cromon.io;

import java.io.*;

public class OutByteStream {
	public OutByteStream(OutputStream destination) {
		mStream = destination;
		
	}
	
	public void writeString(String str) throws IOException {
		try {
			mStream.write(str.getBytes(mStringEncoding));
			mStream.write(0);
		} catch (UnsupportedEncodingException e) {
			// ignore, wont happen.
			return;
		}
	}
	
	public void writeByte(int oneByte) throws IOException {
		mStream.write(oneByte);
	}
	
	public void writeInt32(int value) throws IOException {
		writeByte((value & 0xFF));
		writeByte((value >> 8) & 0xFF);
		writeByte((value >> 16) & 0xFF);
		writeByte((value >> 24) & 0xFF);
	}
	
	public void writeShort(int value) throws IOException {
		writeByte(value & 0xFF);
		writeByte((value >> 8) & 0xFF);
	}
	
	public void writeBytes(byte[] bytes) throws IOException {
		mStream.write(bytes);
	}
	
	public byte[] getStringBytes(String str) {
		try {
			return str.getBytes(mStringEncoding);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	private final String mStringEncoding = "UTF-8";
	
	private OutputStream mStream;
}

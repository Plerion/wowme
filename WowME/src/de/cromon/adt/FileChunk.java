package de.cromon.adt;

import java.io.IOException;

import de.cromon.io.DataLinkStream;
import de.cromon.io.LittleEndianStream;

public class FileChunk {
	public FileChunk(DataLinkStream stream) throws IOException {
		mChunkID = stream.readInt32();
		mChunkSize = stream.readInt32();
		byte[] chunkData = new byte[mChunkSize];
		stream.readBytes(chunkData);
		mChunkData = new LittleEndianStream(chunkData);
	}
	
	public int getSignature() { 
		return mChunkID;
	}
	
	public int getSize() {
		return mChunkSize;
	}
	
	public LittleEndianStream getData() {
		return mChunkData;
	}
	
	private int mChunkID;
	private int mChunkSize;
	private LittleEndianStream mChunkData;
}

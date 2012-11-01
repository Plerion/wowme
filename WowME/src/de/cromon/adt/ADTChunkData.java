package de.cromon.adt;

import java.nio.FloatBuffer;

import de.cromon.graphics.BufferElementType;
import de.cromon.graphics.Semantic;
import de.cromon.graphics.VertexElement;
import de.cromon.io.LittleEndianStream;

public class ADTChunkData {
	public ADTChunkData(FileChunk chunk) {
		mIFFData = chunk;
		mPositions = new VertexElement(Semantic.Position, 3, 145 * 3 * 4, BufferElementType.PositionFloat);
		mNormals = new VertexElement(Semantic.Normal, 3, 145 * 3 * 4, BufferElementType.PositionFloat);
	}
	
	public void loadIFFData() {
		mHeader = new MCNK(mIFFData.getData());
		
		initVertices();
		initNormals();
	}
	
	private void initVertices() {
		LittleEndianStream stream = mIFFData.getData();
		FloatBuffer posBuffer = mPositions.getFloatStream();
		posBuffer.position(0);
		
		stream.setPosition(mHeader.ofsHeight - 8);
		
		stream.skip(8);
		
		for(int i = 0; i < 17; ++i) {
			for(int j = 0; j < (((i % 2) != 0) ? 8 : 9); ++j) {
				float x = mHeader.indexX * Chunksize + j * Unitsize;
				float y = mHeader.indexY * Chunksize + i * 0.5f * Unitsize;
				float z = stream.readFloat() + mHeader.baseZ;
				
				posBuffer.put(x).put(y).put(z);
			}
		}
	}
	
	private void initNormals() {
		LittleEndianStream stream = mIFFData.getData();
		FloatBuffer normalBuffer = mNormals.getFloatStream();
		normalBuffer.position(0);
		
		stream.setPosition(mHeader.ofsNormal - 8);
		
		stream.skip(8);
		
		for(int i = 0; i < 145; ++i) {
			float nx = stream.readByte() / -127.0f;
			float ny = stream.readByte() / -127.0f;
			float nz = stream.readByte() / 127.0f;
			
			normalBuffer.put(nx).put(ny).put(nz);
		}
	}
	
	public VertexElement getPositionElement() {
		return mPositions;
	}
	
	public VertexElement getNormalElement() {
		return mNormals;
	}
	
	private static final float Tilesize = 533 + 1 / 3.0f;
	private static final float Chunksize = Tilesize / 16.0f;
	private static final float Unitsize = Chunksize / 8.0f;
	
	private VertexElement mPositions, mNormals;
	private MCNK mHeader;
	private FileChunk mIFFData;
}

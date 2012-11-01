package de.cromon.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.cromon.io.DataLinkStream;
import de.cromon.wowme.Game;

import android.opengl.GLES20;

public class BLPTexture {
	public BLPTexture(DataLinkStream stream) {
		try {
			loadFromStream(stream);
		} catch (IOException e) {
			mIsLoaded = false;
			return;
		}
	}
	
	public BLPTexture(String remoteName) {
		try {
			loadFromStream(Game.Instance.getDataLink().getFileStream(remoteName));
		} catch (IOException e) {
			mIsLoaded = false;
			return;
		}
	}
	
	public boolean isLoaded() {
		return mIsLoaded;
	}
	
	public int getTexture() {
		return mTextureID;
	}
	
	private void loadFromStream(DataLinkStream strm) throws IOException {
		strm.setPosition(0);
		
		int signature = strm.readInt32();
		if(signature != 0x32504C42) {
			throw new IOException("Invalid signature for texture!");
		}
		
		strm.skip(4);
		int compression = strm.read();
		strm.read();
		int alphaEncoding = strm.read();
		strm.read();
		int width = strm.readInt32();
		int height = strm.readInt32();
		
		int[] offsets = new int[16];
		int[] sizes = new int[16];
		int levelCount = 0;
		int startPos = strm.getPosition();
		
		for(int i = 0; i < 16; ++i) {
			strm.setPosition(startPos + i * 4);
			offsets[i] = strm.readInt32();
			strm.setPosition(startPos + (16 + i) * 4);
			sizes[i] = strm.readInt32();
			
			if(offsets[i] != 0 && sizes[i] != 0)
				++levelCount;
		}
		
		int blockSize = 0;
		int format = 0;
		if(compression == 2) {
			switch(alphaEncoding) {
			case 0:
				blockSize = 8;
				format = GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
				break;
				
			case 1:
				blockSize = 16;
				format = GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
				break;
				
			case 7:
				blockSize = 16;
				format = GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
				break;
			}
		}
		
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		mTextureID = textures[0];
		
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		
		for(int i = 0, j = 0; i < levelCount; ++i) {
			if(offsets[i] == 0 && sizes[i] == 0)
				continue;
			
			int levelWidth = width >> j;
			int levelHeight = height >> j;
			if(levelWidth == 0)
				levelWidth = 1;
			if(levelHeight == 0)
				levelHeight = 1;
			
			byte[] chunk = new byte[sizes[i]];
			strm.setPosition(offsets[i]);
			strm.readBytes(chunk);
			
			ByteBuffer buffer = ByteBuffer.allocateDirect(sizes[i]).order(ByteOrder.nativeOrder());
			buffer.put(chunk);
			buffer.position(0);
			
			int size = ((levelWidth + 3) / 4) * ((levelHeight + 3) / 4) * blockSize;
			GLES20.glCompressedTexImage2D(GLES20.GL_TEXTURE_2D, j, format, levelWidth, levelHeight, 0, size, buffer);
			++j;
		}
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}
	
	final int GL_COMPRESSED_RGBA_S3TC_DXT1_EXT = 0x83F1;
	final int GL_COMPRESSED_RGBA_S3TC_DXT3_EXT = 0x83F2;
	final int GL_COMPRESSED_RGBA_S3TC_DXT5_EXT = 0x83F3;
	
	private int mTextureID;
	private boolean mIsLoaded = false;
}

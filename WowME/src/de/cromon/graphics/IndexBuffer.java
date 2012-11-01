package de.cromon.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class IndexBuffer {
	public IndexBuffer(int numIndices) {
		mIndexBuffer = ByteBuffer.allocateDirect(numIndices * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
		mNumIndices = numIndices;
	}
	
	public ShortBuffer getStream() {
		return mIndexBuffer;
	}
	
	public void drawToTarget(int type) {
		mIndexBuffer.position(0);
		GLES20.glDrawElements(type, mNumIndices, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
	}
	
	private int mNumIndices;
	private ShortBuffer mIndexBuffer;
}

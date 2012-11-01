package de.cromon.graphics;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES20;

public class VertexElement {
	public VertexElement(Semantic semantic, int numComponents, int size, BufferElementType type) {
		this(semantic, numComponents, size, type, 0);		
	}
	
	public VertexElement(Semantic semantic, int numComponents, int size, BufferElementType type, int semanticIndex) {
		mSemantic = semantic;
		mNumComponents = numComponents;
		mType = type;
		mSemanticIndex = semanticIndex;
		
		switch(type)
		{
			case ColorDword:
				mStream = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asIntBuffer();
				break;
				
			default:
				mStream = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
				break;
		}
	}
	
	public int getSemanticIndex() {
		return mSemanticIndex;
	}
	
	public Buffer getStream() {
		return mStream;
	}
	
	public FloatBuffer getFloatStream() {
		return (FloatBuffer)mStream;
	}
	
	public IntBuffer getIntStream() {
		return (IntBuffer)mStream;
	}
	
	public boolean isNormalized() {
		switch(mType)
		{
		case ColorDword:
			return true;
		
		default:
			return false;
		}
	}
	
	public int getElementType() {
		switch(mType)
		{
			case ColorDword:
				return GLES20.GL_UNSIGNED_BYTE;
			
			default:
				return GLES20.GL_FLOAT;
		}
	}
	
	public int getNumComponents() {
		return mNumComponents;
	}
	
	public Semantic getSemantic() {
		return mSemantic;
	}
	
	public int getTotalSize() {
		return mStream.capacity();
	}
	
	public int getBufferOffset() {
		return mBufferOffset;
	}
	
	public void setBufferOffset(int offset) {
		mBufferOffset = offset;
	}
	
	private BufferElementType mType;
	private int mBufferOffset;
	private int mNumComponents;
	private Semantic mSemantic;
	private Buffer mStream;
	private int mSemanticIndex;
}

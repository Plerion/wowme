package de.cromon.graphics;

import java.io.IOException;

import de.cromon.math.Matrix;
import android.opengl.GLES20;

public class ShaderProgram {
	public ShaderProgram(VertexShader vShader, FragmentShader fShader) {
		mVertexShader = vShader;
		mFragmentShader = fShader;
		mProgramID = GLES20.glCreateProgram();
		
		GLES20.glAttachShader(mProgramID, mVertexShader.getShaderHandle());
		GLES20.glAttachShader(mProgramID, mFragmentShader.getShaderHandle());
		
		GLES20.glLinkProgram(mProgramID);
	}
	
	public ShaderProgram(String assetVShader, String assetFShader) throws IOException {
		mVertexShader = new VertexShader();
		mFragmentShader = new FragmentShader();
		
		mVertexShader.loadFromAsset(assetVShader);
		mFragmentShader.loadFromAsset(assetFShader);
		
		mProgramID = GLES20.glCreateProgram();
		
		GLES20.glAttachShader(mProgramID, mVertexShader.getShaderHandle());
		GLES20.glAttachShader(mProgramID, mFragmentShader.getShaderHandle());
		
		GLES20.glLinkProgram(mProgramID);
	}
	
	public int getAttributeIndex(Semantic elem, int semanticIndex) {
		switch(elem)
		{
			case Position:
				return GLES20.glGetAttribLocation(mProgramID, "vPosition" + semanticIndex);
				
			case Color:
				return GLES20.glGetAttribLocation(mProgramID, "vColor" + semanticIndex);
				
			case TexCoord:
				return GLES20.glGetAttribLocation(mProgramID, "vTexCoord" + semanticIndex);
				
			case Normal:
				return GLES20.glGetAttribLocation(mProgramID, "vNormal" + semanticIndex);
				
			default:
				return 0;	
		}
	}
	
	public void enableTexture(String param, int texture, int index) {
		int location = GLES20.glGetUniformLocation(mProgramID, param);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index);
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		
		GLES20.glUniform1i(location, index);
	}
	
	public void setMatrix(String paramName, Matrix value) {
		int index = GLES20.glGetUniformLocation(mProgramID, paramName);
		GLES20.glUniformMatrix4fv(index, 1, false, value.getFloats(), 0);
	}
	
	public void begin() {
		GLES20.glUseProgram(mProgramID);
	}
	
	public void end() {
		GLES20.glUseProgram(0);
	}
	
	private VertexShader mVertexShader;
	private FragmentShader mFragmentShader;
	private int mProgramID;
}

package de.cromon.graphics.ui;

import java.io.IOException;

import android.opengl.GLES20;

import de.cromon.graphics.BufferElementType;
import de.cromon.graphics.Semantic;
import de.cromon.graphics.ShaderProgram;
import de.cromon.graphics.VertexBuffer;
import de.cromon.graphics.VertexElement;
import de.cromon.math.Matrix;
import de.cromon.wowme.Game;

public class UIQuad {
	public UIQuad(float x, float y, float cx, float cy) {
		mPositionElem = new VertexElement(Semantic.Position, 3, 6 * 4 * 3, BufferElementType.PositionFloat);
		mPositionX = x;
		mPositionY = y;
		mSizeX = cx;
		mSizeY = cy;
		
		float[] squareCoords = { 
				0, 0, 0,
				cx, 0, 0,
				cx, cy, 0,
				0, 0, 0,
				cx, cy, 0,
				0, cy, 0
		};
		
		mPositionElem.getFloatStream().put(squareCoords);
		mVertices.addElement(mPositionElem);
		
		mColorElem = new VertexElement(Semantic.Color, 4, 6 * 4, BufferElementType.ColorDword);
		int[] colors = {
			0xFF00FF00,
			0xFF0000FF,
			0xFFFF00FF,
			0xFF00FF00,
			0xFFFF00FF,
			0xFFFF0000
		};
		
		mColorElem.getIntStream().put(colors);
		mVertices.addElement(mColorElem);
		
		mTexCoordElem = new VertexElement(Semantic.TexCoord, 2, 6 * 8, BufferElementType.PositionFloat);
		float[] texCoords = {
				0, 0,
				1, 0,
				1, 1,
				0, 0,
				1, 1,
				0, 1
		};
		
		mTexCoordElem.getFloatStream().put(texCoords);
		mVertices.addElement(mTexCoordElem);
		
		mOffsetMatrix = Matrix.Translation(x, y, 0);
	}
	
	static {
		try {
			mProgram = new ShaderProgram("UIQuadVertexShader.glsl", "UIQuadFragmentShader.glsl");
		} catch (IOException e) {
			Game.Instance.displayError(e.toString(), false);
			e.printStackTrace();
		}
	}
	
	public void draw() {
		mProgram.begin();
		
		mVertices.bindToTarget(mProgram);
		mProgram.setMatrix("matTranslation", Matrix.multiply(Game.Instance.getGraphicsDevice().OrthoMatrix, mOffsetMatrix));
		mProgram.enableTexture("quadSampler", mTexture, 0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
		
		mProgram.end();
	}
	
	public boolean isInRect(float x, float y) {
		return (x >= mPositionX && x <= mPositionX + mSizeX &&
				y >= mPositionY && y <= mPositionY + mSizeY);
	}
	
	public void setTextureCoords(float[] texCoords) {
		if(texCoords == null || texCoords.length != 12)
			return;
		
		mTexCoordElem.getFloatStream().position(0);
		mTexCoordElem.getFloatStream().put(texCoords);
	}
	
	public void setTextureEdges(float srcX, float srcY, float dstX, float dstY) {
		float xtop = 0;
		float ytop = 0;
		float xbot = srcX / dstX;
		float ybot = srcY / dstY;
		
		float[] coords = new float[] {
				xtop, ytop,
				xbot, ytop,
				xbot, ybot,
				xtop, ytop,
				xbot, ybot,
				xtop, ybot
		};
		
		setTextureCoords(coords);
	}
	
	public void setTexture(int texture) {
		mTexture = texture;
	}
	
	public float getPositionX() { return mPositionX; }
	public float getPositionY() { return mPositionY; }
	
	private int mTexture;
	private Matrix mOffsetMatrix;
	private float mPositionX, mPositionY;
	private float mSizeX, mSizeY;
	private VertexElement mPositionElem, mColorElem, mTexCoordElem;
	private VertexBuffer mVertices = new VertexBuffer();
	public static ShaderProgram mProgram;
}

package de.cromon.graphics.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.StringTokenizer;

import de.cromon.graphics.BufferElementType;
import de.cromon.graphics.Colors;
import de.cromon.graphics.Semantic;
import de.cromon.graphics.ShaderProgram;
import de.cromon.graphics.VertexBuffer;
import de.cromon.graphics.VertexElement;
import de.cromon.math.Matrix;
import de.cromon.math.Vector2;
import de.cromon.wowme.Game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

public class FontRender {
	public FontRender(Bitmap fontBmp, InputStream fontDesc) {
		load(fontBmp, fontDesc);
	}
	
	public FontRender(String fontName) {
			try {
				load(BitmapFactory.decodeStream(Game.Instance.getAssets().open(fontName + ".png")), Game.Instance.getAssets().open(fontName + ".fnt"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void load(Bitmap fontBmp, InputStream fontDesc) {
		IntBuffer bbuffer = ByteBuffer.allocateDirect(4 * fontBmp.getWidth() * fontBmp.getHeight()).order(ByteOrder.nativeOrder()).asIntBuffer();
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		mTextureID = textures[0];
		
		fontBmp.copyPixelsToBuffer(bbuffer);
		bbuffer.position(0);
		
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, fontBmp.getWidth(), fontBmp.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bbuffer);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		
		loadGlyphs(fontDesc);
	}
	
	static {
		try {
			mProgram = new ShaderProgram("UITextVertexShader.glsl", "UITextFragmentShader.glsl");
		} catch (IOException e) {
			Game.Instance.displayError(e.toString(), false);
			e.printStackTrace();
		}
	}
	
	public int getTexture() {
		return mTextureID;
	}
	
	public Vector2 measureString(String s) {
		float x = 0;
		float y = mFontHeight;
		
		for(int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if(mGlyphs.containsKey(c) == false)
				continue;
			
			GlyphInfo glyph = mGlyphs.get(c);
			
			if(c == ' ') {
				x += glyph.Width;
				continue;
			}
			
			x += glyph.Width;
		}
		
		return new Vector2(x, y);
	}
	
	public void renderString(TextElement elem, Colors color) {
		renderString(elem, color.getValue());
	}
	
	public void renderString(TextElement elem, int color) {
		String s = elem.Text;
		Vector2 size = elem.Size;
		Vector2 position = elem.Position;
		boolean fitHeight = elem.FitHeight;
		
		VertexElement posElem = new VertexElement(Semantic.Position, 3, 4 * 3 * 6 * s.length(), BufferElementType.PositionFloat);
		VertexElement texElem = new VertexElement(Semantic.TexCoord, 2, 4 * 2 * 6 * s.length(), BufferElementType.PositionFloat);
		VertexElement colorElem = new VertexElement(Semantic.Color, 4, 4 * 6 * s.length(), BufferElementType.ColorDword);
		
		Vector2 fullSize = measureString(s);
		float scale = 1.0f;
		
		if(fullSize.x > size.x) {
			scale = size.x / fullSize.x;
		}
		if(fullSize.y > size.y) {
			float tmpScale = size.y / fullSize.y;
			if(tmpScale < scale)
				scale = tmpScale;
		} else if((fullSize.y < size.y && fitHeight)) {
			float maxUpScale = size.x / fullSize.x;
			float upScale = size.y / fullSize.y;
			scale = Math.min(maxUpScale, upScale);
		}
		
		float x = 0;
		float y = 0;
		
		float transX, transY;
		transX = position.x;
		transY = position.y;
		
		if(elem.HorizontalCenter) {
			float width = fullSize.x * scale;
			if(width < size.x) {
				transX += (size.x - width) / 2.0f;
			}
		}
		
		Matrix scaleMatrix = Matrix.Scaling(scale, scale, 1);
		Matrix translationMatrix = Matrix.Translation(transX, transY, 0);
		
		Matrix worldMatrix = Matrix.multiply(translationMatrix, scaleMatrix);
		
		for(int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if(mGlyphs.containsKey(c) == false)
				continue;
			
			GlyphInfo glyph = mGlyphs.get(c);
			
			if(c == ' ') {
				x += glyph.Width;
				continue;
			}
			
			writeChar(x, y, glyph, posElem, texElem);
			x += glyph.Width;
			
			int[] colors = {
					color,
					color,
					color,
					color,
					color,
					color
			};
			
			colorElem.getIntStream().put(colors);
		}
		
		VertexBuffer vbuffer = new VertexBuffer();
		vbuffer.addElement(posElem);
		vbuffer.addElement(texElem);
		vbuffer.addElement(colorElem);
		
		mProgram.begin();
		
		vbuffer.bindToTarget(mProgram);
		mProgram.setMatrix("matTranslation", Matrix.multiply(Game.Instance.getGraphicsDevice().OrthoMatrix, worldMatrix));
		mProgram.enableTexture("quadSampler", mTextureID, 0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6 * s.length());
		
		mProgram.end();
	}
	
	private void writeChar(float x, float y, GlyphInfo glyph, VertexElement pos, VertexElement texCoord) {
		float[] coords = {
			x, y, 0,
			x + glyph.Width, y, 0,
			x + glyph.Width, y + mFontHeight, 0,
			x, y, 0,
			x + glyph.Width, y + mFontHeight, 0,
			x, y + mFontHeight, 0
		};
		
		float[] texCoords = {
			glyph.TexCoordX, glyph.TexCoordY,
			glyph.TexCoordCX, glyph.TexCoordY,
			glyph.TexCoordCX, glyph.TexCoordCY,
			glyph.TexCoordX, glyph.TexCoordY,
			glyph.TexCoordCX, glyph.TexCoordCY,
			glyph.TexCoordX, glyph.TexCoordCY,
		};
		
		pos.getFloatStream().put(coords);
		texCoord.getFloatStream().put(texCoords);
	}
	
	private void loadGlyphs(InputStream strm) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(strm));
		String line;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			return;
		}
		
		mFontHeight = Integer.parseInt(line);
		
		try {
			line = reader.readLine();
		} catch (IOException e) {
			return;
		}
		
		while(line != null)
		{
			StringTokenizer tk = new StringTokenizer(line, ",");
			char c;
			if(tk.countTokens() == 5)
				c = ',';
			else
				c = tk.nextToken().charAt(0);
			GlyphInfo info = new GlyphInfo();
			info.Width = Float.parseFloat(tk.nextToken());
			info.TexCoordX = Float.parseFloat(tk.nextToken());
			info.TexCoordY = Float.parseFloat(tk.nextToken());
			info.TexCoordCX = Float.parseFloat(tk.nextToken());
			info.TexCoordCY = Float.parseFloat(tk.nextToken());
			mGlyphs.put(c, info);
			
			try {
				line = reader.readLine();
			} catch (IOException e) {
				return;
			}
		}
	}
	
	private int mTextureID;
	
	private class GlyphInfo {
		public float TexCoordX, TexCoordY;
		public float TexCoordCX, TexCoordCY;
		public float Width;
	};
	
	private int mFontHeight = 0;
	private HashMap<Character, GlyphInfo> mGlyphs = new HashMap<Character, GlyphInfo>();
	private static ShaderProgram mProgram;
	
}

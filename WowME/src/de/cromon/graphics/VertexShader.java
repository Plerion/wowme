package de.cromon.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.cromon.wowme.Game;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

public class VertexShader {
	public VertexShader() {
		mShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
	}
	
	public void setShaderSource(String shader) {
		GLES20.glShaderSource(mShader, shader);
		GLES20.glCompileShader(mShader);
		int[] status = new int[1];
		GLES20.glGetShaderiv(mShader, GLES20.GL_COMPILE_STATUS, status, 0);
		if(status[0] != GLES20.GL_TRUE) {
			String log = GLES20.glGetShaderInfoLog(mShader);
			Log.d("GL", log);
		}
	}
	
	public void loadFromAsset(String assetName) throws IOException {
		InputStream asset = Game.Instance.getAssets().open(assetName, AssetManager.ACCESS_BUFFER);
		BufferedReader reader = new BufferedReader(new InputStreamReader(asset));
		String str = "";
		String line = reader.readLine();
		while(line != null) {
			str += line + "\n";
			line = reader.readLine();
		}
		
		setShaderSource(str);
	}
	
	public int getShaderHandle() { return mShader; }
	
	private int mShader;
}

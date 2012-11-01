package de.cromon.graphics;

import android.app.Activity;
import android.opengl.GLSurfaceView;

public class GLView {
	public GLView(Activity destView) {
		mSurface = new GLSurfaceView(destView);
		mSurface.setEGLContextClientVersion(2);
		mDevice = new GLDevice();
		
		mSurface.setRenderer(mDevice);
		
		destView.setContentView(mSurface);
	}
	
	public GLDevice getDevice() {
		return mDevice;
	}
	
	private GLDevice mDevice;
	private GLSurfaceView mSurface;
}

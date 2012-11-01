package de.cromon.wowme;

import android.content.res.AssetManager;
import android.widget.Toast;
import de.cromon.graphics.GLDevice;
import de.cromon.graphics.GLView;
import de.cromon.input.InputManager;
import de.cromon.io.*;

public class Game {
	public Game(MainWindow mainWindow) {
		Instance = this;
		mMainActivity = mainWindow;
	}
	
	public void setupGame() {
		if(mIsLoaded == true)
			return;
		
		mDataLink = new DataLink();
		mInputMgr = new InputManager(mMainActivity);
		mAssetManager = mMainActivity.getAssets();
		
		mGLView = new GLView(mMainActivity);
		mGraphicsDevice = mGLView.getDevice();
		mIsLoaded = true;
	}
	
	public InputManager getInputMgr() {
		return mInputMgr;
	}
	
	public DataLink getDataLink() {
		return mDataLink;
	}
	
	public AssetManager getAssets() {
		return mAssetManager;
	}
	
	public GLDevice getGraphicsDevice() {
		return mGraphicsDevice;
	}
	
	public Object requestService(String serviceName) {
		return mMainActivity.getSystemService(serviceName);
	}
	
	public void displayError(String error, boolean displayLong) {
		class ErrorRunnable implements Runnable {
			public ErrorRunnable(String error, boolean longDisplay) {
				mError = error;
				mLongDisplay = longDisplay;
			}
			
			public void run() {
				int length = mLongDisplay ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
				Toast.makeText(mMainActivity, mError, length).show();
	        }
			
			private String mError;
			private boolean mLongDisplay;
		}
		
		mMainActivity.runOnUiThread(new ErrorRunnable(error, displayLong));
	}
	
	public static Game Instance;
	
	private GLView mGLView;
	private GLDevice mGraphicsDevice;
	private MainWindow mMainActivity;
	private DataLink mDataLink;
	private boolean mIsLoaded = false;
	private AssetManager mAssetManager;
	private InputManager mInputMgr;
}

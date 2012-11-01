package de.cromon.adt;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.List;

import android.opengl.GLES20;
import android.util.Log;

import de.cromon.graphics.ShaderProgram;
import de.cromon.io.DataLinkStream;
import de.cromon.math.Matrix;
import de.cromon.wowme.Game;

public class ADTFile {
	public ADTFile(String fileName) {
		mFileName = fileName;
		asyncLoad();
	}
	
	public void render() {
		synchronized(this) {
			if(mIsLoaded == false)
				return;
		}
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		
		mChunkShader.begin();
		
		mChunkShader.setMatrix("worldViewProj", Game.Instance.getGraphicsDevice().getProjViewMatrix());
		
		synchronized(mChunkRenders) {
			for(MapChunkRender render : mChunkRenders) {
				render.renderChunk(mChunkShader);
			}
		}
		
		mChunkShader.end();
		
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		
	}
	
	private void asyncLoad() {
		class ADTAsyncLoader implements Runnable {
			ADTAsyncLoader(ADTFile file) {
				mFile = file;
			}
			
			@Override
			public void run() {
				try {
					mFile.asyncLoadCallback();
				} catch (Exception e) {
					Game.Instance.displayError(e.toString(), false);
				}
			}
			
			private ADTFile mFile;
		}
		
		mAsyncLoader = new Thread(new ADTAsyncLoader(this));
		mAsyncLoader.start();
	}
	
	private void asyncLoadCallback() throws Exception {
		mFileStream = Game.Instance.getDataLink().getFileStream(mFileName);
		while(mFileStream.getPosition() < mFileStream.getLength()) {
			try {
				FileChunk chunk = new FileChunk(mFileStream);
				switch(chunk.getSignature()) {
				case 0x4D564552:
					handleMVER(chunk);
					break;
					
				case 0x4D484452:
					handleMHDR(chunk);
					break;
					
				case 0x4D434E4B:
					handleMapChunkEntry(chunk);
					break;
				}
				//Log.d("ADT", "Got chunk: " + Integer.toHexString(chunk.getSignature()) + " - Size: " + chunk.getSize());
			} catch (IOException e) {
				break;
			}
		}
		
		synchronized(this) {
			this.mIsLoaded = true;
		}
		
		for(ADTChunkData chunk : mChunkData) {
			MapChunkRender render = new MapChunkRender(chunk);
			synchronized(mChunkRenders) {
				mChunkRenders.add(render);
			}
		}
	}
	
	private void handleMVER(FileChunk chunk) throws Exception {
		if((mLoadMask & MASK_MVER) != 0)
			throw new java.io.StreamCorruptedException("File has multiple MVER chunks!");
		
		mLoadMask |= MASK_MVER;
		int version = chunk.getData().readInt();
		if(version != 18)
			throw new Exception("Only version 18 files are supported!");
	}
	
	private void handleMHDR(FileChunk chunk) throws Exception {
		if(chunk.getSize() < MHDR.Size)
			throw new Exception("Invalid MHDR chunk!");
		
		if((mLoadMask & MASK_MHDR) != 0)
			throw new Exception("Multiple header chunks in ADT.");
		
		mLoadMask |= MASK_MHDR;
		mHeader = new MHDR(chunk.getData());
	}
	
	private void handleMapChunkEntry(FileChunk chunk) {
		ADTChunkData mapChunk = new ADTChunkData(chunk);
		mapChunk.loadIFFData();
		mChunkData.add(mapChunk);
	}
	
	private boolean mIsLoaded = false;
	private String mFileName;
	private DataLinkStream mFileStream;
	private Thread mAsyncLoader;
	private int mLoadMask = 0;
	private MHDR mHeader;
	private List<ADTChunkData> mChunkData = new LinkedList<ADTChunkData>();
	private List<MapChunkRender> mChunkRenders = new LinkedList<MapChunkRender>();
	
	private static ShaderProgram mChunkShader;
	
	static final int MASK_MVER 		= 1;
	static final int MASK_MHDR		= 2;
	static final int MASK_MCIN		= 4;
	
	static final int LOAD_COMPLETE	= MASK_MVER | MASK_MHDR | MASK_MCIN;
	
	static {
		try {
			mChunkShader = new ShaderProgram("ADTVertexShader.glsl", "ADTFragmentShader.glsl");
		} catch (IOException e) {
			Log.d("ADT", e.toString());
		}
	}
}

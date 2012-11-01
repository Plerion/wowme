package de.cromon.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.NumberFormat;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.cromon.adt.ADTFile;
import de.cromon.graphics.ui.Button;
import de.cromon.graphics.ui.FontRender;
import de.cromon.graphics.ui.TextElement;
import de.cromon.math.Matrix;
import de.cromon.math.Vector2;
import de.cromon.math.Vector3;
import de.cromon.math.ViewFrustum;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class GLDevice implements Renderer {

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		mTestADT.render();
		mButton.draw();
		
		++mFrameCount;
		
		/*if(Game.Instance.getInputMgr().isTouchDown()) {
			Vector3 pos = getWorldPosition(Game.Instance.getInputMgr().getLastTouch());
			mWorldPosElem.Text = "Touch position: (X: " + pos.x + " Y: " + pos.y + " Z: " + pos.z + ")";
			mInfoRender.renderString(mWorldPosElem, 0xFF13458B);
		}*/
		
		long curTime = System.currentTimeMillis();
		if((curTime - mLastFrameTime) > 1000) {
			float fps = (mFrameCount / (float)(curTime - mLastFrameTime)) * 1000.0f;
			
			mWorldPosElem.Text = "FPS: " + mFpsFormater.format(fps);
			
			mLastFrameTime = curTime;
			mFrameCount = 0;
		}
		
		mInfoRender.renderString(mWorldPosElem, 0xFF13458B);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		OrthoMatrix = Matrix.Ortho(width, height);
		PerspectiveMatrix = Matrix.Perspective(45.0f, width, height, 0.1f, 10000.0f);
		mWidth = width;
		mHeight = height;
		mWorldPosElem.Position = new Vector2(width - 500, 30);
		
		mViewFrustum.updateFrustum(Matrix.multiply(getActiveCamera().getMatView(), PerspectiveMatrix));
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
		gl.glClearColor(0.5f,  0.75f,  0.6f,  1);
		mButton = new Button(100, 100, 79 * 1.5f, 22 * 1.5f);
		mTestADT = new ADTFile("World/Maps/Kalimdor/Kalimdor_40_29.adt");
		mCamera = new Camera(this);
		mInfoRender = new FontRender("Calibri-24");
		mWorldPosElem = new TextElement();
		mWorldPosElem.FitHeight = false;
		mWorldPosElem.HorizontalCenter = false;
		mWorldPosElem.Size = new Vector2(500, 100);
		mWorldPosElem.Text = "FPS: ";
		mLastFrameTime = System.currentTimeMillis();
		mFpsFormater.setMaximumFractionDigits(2);
		mFpsFormater.setMinimumFractionDigits(2);
		mFpsFormater.setGroupingUsed(false);
	}
	
	public void onViewMatrixChanged() {
		mViewFrustum.updateFrustum(Matrix.multiply(getActiveCamera().getMatView(), PerspectiveMatrix));
	}
	
	public Camera getActiveCamera() {
		return mCamera;
	}
	
	public Matrix getProjViewMatrix() {
		return Matrix.multiply(PerspectiveMatrix, getActiveCamera().getMatView());
	}
	
	public Vector3 getWorldPosition(Vector2 screenPos) {
		int[] viewport = new int[4];
		GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, viewport, 0);
		float yReal = viewport[3] - screenPos.y;
		
		mDepthReadBuffer.position(0);
		GLES20.glReadPixels((int)screenPos.x, (int)screenPos.y, 1, 1, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_FLOAT, mDepthReadBuffer);
		mDepthReadBuffer.position(0);
		
		float depth = mDepthReadBuffer.getFloat();
		
		Matrix view = mCamera.getMatView();
		float[] objPosition = new float[4];
		
		GLU.gluUnProject(screenPos.x, yReal, depth, view.getFloats(), 0, PerspectiveMatrix.getFloats(), 0, viewport, 0, objPosition, 0);
	
		return new Vector3(objPosition[0] / objPosition[3], objPosition[1] / objPosition[3], objPosition[2] / objPosition[3]);
	}
	
	public ViewFrustum getViewFrustum() {
		return mViewFrustum;
	}
	
	public Matrix OrthoMatrix = new Matrix();
	public Matrix PerspectiveMatrix = new Matrix();
	private Button mButton;
	private ADTFile mTestADT;
	private Camera mCamera;
	@SuppressWarnings("unused")
	private int mWidth, mHeight;
	private ByteBuffer mDepthReadBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
	private long mLastFrameTime = 0;
	private int mFrameCount = 0;
	private NumberFormat mFpsFormater = NumberFormat.getInstance();
	private ViewFrustum mViewFrustum = new ViewFrustum();
	
	private FontRender mInfoRender;
	private TextElement mWorldPosElem;
}

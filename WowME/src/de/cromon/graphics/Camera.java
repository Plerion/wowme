package de.cromon.graphics;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import de.cromon.input.TouchListener;
import de.cromon.math.Matrix;
import de.cromon.math.Vector2;
import de.cromon.math.Vector3;
import de.cromon.wowme.Game;

public class Camera implements TouchListener {
	public Camera(GLDevice device) {
		mMatView = Matrix.LookAt(mPosition, mTarget, mUp);
		Game.Instance.getInputMgr().addTouchListener(this);
		mDevice = device;
	}
	
	public Matrix getMatView() {
		return mMatView;
	}
	
	@Override
	public void OnTouchEvent(MotionEvent args) {
		if(args.getPointerCount() == 0) {
			return;
		}
		
		if(args.getPointerCount() == 3) {
			handleTouchThreePointer(args);
		} else if(args.getPointerCount() == 2) {
			handleTouchTwoPoints(args);
		}
		
		// generic starting point when the first touch contact is established.
		if(args.getActionMasked() == MotionEvent.ACTION_DOWN) {
			// Action starts -> one or more fingers pressed the screen -> use the first as the main mover
			PointerCoords coords = new PointerCoords();
			args.getPointerCoords(0, coords);
			mTouchLastPosition = new Vector2(coords.x, coords.y);
		}
		
		// generic ending point of every touch contact
		if(args.getActionMasked() == MotionEvent.ACTION_UP) {
			PointerCoords coords = new PointerCoords();
			args.getPointerCoords(0, coords);
			mTouchLastPosition = new Vector2(coords.x, coords.y);
		}
		
		/*if(args.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
			for(int i = 0; i < args.getPointerCount(); ++i) {
				if(i != args.getActionIndex()) {
					PointerCoords coords = new PointerCoords();
					args.getPointerCoords(i, coords);
					mTouchLastPosition = new Vector2(coords.x, coords.y);
					// use that pointer for any further movement checks
					mMovePointer = i;
				}
			}
		}*/
	}
	
	private void handleTouchTwoPoints(MotionEvent args) {
		if(args.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN ||
		   args.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
			
			mTouchLastPosition = getPointerAverage(2, args);
			mZoomLastDistance = getPointerDistance(args, 0, 1);
		}
		
		if(args.getActionMasked() == MotionEvent.ACTION_MOVE) {
			Vector2 curPos = getPointerAverage(2, args);
			float dx = curPos.x - mTouchLastPosition.x;
			float dy = curPos.y - mTouchLastPosition.y;
			
			yaw(dx);
			pitch(dy);
			
			mMatView = Matrix.LookAt(mPosition, mTarget, mUp);
			mTouchLastPosition = new Vector2(curPos.x, curPos.y);
			
			float curDist = getPointerDistance(args, 0, 1);
			mPosition = Vector3.add(mPosition, Vector3.mul(mFront, (mZoomLastDistance - curDist)));
			mTarget = Vector3.add(mPosition, mFront);
			
			mZoomLastDistance = curDist;
			
			mDevice.onViewMatrixChanged();
		}
	}
	
	private void handleTouchThreePointer(MotionEvent args) {
		if(args.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN ||
		   args.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
			
			mTouchLastPosition = getPointerAverage(3, args);
		}
		
		if(args.getActionMasked() == MotionEvent.ACTION_MOVE) {
			Vector2 curPos = getPointerAverage(3, args);
			float dx = curPos.x - mTouchLastPosition.x;
			float dy = curPos.y - mTouchLastPosition.y;
			
			mPosition = Vector3.sub(mPosition, Vector3.mul(Vector3.NegUnitZ, dy));
			mTarget = Vector3.sub(mTarget, Vector3.mul(Vector3.NegUnitZ, dy));
			mPosition = Vector3.sub(mPosition, Vector3.mul(mRight, dx));
			mTarget = Vector3.sub(mTarget, Vector3.mul(mRight, dx));
			mFront = Vector3.sub(mTarget, mPosition);
			mFront.normalize();
			
			mMatView = Matrix.LookAt(mPosition, mTarget, mUp);
			mTouchLastPosition = new Vector2(curPos.x, curPos.y);
			mDevice.onViewMatrixChanged();
		}
	}
	
	private void yaw(float diff) {
		Matrix rot = Matrix.RotationZ(diff * 0.05f);
		mFront = Vector3.transformCoordinate(mFront, rot);
        mFront.normalize();
        mTarget = Vector3.add(mPosition, mFront);
        mRight = Vector3.transformCoordinate(mRight, rot);
        mRight.normalize();
        mUp = Vector3.transformCoordinate(mUp, rot);
        mUp.normalize();
	}
	
	private void pitch(float diff) {
		Matrix rot = Matrix.Rotation(diff * 0.05f, mRight.x, mRight.y, mRight.z);
        mFront = Vector3.transformCoordinate(mFront, rot);
        mFront.normalize();
        mTarget = Vector3.add(mPosition, mFront);
        mUp = Vector3.transformCoordinate(mUp, rot);
        mUp.normalize();
	}
	
	private Vector2 getPointerAverage(int numPointers, MotionEvent args) {
		Vector3 position = new Vector3();
		for(int i = 0; i < numPointers; ++i) {
			PointerCoords coords = new PointerCoords();
			args.getPointerCoords(i, coords);
			
			position = Vector3.add(position, new Vector3(coords.x, coords.y, 0));
		}
		
		Vector3 av = Vector3.div(position, numPointers);
		return new Vector2(av.x, av.y);
	}
	
	private float getPointerDistance(MotionEvent args, int start, int end) {
		PointerCoords coords1 = new PointerCoords();
		PointerCoords coords2 = new PointerCoords();
		
		args.getPointerCoords(start, coords1);
		args.getPointerCoords(end, coords2);
		
		Vector3 p1 = new Vector3(coords1.x, coords1.y, 0);
		Vector3 p2 = new Vector3(coords2.x, coords2.y, 0);
		
		return Vector3.sub(p1, p2).length();
	}
	
	private Vector2 mTouchLastPosition;
	private float mZoomLastDistance;
	private GLDevice mDevice;
	
	Vector3 mFront = new Vector3(1, 0, 0);
	Vector3 mRight = new Vector3(0, -1, 0);
	Vector3 mPosition = new Vector3(-100, 0, 50);
	Vector3 mTarget = new Vector3(0, 0, 50);
	Vector3 mUp = new Vector3(0, 0, 1);
	
	Matrix mMatView;
}

package de.cromon.input;

import java.util.LinkedList;
import java.util.List;

import android.view.MotionEvent;
import de.cromon.math.Vector2;
import de.cromon.wowme.MainWindow;

public class InputManager implements InputListener {
	public InputManager(MainWindow inputControl) {
		mActivity = inputControl;
		mActivity.addInputListener(this);
	}
	
	public void addTouchListener(TouchListener listener) {
		synchronized(mTouchListeners) {
			mTouchListeners.add(listener);
		}
	}
	
	@Override
	public void OnTouchEvent(MotionEvent args) {
		if(args.getPointerCount() > 0) {
			mTouchPosition = Vector2.FromMotionEvent(args, 0);
		}
		
		if(args.getActionMasked() == MotionEvent.ACTION_DOWN)
			mIsTouchDown = true;
		else if(args.getActionMasked() == MotionEvent.ACTION_UP)
			mIsTouchDown = false;
		
		synchronized(mTouchListeners) {
			for(TouchListener listener : mTouchListeners) {
				listener.OnTouchEvent(args);
			}
		}
	}
	
	public boolean isTouchDown() {
		return mIsTouchDown;
	}
	
	public Vector2 getLastTouch() {
		return mTouchPosition;
	}

	private Vector2 mTouchPosition;
	private boolean mIsTouchDown = false;	
	private MainWindow mActivity;
	private List<TouchListener> mTouchListeners = new LinkedList<TouchListener>();
}

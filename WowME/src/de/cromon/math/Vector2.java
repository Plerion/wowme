package de.cromon.math;

import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class Vector2 {
	public Vector2() {
		x = y = 0;
	}
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector2 FromMotionEvent(MotionEvent event, int pointer) {
		PointerCoords pc = new PointerCoords();
		event.getPointerCoords(pointer, pc);
		
		return new Vector2(pc.x, pc.y);
	}
	
	public float x, y;
}

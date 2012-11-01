package de.cromon.math;

public class AABox {
	public AABox() {
		maximum = new Vector3(0, 0, 0);
		minimum = new Vector3(0, 0, 0);
		
		recalcCorners();
	}
	
	public AABox(Vector3 min, Vector3 max) {
		maximum = max;
		minimum = min;
		
		recalcCorners();
	}
	
	public void setMinimum(Vector3 minimum) {
		this.minimum = minimum;
		
		recalcCorners();
	}
	
	public void setMaximum(Vector3 maximum) {
		this.maximum = maximum;
		
		recalcCorners();
	}
	
	public void setBox(Vector3 minimum, Vector3 maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
		
		recalcCorners();
	}
	
	public Vector3 getMinimum() { return minimum; }
	public Vector3 getMaximum() { return maximum; }
	
	private void recalcCorners() {
		corners[0] = minimum;
		corners[1] = new Vector3(maximum.x, minimum.y, minimum.z);
		corners[2] = new Vector3(maximum.x, maximum.y, minimum.z);
		corners[3] = new Vector3(minimum.x, maximum.y, minimum.z);
		corners[4] = new Vector3(maximum.x, minimum.y, maximum.z);
		corners[5] = new Vector3(maximum.x, maximum.y, maximum.z);
		corners[6] = new Vector3(minimum.x, maximum.y, maximum.z);
		corners[7] = maximum;
		
	}
	
	public Vector3[] corners = new Vector3[8];
	private Vector3 maximum, minimum;
}

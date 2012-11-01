package de.cromon.math;

public class Plane {
	public Plane() {
		mNormal = new Vector3();
		d = 0.0f;
	}
	
	public void setPoints(Vector3 v1, Vector3 v2, Vector3 v3) {
		Vector3 d1 = Vector3.sub(v1, v2);
		Vector3 d2 = Vector3.sub(v3, v2);
		
		mNormal = Vector3.cross(d1, d2);
		mNormal.normalize();
		
		d = -Vector3.dot(mNormal, v2);
	}
	
	public void setNormalPoint(Vector3 normal, Vector3 point) {
		mNormal = normal;
		mNormal.normalize();
		
		d = -Vector3.dot(mNormal, point);
	}
	
	public void setCoeffs(float a, float b, float c, float d) {
		mNormal = new Vector3(a, b, c);
		float len = mNormal.length();
		mNormal.normalize();
		
		this.d = d / len;
	}
	
	public float distance(Vector3 p) {
		return (d + Vector3.dot(mNormal, p));
	}
	
	private Vector3 mNormal;
	private float d;
}

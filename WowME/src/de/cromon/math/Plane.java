package de.cromon.math;

public class Plane {
	public Plane() {
		normal = new Vector3();
		d = 0.0f;
	}
	
	public void setPoints(Vector3 v1, Vector3 v2, Vector3 v3) {
		Vector3 d1 = Vector3.sub(v1, v2);
		Vector3 d2 = Vector3.sub(v3, v2);
		
		normal = Vector3.cross(d1, d2);
		normal.normalize();
		
		d = -Vector3.dot(normal, v2);
	}
	
	public void setNormalPoint(Vector3 normal, Vector3 point) {
		this.normal = normal;
		this.normal.normalize();
		
		d = -Vector3.dot(this.normal, point);
	}
	
	public void setCoeffs(float a, float b, float c, float d) {
		normal = new Vector3(a, b, c);
		float len = normal.length();
		normal.normalize();
		
		this.d = d / len;
	}
	
	public float distance(Vector3 p) {
		return (d + Vector3.dot(normal, p));
	}
	
	public Vector3 normal;
	public float d;
}

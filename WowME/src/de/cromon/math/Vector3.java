package de.cromon.math;

public class Vector3 {
	public float x, y, z;
	
	public Vector3() {
		x = y = z = 0;
	}
	
	public float length() {
		return android.util.FloatMath.sqrt(x * x + y * y + z * z);
	}
	
	public void normalize() {
		float len = length();
		if(len == 0)
			return;
		
		x /= len;
		y /= len;
		z /= len;
	}
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector3 add(Vector3 v1, Vector3 v2) {
		Vector3 ret = new Vector3();
		ret.x = v1.x + v2.x;
		ret.y = v1.y + v2.y;
		ret.z = v1.z + v2.z;
		
		return ret;
	}
	
	public static Vector3 sub(Vector3 v1, Vector3 v2) {
		Vector3 ret = new Vector3();
		ret.x = v1.x - v2.x;
		ret.y = v1.y - v2.y;
		ret.z = v1.z - v2.z;
		
		return ret;
	}
	
	public static Vector3 mul(Vector3 v1, Vector3 v2) {
		Vector3 ret = new Vector3();
		ret.x = v1.x * v2.x;
		ret.y = v1.y * v2.y;
		ret.z = v1.z * v2.z;
		
		return ret;
	}
	
	public static Vector3 mul(Vector3 v1, float val) {
		Vector3 ret = new Vector3();
		ret.x = v1.x * val;
		ret.y = v1.y * val;
		ret.z = v1.z * val;
		
		return ret;		
	}
	
	public static Vector3 div(Vector3 v1, float val) {
		Vector3 ret = new Vector3();
		ret.x = v1.x / val;
		ret.y = v1.y / val;
		ret.z = v1.z / val;
		
		return ret;		
	}
	
	public static float dot(Vector3 v1, Vector3 v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	public static Vector3 cross(Vector3 v1, Vector3 v2) {
		Vector3 ret = new Vector3();
		
		ret.x = v1.y * v2.z - v1.z * v2.y;
		ret.y = v1.z * v2.x - v1.x * v2.z;
		ret.z = v1.x * v2.y - v1.y * v2.x;
		
		return ret;
	}
	
	public static Vector3 transformCoordinate(Vector3 v1, Matrix matrix) {
		float[] mat = matrix.getFloats();
		float x = (v1.x * mat[0]) + (v1.y * mat[4]) + (v1.z * mat[8]) + mat[12];
		float y = (v1.x * mat[1]) + (v1.y * mat[5]) + (v1.z * mat[9]) + mat[13];
		float z = (v1.x * mat[2]) + (v1.y * mat[6]) + (v1.z * mat[10]) + mat[14];
		float w = 1.0f / ((v1.x * mat[3]) + (v1.y * mat[7]) + (v1.z * mat[11]) + mat[15]);
		
		return new Vector3(x / w, y / w, z / w);
	}
	
	public static Vector3 UnitX = new Vector3(1, 0, 0);
	public static Vector3 UnitY = new Vector3(0, 1, 0);
	public static Vector3 UnitZ = new Vector3(0, 0, 1);
	public static Vector3 NegUnitX = new Vector3(-1, 0, 0);
	public static Vector3 NegUnitY = new Vector3(0, -1, 0);
	public static Vector3 NegUnitZ = new Vector3(0, 0, -1);
}

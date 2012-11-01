package de.cromon.math;

public class Matrix {
	public Matrix() {
		android.opengl.Matrix.setIdentityM(mMatrixFloats, 0);
	}
	
	public float get(int row, int col) {
		return mMatrixFloats[row * 4 + col];
	}
	
	public static Matrix Translation(float x, float y, float z) {
		Matrix ret = new Matrix();
		
		android.opengl.Matrix.translateM(ret.mMatrixFloats, 0, x, y, z);
		
		return ret;
	}
	
	public static Matrix Scaling(float x, float y, float z) {
		Matrix ret = new Matrix();
		
		android.opengl.Matrix.scaleM(ret.mMatrixFloats, 0, x, y, z);
		
		return ret;
	}
	
	/**
	 * @param angle Angle in degrees to rotate
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static Matrix Rotation(float angle, float x, float y, float z) {
		Matrix ret = new Matrix();
		
		android.opengl.Matrix.rotateM(ret.mMatrixFloats, 0, angle, x, y, z);
		
		return ret;
	}
	
	public static Matrix RotationZ(float angle) {
		return Rotation(angle, 0, 0, 1);
	}
	
	public static Matrix RotationY(float angle) {
		return Rotation(angle, 0, 1, 0);
	}
	
	public static Matrix RotationX(float angle) {
		return Rotation(angle, 1, 0, 0);
	}
	
	public static Matrix Ortho(int width, int height) {
		Matrix ret = new Matrix();
		android.opengl.Matrix.orthoM(ret.mMatrixFloats, 0, 0, width, height, 0, 0, 1);
		return ret;
	}
	
	public void multiply(Matrix other) {
		float[] tmpValues = new float[16];
		android.opengl.Matrix.multiplyMM(tmpValues, 0, mMatrixFloats, 0, other.mMatrixFloats, 0);
		mMatrixFloats = tmpValues;
	}
	
	public static Matrix multiply(Matrix lhs, Matrix rhs) {
		Matrix ret = new Matrix();
		android.opengl.Matrix.multiplyMM(ret.mMatrixFloats, 0, lhs.mMatrixFloats, 0, rhs.mMatrixFloats, 0);
		return ret;
	}
	
	public static Matrix Perspective(float fov, int width, int height, float zNear, float zFar) {
		Matrix ret = new Matrix();
		
		android.opengl.Matrix.perspectiveM(ret.mMatrixFloats, 0, fov, ((float)width / height), zNear, zFar);
		
		return ret;
	}
	
	public static Matrix LookAt(Vector3 eye, Vector3 target, Vector3 up) {
		Matrix ret = new Matrix();
		
		android.opengl.Matrix.setLookAtM(ret.mMatrixFloats, 0, eye.x, eye.y, eye.z, target.x, target.y, target.z, up.x, up.y, up.z);
		
		return ret;
	}
	
	public float[] getFloats() { return mMatrixFloats; }
	
	private float[] mMatrixFloats = new float[16];
}

package de.cromon.math;

public class ViewFrustum {
	public ViewFrustum() {
		for(int i = 0; i < 6; ++i) {
			mPlanes[i] = new Plane();
		}
	}
	
	public void updateFrustum(Matrix mat) {
		mPlanes[4].setCoeffs(mat.get(2, 0) + mat.get(3, 0),
				mat.get(2, 1) + mat.get(3, 1),
				mat.get(2, 2) + mat.get(3, 2),
				mat.get(2, 3) + mat.get(3, 3));
		
		mPlanes[5].setCoeffs(-mat.get(2, 0) + mat.get(3, 0),
				-mat.get(2, 1) + mat.get(3, 1),
				-mat.get(2, 2) + mat.get(3, 2),
				-mat.get(2, 3) + mat.get(3, 3));
		
		mPlanes[1].setCoeffs(mat.get(1, 0) + mat.get(3, 0),
				mat.get(1, 1) + mat.get(3, 1),
				mat.get(1, 2) + mat.get(3, 2),
				mat.get(1, 3) + mat.get(3, 3));
		
		mPlanes[0].setCoeffs(-mat.get(1, 0) + mat.get(3, 0),
				-mat.get(1, 1) + mat.get(3, 1),
				-mat.get(1, 2) + mat.get(3, 2),
				-mat.get(1, 3) + mat.get(3, 3));
		
		mPlanes[2].setCoeffs(mat.get(0, 0) + mat.get(3, 0),
				mat.get(0, 1) + mat.get(3, 1),
				mat.get(0, 2) + mat.get(3, 2),
				mat.get(0, 3) + mat.get(3, 3));
		
		mPlanes[3].setCoeffs(-mat.get(0, 0) + mat.get(3, 0),
				-mat.get(0, 1) + mat.get(3, 1),
				-mat.get(0, 2) + mat.get(3, 2),
				-mat.get(0, 3) + mat.get(3, 3));
	}
	
	public Plane[] mPlanes = new Plane[6];
}

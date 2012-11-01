package de.cromon.math;

public class ViewFrustum {
	public ViewFrustum() {
		for(int i = 0; i < 6; ++i) {
			mPlanes[i] = new Plane();
		}
	}
	
	private void extractPlane(Plane plane, Matrix mat, int row) {
		int scale = (row < 0) ? -1 : 1;
		row = Math.abs(row) - 1;
		
		float[] m = mat.getFloats();
		
		plane.setCoeffs(
				m[3] + scale * m[row],
				m[7] + scale * m[row + 4],
				m[11] + scale * m[row + 8],
				m[15] + scale * m[row + 12]);
		
		float len = plane.normal.length();
		plane.normal.normalize();
		plane.d /= len;
	}
	
	public ContainmentType test(AABox box) {
		ContainmentType result = ContainmentType.Inside;
		
		int nOut = 0, nIn = 0;
		
		for(int i = 0; i < 6; ++i) {
			nOut = nIn = 0;
			
			for(int k = 0; k < 8 && (nIn == 0 || nOut == 0); ++k) {
				if(mPlanes[i].distance(box.corners[k]) < 0)
					++nOut;
				else
					++nIn;
			}
			
			if(nIn == 0)
				return ContainmentType.Outside;
			else if(nOut != 0)
				result = ContainmentType.Intersects;
		}
		
		return result;
	}
	
	public void updateFrustum(Matrix mat) {
		extractPlane(mPlanes[0], mat, 1);
		extractPlane(mPlanes[1], mat, -1);
		extractPlane(mPlanes[2], mat, 2);
		extractPlane(mPlanes[3], mat, -2);
		extractPlane(mPlanes[4], mat, 3);
		extractPlane(mPlanes[5], mat, -3);
	}
	
	public Plane[] mPlanes = new Plane[6];
}

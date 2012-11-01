package de.cromon.graphics;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import android.opengl.GLES20;

public class VertexBuffer {

	public VertexBuffer() {
	}
	
	/**
	 * Adds an element describing a portion of a vertex.
	 * @param elem The element describing the vertex.
	 */
	public void addElement(VertexElement elem) {
		mElements.add(elem);
		
		java.util.Collections.sort(mElements, new Comparator<VertexElement>() {
			@Override
			public int compare(VertexElement lhs, VertexElement rhs) {
				if(lhs.getSemantic().getValue() < rhs.getSemantic().getValue())
					return -1;
				
				if(lhs.getSemantic().getValue() > rhs.getSemantic().getValue())
					return 1;
				
				return 0;
			}
		});
	}
	
	public void bindToTarget(ShaderProgram program) {
		for(VertexElement elem : mElements) {
			elem.getStream().position(0);
			int index = program.getAttributeIndex(elem.getSemantic(), elem.getSemanticIndex());
			GLES20.glEnableVertexAttribArray(index);
			
			GLES20.glVertexAttribPointer(index, elem.getNumComponents(),
					elem.getElementType(), elem.isNormalized(), 0, elem.getStream());
		}
	}
	
	public void unbindBuffer(ShaderProgram program) {
		for(VertexElement elem : mElements) {
			int index = program.getAttributeIndex(elem.getSemantic(), elem.getSemanticIndex());
			GLES20.glDisableVertexAttribArray(index);
		}
	}
	
	private List<VertexElement> mElements = new LinkedList<VertexElement>();
}

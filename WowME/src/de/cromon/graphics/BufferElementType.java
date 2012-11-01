package de.cromon.graphics;

public enum BufferElementType {
	ColorDword (0),
	PositionFloat (1);
	
	public int getValue() {
		return mValue;
	}
	
	private int mValue;
	
	private BufferElementType(int value) {
		mValue = value;
	}
}

package de.cromon.graphics;

public enum Semantic {
	Position (0),
	Color (1),
	TexCoord (2),
	Normal (3);
			
	
	public int getValue() {
		return mValue;
	}
	
	private int mValue;
	
	private Semantic(int value) {
		mValue = value;
	}
}

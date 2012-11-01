package de.cromon.graphics;

public enum Colors {
	Blue(0xFFFF0000),
	Green(0xFF00FF00),
	Red(0xFF0000FF);
	
	private int mValue;
	
	public int getValue() {
		return mValue;
	}
	
	private Colors(int value) {
		mValue = value;
	}
}

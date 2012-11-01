package de.cromon.adt;

import de.cromon.io.LittleEndianStream;

public class MHDR {
	public int Flags, ofsMCIN, ofsMTEX, ofsMMDX, ofsMMID, ofsMWMO, ofsMWID, ofsMDDF, ofsMODF;
	public int ofsMFBO, ofsMH2O, ofsMTFX;
	
	public static final int Size = 16 * 4;
	
	public MHDR(LittleEndianStream hdrChunk) {
		Flags = hdrChunk.readInt();
		ofsMCIN = hdrChunk.readInt();
		ofsMTEX = hdrChunk.readInt();
		ofsMMDX = hdrChunk.readInt();
		ofsMMID = hdrChunk.readInt();
		ofsMWMO = hdrChunk.readInt();
		ofsMWID = hdrChunk.readInt();
		ofsMDDF = hdrChunk.readInt();
		ofsMODF = hdrChunk.readInt();
		ofsMFBO = hdrChunk.readInt();
		ofsMH2O = hdrChunk.readInt();
		ofsMTFX = hdrChunk.readInt();
		hdrChunk.readInt();
		hdrChunk.readInt();
		hdrChunk.readInt();
		hdrChunk.readInt();
	}
}

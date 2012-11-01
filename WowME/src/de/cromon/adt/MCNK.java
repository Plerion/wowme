package de.cromon.adt;

import de.cromon.io.LittleEndianStream;

public class MCNK {

    public int flags, indexX, indexY, nLayers, nDoodadRefs;
    public int ofsHeight, ofsNormal, ofsLayer, ofsRefs, ofsAlpha;
    public int sizeAlpha, ofsShadow, sizeShadow, areaId, nMapObjRefs;
    public int holes;
    public long lq1, lq2;
    public int predTex, noEffectDoodad, ofsSndEmitter, nSndEmitters;
    public int ofsLiquid, sizeLiquid;
    public float baseX, baseY, baseZ;
    public int ofsMCCV, ofsMCLV, unused;
    
    public MCNK(LittleEndianStream strm) {
    	flags = strm.readInt();
    	indexX = strm.readInt();
    	indexY = strm.readInt();
    	nLayers = strm.readInt();
    	nDoodadRefs = strm.readInt();
    	ofsHeight = strm.readInt();
    	ofsNormal = strm.readInt();
    	ofsLayer = strm.readInt();
    	ofsRefs = strm.readInt();
    	ofsAlpha = strm.readInt();
    	sizeAlpha = strm.readInt();
    	ofsShadow = strm.readInt();
    	sizeShadow = strm.readInt();
    	areaId = strm.readInt();
    	nMapObjRefs = strm.readInt();
    	holes = strm.readInt();
    	lq1 = strm.readLong();
    	lq2 = strm.readLong();
    	predTex = strm.readInt();
    	noEffectDoodad = strm.readInt();
    	ofsSndEmitter = strm.readInt();
    	nSndEmitters = strm.readInt();
    	ofsLiquid = strm.readInt();
    	sizeLiquid = strm.readInt();
    	baseX = strm.readFloat();
    	baseY = strm.readFloat();
    	baseZ = strm.readFloat();
    	ofsMCCV = strm.readInt();
    	ofsMCLV = strm.readInt();
    	unused = strm.readInt();
    }
}

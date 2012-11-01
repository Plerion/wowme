package de.cromon.adt;

import android.opengl.GLES20;
import de.cromon.graphics.IndexBuffer;
import de.cromon.graphics.ShaderProgram;
import de.cromon.graphics.VertexBuffer;

public class MapChunkRender {
	public MapChunkRender(ADTChunkData data) {
		mVertexBuffer.addElement(data.getPositionElement());
		mVertexBuffer.addElement(data.getNormalElement());
	}
	
	public void renderChunk(ShaderProgram shader) {
		mVertexBuffer.bindToTarget(shader);
		
		mChunkIndexBuffer.drawToTarget(GLES20.GL_TRIANGLES);
		
		mVertexBuffer.unbindBuffer(shader);
	}
	
	private VertexBuffer mVertexBuffer = new VertexBuffer();
	
	private static IndexBuffer mChunkIndexBuffer = new IndexBuffer(768);
	
	static {
		short[][] indices = new short[64][12];
        for (short i = 0; i < 8; ++i)
        {
            for (short j = 0; j < 8; ++j)
            {
                short topLeft = (short)(i * 17 + j);
                short midPoint = (short)(i * 17 + j + 9);
                short topRight = (short)(i * 17 + j + 1);
                short bottomRight = (short)(i * 17 + j + 18);
                short bottomLeft = (short)((i + 1) * 17 + j);
                indices[i * 8 + j][0] = topLeft;
                indices[i * 8 + j][1] = midPoint;
                indices[i * 8 + j][2] = bottomLeft;
                indices[i * 8 + j][3] = topLeft;
                indices[i * 8 + j][4] = midPoint;
                indices[i * 8 + j][5] = topRight;
                indices[i * 8 + j][6] = topRight;
                indices[i * 8 + j][7] = midPoint;
                indices[i * 8 + j][8] = bottomRight;
                indices[i * 8 + j][9] = bottomRight;
                indices[i * 8 + j][10] = midPoint;
                indices[i * 8 + j][11] = bottomLeft;
            }
        }
        
        short[] realIndices = new short[768];

        for (short i = 0; i < 64; ++i)
            for (short j = 0; j < 12; ++j)
                realIndices[i * 12 + j] = (short)(indices[i][j]);
        
        mChunkIndexBuffer.getStream().put(realIndices);
        
	}
}

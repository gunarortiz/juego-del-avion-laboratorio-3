package com.gunar.lab3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

public class RectanguloGrafico {
	
	private float vertices[] = new float[] {
		 0, 0, // 0
		 1, 0, // 1
		 1, 3, // 2
		 0, 3, // 3
	};
	
	private short indices[] = new short [] { 
		 0, 1, 
		 2, 0,
		 2, 3,
	};
	
	FloatBuffer bufVertices;
	ShortBuffer bufIndices;
	
	public RectanguloGrafico(){
		ByteBuffer bufByte = ByteBuffer.allocateDirect(vertices.length * 4);
		bufByte.order(ByteOrder.nativeOrder());
		bufVertices = bufByte.asFloatBuffer();
		bufVertices.put(vertices).rewind();

		bufByte = ByteBuffer.allocateDirect(indices.length * 2);
		bufByte.order(ByteOrder.nativeOrder());
		bufIndices = bufByte.asShortBuffer();
		bufIndices.put(indices).rewind();
	}
	
	public void dibuja(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, bufVertices);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, bufIndices);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}

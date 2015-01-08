package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Axis {
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;
	private Vertex axis[] = new Vertex[6];
	
	private float vertices[] = new float[6*3];
	private float colors[] = new float[6*4];

	public Axis() {
		float sp[] = {0f, 0f, 0f};
		setVertex(10f, sp);		
	}
	
	public Axis(float[] pos) {
		setVertex(1f, pos);		
	}

	public void draw(GL10 gl) {		
		//Set the face rotation
		gl.glFrontFace(GL10.GL_CW);
		
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		
		//Enable the vertex and color state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		//gl.glDrawElements(GL10.GL_LINES, 36, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		gl.glDrawArrays(GL10.GL_LINES, 0, vertices.length/3);
		
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		
	}	
	
	private void setVertex(float n, float[] pos) {
		
		for(int i=0; i<axis.length; i++) {
			axis[i] = new Vertex();
		}
		
		axis[0].setX(0f+pos[0]);	axis[0].setY(0f+pos[1]);	axis[0].setZ(0f+pos[2]);
		axis[1].setX(n+pos[0]);		axis[1].setY(0f+pos[1]);	axis[1].setZ(0f+pos[2]);
		axis[2].setX(0f+pos[0]);	axis[2].setY(0f+pos[1]);	axis[2].setZ(0f+pos[2]);
		axis[3].setX(0f+pos[0]);	axis[3].setY(n+pos[1]);		axis[3].setZ(0f+pos[2]);
		axis[4].setX(0f+pos[0]);	axis[4].setY(0f+pos[1]);	axis[4].setZ(0f+pos[2]);
		axis[5].setX(0f+pos[0]);	axis[5].setY(0f+pos[1]);	axis[5].setZ(n+pos[2]);
		axis[0].setR(1f);	axis[1].setR(1f);
		axis[2].setG(1f);	axis[3].setG(1f);
		axis[4].setB(1f);	axis[5].setB(1f);
		
		//set
		for(int i=0,k=0; i<6; i++,k+=3) {
			vertices[k]=axis[i].getX();
			vertices[k+1]=axis[i].getY();
			vertices[k+2]=axis[i].getZ();
		}		
		for(int i=0, k=0; i<6; i++, k+=4) {			
			colors[k]=axis[i].getR();
			colors[k+1]=axis[i].getG();
			colors[k+2]=axis[i].getB();
			colors[k+3]=axis[i].getAlpha();
		}
			
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);	
	}
}

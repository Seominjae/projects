package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Background {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private int tex_id[];
	

	public Background(GL10 gl, Bitmap bmp) {
		tex_id = new int[1];		
		gl.glGenTextures(1, tex_id, 0);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);	
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);		
		
		setVertex();
	}
	
	public void draw(GL10 gl) {		
		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		//Point to our buffers
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		//Enable the vertex and color state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glEnable(GL10.GL_BLEND);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_id[0]);
		
		//Draw the vertices as triangles, based on the Index Buffer information
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);			
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);					
		
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
	}	
	
	private void setVertex() {
		
		float vertices[] = { -60f, -40f, 50f, 
							 -60f, -40f, -50f, 
							 -60f, 40f, 50f,
							 -60f, 40f, -50f };
		
		float texturecoords[]= {0f, 0f, 0f, 1f, 1f, 0f, 1f, 1f};
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(texturecoords.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texturecoords);
		textureBuffer.position(0);
	}
}

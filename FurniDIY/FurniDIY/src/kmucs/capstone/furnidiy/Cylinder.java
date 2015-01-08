package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

public class Cylinder extends Prism {
	

	
	public Cylinder(float[] pos, Prism SquarePrism, EditRenderer renderer, float x, float y, float z) {
		super(20);
		
		
		// SquarePrism.getCalculatedVertex()[0].getX(), SquarePrism.getCalculatedVertex()[0].getY(), SquarePrism.getCalculatedVertex()[0].getZ()
		Log.d("pos", "pos : " + pos[0] + " " + pos[1] + " " +pos[2]);
		//Log.d("squarePrism", "squarePrism : " +)
		Log.d("xyz", "xyz : " + x +" "+ y +" "+ z);
		
		setMovingPoint2(1, pos[0]);
		setMovingPoint2(2, pos[1]);
		setMovingPoint2(3, pos[2]);
		
		
		Log.d("this.movePoints2", "this.movePoints2 : " + this.movePoints2[0] + " "+this.movePoints2[1] + " " + this.movePoints2[2]);
//		setMovingPoint(1, x);
//		setMovingPoint(2, y);
//		setMovingPoint(3, z);
		
		this.renderer=renderer;
		setPtVertex(0.1f);		
	}
	
	public Cylinder(float[] pos,EditRenderer renderer) {
		super(20);
		
		
		// SquarePrism.getCalculatedVertex()[0].getX(), SquarePrism.getCalculatedVertex()[0].getY(), SquarePrism.getCalculatedVertex()[0].getZ()
		
		setMovingPoint(1, pos[0]);
		setMovingPoint(2, pos[1]);
		setMovingPoint(3, pos[2]);
		
		this.renderer=renderer;
		setVertex();		
	}
	
	public Cylinder(Prism hPrism,EditRenderer renderer) {
		super(20);
		
		for(int i=0; i<3; i++) {
			this.rotationAngle[i] = hPrism.rotationAngle[i];
			this.movePoints[i] = hPrism.movePoints[i];
		}
		this.renderer=renderer;
		setVertex();
	}
	
	@Override
	public void setVertex() {
		for(int i=0; i<vertex.length; i++) {
			vertex[i] = new Vertex();
			
			if(i<vertex.length/2) {
				double theta = 2.0 * Math.PI * (double)i / (double)(vertex.length/2);
				vertex[i].setX((float)Math.cos(theta));
				vertex[i].setY((float)Math.sin(theta));
				vertex[i].setZ(-1f);
			}
			else {
				double theta = 2.0 * Math.PI * (double)(i-vertex.length) / (double)(vertex.length/2);
				vertex[i].setX((float)Math.cos(theta));
				vertex[i].setY((float)Math.sin(theta));
				vertex[i].setZ(1f);
			}
		}
		
		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {0,1,20,21, 21,1,22,2, 2,3,22,23, 23,3,24,4, 4,5,24,25, 25,5,26,6, 6,7,26,27,
							27,7,28,8, 8,9,28,29, 29,9,30,10, 10,11,30,31, 31,11,32,12, 12,13,32,33, 
							33,13,34,14, 14,15,34,35, 35,15,36,16, 16,17,36,37, 37,17,38,18, 18,19,38,39, 39,19,20,0,
							0,1,19,2,18,3,17,4,16,5,15,6,14,7,13,8,12,9,11,10, 10,20, 
							20,21,39,22,38,23,37,24,36,25,35,26,34,27,33,28,32,29,31,30};
		
		for(int i=0, k=0; i<tmpIndices.length; i++, k+=3) {
			vertices[k] = vertex[tmpIndices[i]].getX();
			vertices[k+1] = vertex[tmpIndices[i]].getY();
			vertices[k+2] = vertex[tmpIndices[i]].getZ();
		}
		
		for(int i=0, k=0; i<6; i++, k+=4) {
			colors[k]=vertex[i].getR();
			colors[k+1]=vertex[i].getG();
			colors[k+2]=vertex[i].getB();
			colors[k+3]=vertex[i].getAlpha();
		}
		
		float texturecoords[] = new float[tmpIndices.length*2];
		int tmpIndex;
		
		for(int i=0; i<vertex.length/4; i++) {
			texturecoords[i*16] = 0f;
			texturecoords[i*16+1] = 1f;
			texturecoords[i*16+2] = 1f;
			texturecoords[i*16+3] = 1f;
			texturecoords[i*16+4] = 0f;
			texturecoords[i*16+5] = 0f;
			texturecoords[i*16+6] = 1f;
			texturecoords[i*16+7] = 0f;
			texturecoords[i*16+8] = 1f;
			texturecoords[i*16+9] = 1f;
			texturecoords[i*16+10] = 0f;
			texturecoords[i*16+11] = 1f;
			texturecoords[i*16+12] = 1f;
			texturecoords[i*16+13] = 0f;
			texturecoords[i*16+14] = 0f;
			texturecoords[i*16+15] = 0f;
		}
		
		tmpIndex = 16*10;
		
		for(int i=0; i<20; i++) {
			double theta = 2.0 * Math.PI * (double)i / (double)(20);
			texturecoords[tmpIndex+i*2] = (float)Math.cos(theta);
			texturecoords[tmpIndex+i*2+1] = (float)Math.sin(theta);
		}
		
		tmpIndex += 40;
		
		texturecoords[tmpIndex] = texturecoords[tmpIndex-2];
		texturecoords[tmpIndex+1] = texturecoords[tmpIndex-1];
		texturecoords[tmpIndex+2] = 1.0f;
		texturecoords[tmpIndex+3] = 0f;
		
		tmpIndex += 4;
		
		for(int i=0; i<20; i++) {
			double theta = 2.0 * Math.PI * (double)i / (double)(20);
			texturecoords[tmpIndex+i*2] = (float)Math.cos(theta);
			texturecoords[tmpIndex+i*2+1] = (float)Math.sin(theta);
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

		byteBuf = ByteBuffer.allocateDirect(texturecoords.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texturecoords);
		textureBuffer.position(0);
		
		setTransparencyVertex();
	}
	
	public void setPtVertex(float size) {
		for(int i=0; i<vertex.length; i++) {
			vertex[i] = new Vertex();
			
			if(i<vertex.length/2) {
				double theta = 2.0 * Math.PI * (double)i / (double)(vertex.length/2);
				vertex[i].setX((float)Math.cos(theta)*0.4f);
				vertex[i].setY((float)Math.sin(theta)*0.4f);
				vertex[i].setZ(-1f/2);
			}
			else {
				double theta = 2.0 * Math.PI * (double)(i-vertex.length) / (double)(vertex.length/2);
				vertex[i].setX((float)Math.cos(theta)*0.4f);
				vertex[i].setY((float)Math.sin(theta)*0.4f);
				vertex[i].setZ(1f/2);
			}
		}
		
		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {0,1,20,21, 21,1,22,2, 2,3,22,23, 23,3,24,4, 4,5,24,25, 25,5,26,6, 6,7,26,27,
							27,7,28,8, 8,9,28,29, 29,9,30,10, 10,11,30,31, 31,11,32,12, 12,13,32,33, 
							33,13,34,14, 14,15,34,35, 35,15,36,16, 16,17,36,37, 37,17,38,18, 18,19,38,39, 39,19,20,0,
							0,1,19,2,18,3,17,4,16,5,15,6,14,7,13,8,12,9,11,10, 10,20, 
							20,21,39,22,38,23,37,24,36,25,35,26,34,27,33,28,32,29,31,30};
		
		for(int i=0, k=0; i<tmpIndices.length; i++, k+=3) {
			vertices[k] = vertex[tmpIndices[i]].getX();
			vertices[k+1] = vertex[tmpIndices[i]].getY();
			vertices[k+2] = vertex[tmpIndices[i]].getZ();
		}
		
		for(int i=0, k=0; i<6; i++, k+=4) {
			colors[k]=vertex[i].getR();
			colors[k+1]=vertex[i].getG();
			colors[k+2]=vertex[i].getB();
			colors[k+3]=vertex[i].getAlpha();
		}
		
		float texturecoords[] = new float[tmpIndices.length*2];
		int tmpIndex;
		
		for(int i=0; i<vertex.length/4; i++) {
			texturecoords[i*16] = 0f*size;
			texturecoords[i*16+1] = 1f*size;
			texturecoords[i*16+2] = 1f*size;
			texturecoords[i*16+3] = 1f*size;
			texturecoords[i*16+4] = 0f*size;
			texturecoords[i*16+5] = 0f*size;
			texturecoords[i*16+6] = 1f*size;
			texturecoords[i*16+7] = 0f*size;
			texturecoords[i*16+8] = 1f*size;
			texturecoords[i*16+9] = 1f*size;
			texturecoords[i*16+10] = 0f*size;
			texturecoords[i*16+11] = 1f*size;
			texturecoords[i*16+12] = 1f*size;
			texturecoords[i*16+13] = 0f*size;
			texturecoords[i*16+14] = 0f*size;
			texturecoords[i*16+15] = 0f*size;
		}
		
		tmpIndex = 16*10;
		
		for(int i=0; i<20; i++) {
			double theta = 2.0 * Math.PI * (double)i / (double)(20);
			texturecoords[tmpIndex+i*2] = (float)Math.cos(theta);
			texturecoords[tmpIndex+i*2+1] = (float)Math.sin(theta);
		}
		
		tmpIndex += 40;
		
		texturecoords[tmpIndex] = texturecoords[tmpIndex-2];
		texturecoords[tmpIndex+1] = texturecoords[tmpIndex-1];
		texturecoords[tmpIndex+2] = 1.0f;
		texturecoords[tmpIndex+3] = 0f;
		
		tmpIndex += 4;
		
		for(int i=0; i<20; i++) {
			double theta = 2.0 * Math.PI * (double)i / (double)(20);
			texturecoords[tmpIndex+i*2] = (float)Math.cos(theta);
			texturecoords[tmpIndex+i*2+1] = (float)Math.sin(theta);
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

		byteBuf = ByteBuffer.allocateDirect(texturecoords.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texturecoords);
		textureBuffer.position(0);
		
		setTransparencyVertex();
	}
	@Override
	protected boolean checkPick(float[] result) {
		// TODO Auto-generated method stub
		float r=0;
		float r2=0;
		
		float x = getMovingPoints()[0]-this.vertex[4].getX();
		float y = getMovingPoints()[1]-this.vertex[4].getY();
		float z = getMovingPoints()[2]-this.vertex[4].getZ();
		
		Log.d("x", "x : " + x);
		Log.d("y", "y : " + y);
		Log.d("z", "z : " + z);
		
		r=x*x+y*y+z*z;
		
		Log.d("r", "r : " + r);
		float x2 = getMovingPoints()[0]-result[0];
		float y2 = getMovingPoints()[1]-result[1];
		float z2 = getMovingPoints()[2]-result[2];
		
		Log.d("x2", "x2 : " + x2);
		Log.d("y2", "y2 : " + y2);
		Log.d("z2", "z2 : " + z2);
		
		r2=x2*x2+y2*y2+z2*z2;
		Log.d("r2", "r2 : " + r2);
		
		Log.d("r-r2", "r-r2 : " + (r-r2));
		if(r2<r)
			return true;
		
		return false;
	}
	private float dist(Vertex a,float[] result)
	{
//		setMovingPoint2(1, pos[0]);
//		setMovingPoint2(2, pos[1]);
//		setMovingPoint2(3, pos[2]);
		
		return (float)1;
	}
}

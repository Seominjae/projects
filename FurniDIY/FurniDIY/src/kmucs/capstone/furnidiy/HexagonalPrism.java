package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HexagonalPrism extends Prism{

	
	public HexagonalPrism() {
		super(6);
		
		setVertex();		
	}
	
	public HexagonalPrism(float[] pos,EditRenderer renderer) {
		super(6);
		
		setMovingPoint(1, pos[0]);
		setMovingPoint(2, pos[1]);
		setMovingPoint(3, pos[2]);
		this.renderer=renderer;
		setVertex();		
	}
	public HexagonalPrism(float[] pos, Vertex[] vPos,EditRenderer renderer) {
		super(6);
		
		setMovingPoint(1, 0.0f);//pos[0]);
		setMovingPoint(2, 0.0f);//pos[1]);
		setMovingPoint(3, 0.0f);//pos[2]);
		this.renderer=renderer;
		setVertex(vPos);		
	}
	public HexagonalPrism(Prism hPrism,EditRenderer renderer) {
		super(6);
		
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
		}
		vertex[0].setX((float)(-1*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[0].setY(1f);	vertex[0].setZ(-1f);
		vertex[1].setX(-1f);	vertex[1].setY(0f);	vertex[1].setZ(-1f);
		vertex[2].setX((float)(-1*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[2].setY(-1f);	vertex[2].setZ(-1f);
		vertex[3].setX((float)(Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[3].setY(-1f);	vertex[3].setZ(-1f);			
		vertex[4].setX(1f);		vertex[4].setY(0f);	vertex[4].setZ(-1f);
		vertex[5].setX((float)(Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[5].setY(1f);	vertex[5].setZ(-1f);		
		vertex[6].setX((float)(-1*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[6].setY(1f);	vertex[6].setZ(+1f);
		vertex[7].setX(-1f);	vertex[7].setY(0f);	vertex[7].setZ(+1f);
		vertex[8].setX((float)(-1*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[8].setY(-1f);	vertex[8].setZ(+1f);
		vertex[9].setX((float)(Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[9].setY(-1f);	vertex[9].setZ(+1f);			
		vertex[10].setX(1f);	vertex[10].setY(0f);	vertex[10].setZ(+1f);
		vertex[11].setX((float)(Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))));	vertex[11].setY(+1f);	vertex[11].setZ(+1f);
		
		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {1,2,7,8, 8,2,9,3, 3,4,9,10, 10,4,11,5, 5,0,11,6, 6,0,7,1, 1,2,0,3,5,4, 4,7, 7,8,6,9,11,10};
		
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
		
		float texturecoords[]= {0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
				1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
				0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
				1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
				0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
				1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
				0f, 0.5f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				1f, 0.5f,
				1f, 0.5f, 0f, 0.5f,
				0f, 0.5f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				1f, 0.5f};
		
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
	public void setVertex(Vertex[] vPos) {
		for(int i=0; i<vertex.length; i++) {
			vertex[i] = new Vertex();
		}
		for(int i=0; i<vertex.length; i++) {
			vertex[i].setX(vPos[i].getX());	
			vertex[i].setY(vPos[i].getY());	
			vertex[i].setZ(vPos[i].getZ());
		}
		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {1,2,7,8, 8,2,9,3, 3,4,9,10, 10,4,11,5, 5,0,11,6, 6,0,7,1, 1,2,0,3,5,4, 4,7, 7,8,6,9,11,10};
		
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
		float texturecoords[]= {0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
				1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
				0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
				1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
				0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
				1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
				0f, 0.5f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				1f, 0.5f,
				1f, 0.5f, 0f, 0.5f,
				0f, 0.5f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5-0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 0f,
				(float)(0.5+0.5*Math.cos(Math.toRadians(60))/Math.sin(Math.toRadians(60))), 1f,
				1f, 0.5f};
		
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
}

package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PentagonalPrism extends Prism {

	
	public PentagonalPrism() {
		super(5);
		
		setVertex();		
	}
	
	public PentagonalPrism(float[] pos,EditRenderer renderer) {
		super(5);
		
		setMovingPoint(1, pos[0]);
		setMovingPoint(2, pos[1]);
		setMovingPoint(3, pos[2]);
		this.renderer=renderer;
		setVertex();		
	}
	
	public PentagonalPrism(Prism pPrism,EditRenderer renderer) {
		super(5);
		
		for(int i=0; i<3; i++) {
			this.rotationAngle[i] = pPrism.rotationAngle[i];
			this.movePoints[i] = pPrism.movePoints[i];
		}
		this.renderer=renderer;
		setVertex();
	}
	// yhchung
		public PentagonalPrism(float[] pos, Vertex[] vPos,EditRenderer renderer) {
			super(5);
			
			setMovingPoint(1, 0.0f);//pos[0]);
			setMovingPoint(2, 0.0f);//pos[1]);
			setMovingPoint(3, 0.0f);//pos[2]);
			this.renderer=renderer;
			setVertex(vPos);		
		}	
	@Override
	public void setVertex() {
		for(int i=0; i<vertex.length; i++) {
			vertex[i] = new Vertex();
		}
		vertex[0].setX((float)(1-Math.cos(Math.toRadians(72))/Math.cos(Math.toRadians(36))));	
		vertex[0].setY((float)(-1*Math.sin(Math.toRadians(72))/Math.cos(Math.toRadians(36))));	vertex[0].setZ(-1f);			
		vertex[1].setX(+1f);	vertex[1].setY(0f);		vertex[1].setZ(-1f);
		vertex[2].setX(0f);		vertex[2].setY((float)Math.tan(Math.toRadians(36)));	vertex[2].setZ(-1f);
		vertex[3].setX(-1f);	vertex[3].setY(0f);		vertex[3].setZ(-1f);			
		vertex[4].setX((float)(Math.cos(Math.toRadians(72))/Math.cos(Math.toRadians(36))-1));	
		vertex[4].setY((float)(-1*Math.sin(Math.toRadians(72))/Math.cos(Math.toRadians(36))));	vertex[4].setZ(-1f);			
		vertex[5].setX((float)(1-Math.cos(Math.toRadians(72))/Math.cos(Math.toRadians(36))));	
		vertex[5].setY((float)(-1*Math.sin(Math.toRadians(72))/Math.cos(Math.toRadians(36))));	vertex[5].setZ(+1f);
		vertex[6].setX(+1f);	vertex[6].setY(0f);		vertex[6].setZ(+1f);
		vertex[7].setX(0f);		vertex[7].setY((float)Math.tan(Math.toRadians(36)));	vertex[7].setZ(+1f);
		vertex[8].setX(-1f);	vertex[8].setY(0f);		vertex[8].setZ(+1f);
		vertex[9].setX((float)(Math.cos(Math.toRadians(72))/Math.cos(Math.toRadians(36))-1));	
		vertex[9].setY((float)(-1*Math.sin(Math.toRadians(72))/Math.cos(Math.toRadians(36))));	vertex[9].setZ(+1f);
		
		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {3,4,8,9, 9,4,5,0, 0,1,5,6, 6,1,7,2, 2,3,7,8, 8,9,7,5,6, 6,3, 3,4,2,0,1};
		
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
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    0f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    (float)(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))), 0f,
			    0.5f, 1f,
			    (float)(1-(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36))))), 0f,
			    1f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    1f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    0f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    0f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    (float)(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))), 0f,
			    0.5f, 1f,
			    (float)(1-(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36))))), 0f,
			    1f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36))))};
		
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
		int tmpIndices[] = {3,4,8,9, 9,4,5,0, 0,1,5,6, 6,1,7,2, 2,3,7,8, 8,9,7,5,6, 6,3, 3,4,2,0,1};
		
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
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    0f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    (float)(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))), 0f,
			    0.5f, 1f,
			    (float)(1-(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36))))), 0f,
			    1f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    1f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    0f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    0f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))),
			    (float)(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36)))), 0f,
			    0.5f, 1f,
			    (float)(1-(Math.cos(Math.toRadians(72)/2/Math.cos(Math.toRadians(36))))), 0f,
			    1f, (float)(Math.sin(Math.toRadians(72)/2/Math.cos(Math.toRadians(36))))};
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

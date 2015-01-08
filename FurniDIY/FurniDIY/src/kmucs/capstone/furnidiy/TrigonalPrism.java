package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TrigonalPrism extends Prism {
	
	public TrigonalPrism() {
		super(3);
		
		setVertex();		
	}
	
	public TrigonalPrism(float[] pos,EditRenderer renderer) {
		super(3);
		
		setMovingPoint(1, pos[0]);
		setMovingPoint(2, pos[1]);
		setMovingPoint(3, pos[2]);
		
		this.renderer=renderer;
		setVertex();		
	}
	
	public TrigonalPrism(Prism tPrism,EditRenderer renderer) {
		super(3);
		
		for(int i=0; i<3; i++) {
			this.rotationAngle[i] = tPrism.rotationAngle[i];
			this.movePoints[i] = tPrism.movePoints[i];
		}
		this.renderer=renderer;
		setVertex();
	}
	
	// yhchung
		public TrigonalPrism(float[] pos, Vertex[] vPos,EditRenderer renderer) {
			super(3);
			
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
		vertex[0].setX(0f);		vertex[0].setY((float)Math.sin(Math.toRadians(60)));	vertex[0].setZ(-1f);
		vertex[1].setX(-1f);	vertex[1].setY(-1*(float)Math.sin(Math.toRadians(60)));	vertex[1].setZ(-1f);
		vertex[2].setX(1f);		vertex[2].setY(-1*(float)Math.sin(Math.toRadians(60)));	vertex[2].setZ(-1f);
		vertex[3].setX(0f);		vertex[3].setY((float)Math.sin(Math.toRadians(60)));	vertex[3].setZ(+1f);
		vertex[4].setX(-1f);	vertex[4].setY(-1*(float)Math.sin(Math.toRadians(60)));	vertex[4].setZ(+1f);
		vertex[5].setX(1f);		vertex[5].setY(-1*(float)Math.sin(Math.toRadians(60)));	vertex[5].setZ(+1f);
		
		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {1,2,4,5, 5,2,3,0, 0,1,3,4, 4,5,3, 3,1, 1,2,0};
		
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
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 0.5f, 0f,
			    0.5f, 0f, 0f, 1f,
			    0f, 1f, 1f, 1f, 0.5f, 0f};
		
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
	// yhchung
	public void setVertex(Vertex[] vPos) {
		float texturecoords[]= {0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
				1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 0.5f, 0f,
			    0.5f, 0f, 0f, 1f,
			    0f, 1f, 1f, 1f, 0.5f, 0f};
			for(int i=0; i<vertex.length; i++) {
				vertex[i] = new Vertex();
			}
			
			for(int i=0; i<vertex.length; i++) {
				vertex[i].setX(vPos[i].getX());	
				vertex[i].setY(vPos[i].getY());	
				vertex[i].setZ(vPos[i].getZ());
			}
			
			//vertex order for GL_TRIANGLE_STRIP
			int tmpIndices[] = {1,2,4,5, 5,2,3,0, 0,1,3,4, 4,5,3, 3,1, 1,2,0};
			
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

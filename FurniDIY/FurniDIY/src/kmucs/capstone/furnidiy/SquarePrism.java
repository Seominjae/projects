package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.util.Log;

public class SquarePrism extends Prism {
	
	float rememberX=0;
	float rememberY=0;
	float rememberZ=0;
	public SquarePrism() {
		super(4);
		
		setVertex();		
	}

	public SquarePrism(float[] pos,Prism SquarePrism,EditRenderer renderer, float x, float y, float z) {
		super(4);
		

		
		// SquarePrism.getCalculatedVertex()[0].getX(), SquarePrism.getCalculatedVertex()[0].getY(), SquarePrism.getCalculatedVertex()[0].getZ()
		Log.d("pos", "pos : " + pos[0] + " " + pos[1] + " " +pos[2]);
		//Log.d("squarePrism", "squarePrism : " +)
		Log.d("xyz", "xyz : " + x +" "+ y +" "+ z);
		//this.createdCameraPosition = renderer.convert2dTo3d(touchX, touchY);
		
		setMovingPoint2(1, pos[0]);
		setMovingPoint2(2, pos[1]);
		setMovingPoint2(3, pos[2]);
		
		
		Log.d("this.movePoints2", "this.movePoints2 : " + this.movePoints2[0] + " "+this.movePoints2[1] + " " + this.movePoints2[2]);
		
		this.renderer=renderer;
		setPtVertex(0.6f);		
	}

	public SquarePrism(float[] pos,EditRenderer renderer,float[] result) {
		super(4);
		
		this.createdCameraPosition = result;
		setMovingPoint(1, pos[0]);
		setMovingPoint(2, pos[1]);
		setMovingPoint(3, pos[2]);
		this.renderer=renderer;
		setVertex();		
	}
	
	public SquarePrism(Prism sPrism,EditRenderer renderer,float[] result) {
		super(4);
		this.createdCameraPosition = result;
		for(int i=0; i<3; i++) {
			this.rotationAngle[i] = sPrism.rotationAngle[i];
			this.movePoints[i] = sPrism.movePoints[i];
		}
		this.renderer=renderer;
		setVertex();
	}
	
	public SquarePrism(float[] pos, Vertex[] vPos,EditRenderer renderer,float[] result) {
		super(4);
		this.createdCameraPosition = result;
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
		vertex[0].setX(-1f);	vertex[0].setY(-1f);	vertex[0].setZ(-1f);
		vertex[1].setX(+1f);	vertex[1].setY(-1f);	vertex[1].setZ(-1f);
		vertex[2].setX(+1f);	vertex[2].setY(+1f);	vertex[2].setZ(-1f);
		vertex[3].setX(-1f);	vertex[3].setY(+1f);	vertex[3].setZ(-1f);
		vertex[4].setX(-1f);	vertex[4].setY(-1f);	vertex[4].setZ(+1f);
		vertex[5].setX(+1f);	vertex[5].setY(-1f);	vertex[5].setZ(+1f);
		vertex[6].setX(+1f);	vertex[6].setY(+1f);	vertex[6].setZ(+1f);
		vertex[7].setX(-1f);	vertex[7].setY(+1f);	vertex[7].setZ(+1f);

		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {1,2,5,6, 6,2,7,3, 3,0,7,4, 4,0,5,1, 1,0,2,3, 3,5, 5,6,4,7};
		
		for(int i=0, k=0; i<tmpIndices.length; i++, k+=3) {
			vertices[k] = vertex[tmpIndices[i]].getX();
			vertices[k+1] = vertex[tmpIndices[i]].getY();
			vertices[k+2] = vertex[tmpIndices[i]].getZ();
		}		
		for(int i=0, k=0; i<8; i++, k+=4) {
			colors[k]=vertex[i].getR();
			colors[k+1]=vertex[i].getG();
			colors[k+2]=vertex[i].getB();
			colors[k+3]=vertex[i].getAlpha();
		}
		
		float texturecoords[]= {0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 0f, 0f, 1f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f};
		
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
		/*
		 *    4    7
		 *  5    6 
		 *  
		 *  
		 *  
		 *    0    3
		 *  1    2
		 */
		
		for(int i=0; i<vertex.length; i++) {
			vertex[i] = new Vertex();
		}
		
		for(int i=0; i<vertex.length; i++) {
			vertex[i].setX(vPos[i].getX());	
			vertex[i].setY(vPos[i].getY());	
			vertex[i].setZ(vPos[i].getZ());
		}

		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {1,2,5,6, 6,2,7,3, 3,0,7,4, 4,0,5,1, 1,0,2,3, 3,5, 5,6,4,7};
		
		for(int i=0, k=0; i<tmpIndices.length; i++, k+=3) {
			vertices[k] = vertex[tmpIndices[i]].getX();
			vertices[k+1] = vertex[tmpIndices[i]].getY();
			vertices[k+2] = vertex[tmpIndices[i]].getZ();
		}		
		for(int i=0, k=0; i<8; i++, k+=4) {
			colors[k]=vertex[i].getR();
			colors[k+1]=vertex[i].getG();
			colors[k+2]=vertex[i].getB();
			colors[k+3]=vertex[i].getAlpha();
		}
		float texturecoords[]= {0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 0f, 0f, 1f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f};
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
	public boolean checkPick(float[] result) {
		// TODO Auto-generated method stub
		//Log.d("movedPoints", "MovedPoint = x : "+getcalculatedMovePoints(calculatedMovePoints)[0] + " y : "+getcalculatedMovePoints(calculatedMovePoints)[1]+" z : " +getcalculatedMovePoints(calculatedMovePoints)[2]);
		
		//this.printOriginalVertex();
		
		
		setCalculatedVertex(this.vertex, getCalculatedMovePoints(calculatedMovePoints), getCalculatedRotationAngle(calculatedRotationAngle),dist);
		
		
		//Log.d("checkPick에서 호출", "checkPick에서 호출 ");
		//this.printCalculedVertex();
		//Log.d("calculatedVertex", "calculatedVertex : " + calculatedVertex[0].getX());
		
		
		boolean r = false;
		
		//result[1]
		r = findSign(findNormalVector(this.calculatedVertex[3],this.calculatedVertex[0],this.calculatedVertex[2]),result)
		&& findSign(findNormalVector(this.calculatedVertex[6],this.calculatedVertex[5],this.calculatedVertex[7]),result)
		&& findSign(findNormalVector(this.calculatedVertex[7],this.calculatedVertex[4],this.calculatedVertex[3]),result)
		&& findSign(findNormalVector(this.calculatedVertex[5],this.calculatedVertex[6],this.calculatedVertex[1]),result)
		&& findSign(findNormalVector(this.calculatedVertex[2],this.calculatedVertex[6],this.calculatedVertex[3]),result)
		&& findSign(findNormalVector(this.calculatedVertex[4],this.calculatedVertex[5],this.calculatedVertex[0]),result);
		
		
		if(r)
		{
			return r;
		}
//		else
//		{
//			boolean x=false,y=false,z=false;
//			
//			if((this.calculatedVertex[0].getY()<result[1])&&(this.calculatedVertex[1].getY()<result[1])&&(this.calculatedVertex[4].getY()<result[1])&&(this.calculatedVertex[5].getY()<result[1])&&
//			(this.calculatedVertex[2].getY()>result[1])&&(this.calculatedVertex[3].getY()>result[1])&&(this.calculatedVertex[6].getY()>result[1])&&(this.calculatedVertex[7].getY()>result[1]))
//				y=true;
//			
//			if((this.calculatedVertex[0].getZ()<result[2])&&(this.calculatedVertex[1].getZ()<result[2])&&(this.calculatedVertex[2].getZ()<result[2])&&(this.calculatedVertex[3].getZ()<result[2])&&
//					(this.calculatedVertex[4].getZ()>result[2])&&(this.calculatedVertex[5].getZ()>result[2])&&(this.calculatedVertex[6].getZ()>result[2])&&(this.calculatedVertex[7].getZ()>result[2]))
//						z=true;
//			
//			if(Math.abs(result[0]-this.calculatedVertex[0].getX())<this.calculatedVertex[1].getY()/2)
//				x=true;
//			
//			r=x&&y&&z;
//			
//			
//			
//			return r;
//		}
		
		return r;
	}
	
	@Override
	public void setCalculatedVertex(Vertex[] vertex, float[] move, float[] angle,float[] dist) { //여기에 카메라 무빙을 섞어서 만들면 될것같지않습니까???
		// TODO Auto-generated method stub
		for(int i=0; i<vertex.length; i++) {
			calculatedVertex[i] = new Vertex();
		}
		
		Log.d("vertex[0].getX()+move[0]" , "vertex[0].getX()+move[0] : " + vertex[0].getX()+move[0]);
		calculatedVertex[0].setX(vertex[0].getX()+move[0]);calculatedVertex[0].setY(vertex[0].getY()+move[1]);calculatedVertex[0].setZ(vertex[0].getZ()+move[2]);
		calculatedVertex[1].setX(vertex[1].getX()+move[0]);calculatedVertex[1].setY(vertex[1].getY()+move[1]);calculatedVertex[1].setZ(vertex[1].getZ()+move[2]);
		calculatedVertex[2].setX(vertex[2].getX()+move[0]);calculatedVertex[2].setY(vertex[2].getY()+move[1]);calculatedVertex[2].setZ(vertex[2].getZ()+move[2]);
		calculatedVertex[3].setX(vertex[3].getX()+move[0]);calculatedVertex[3].setY(vertex[3].getY()+move[1]);calculatedVertex[3].setZ(vertex[3].getZ()+move[2]);
		calculatedVertex[4].setX(vertex[4].getX()+move[0]);calculatedVertex[4].setY(vertex[4].getY()+move[1]);calculatedVertex[4].setZ(vertex[4].getZ()+move[2]);
		calculatedVertex[5].setX(vertex[5].getX()+move[0]);calculatedVertex[5].setY(vertex[5].getY()+move[1]);calculatedVertex[5].setZ(vertex[5].getZ()+move[2]);
		calculatedVertex[6].setX(vertex[6].getX()+move[0]);calculatedVertex[6].setY(vertex[6].getY()+move[1]);calculatedVertex[6].setZ(vertex[6].getZ()+move[2]);
		calculatedVertex[7].setX(vertex[7].getX()+move[0]);calculatedVertex[7].setY(vertex[7].getY()+move[1]);calculatedVertex[7].setZ(vertex[7].getZ()+move[2]);
		
		Log.d("dist적용x", "dist적용x");
		//printCalculedVertex();
		
		Log.d("dist", "dist : " + dist[0] + " " + dist[1]+ " " + dist[2]);
		
		Log.d("vertex[0].getX()+move[0]" , "vertex[0].getX()+move[0] : " + vertex[0].getX()+move[0]);
		Log.d("(vertex[0].getX()+move[0]+dist[0])", "(vertex[0].getX()+move[0]+dist[0])" + (vertex[0].getX()+move[0]+dist[0]));
		
//		calculatedVertex[0].setX2(vertex[0].getX()+move[0]+dist[0]);calculatedVertex[0].setY2(vertex[0].getY()+move[1]+dist[1] );calculatedVertex[0].setZ2(vertex[0].getZ()+move[2]+dist[2] );
//		calculatedVertex[1].setX2(vertex[1].getX()+move[0]+dist[0] );calculatedVertex[1].setY2(vertex[1].getY()+move[1]+dist[1] );calculatedVertex[1].setZ2(vertex[1].getZ()+move[2]+dist[2] );
//		calculatedVertex[2].setX2(vertex[2].getX()+move[0]+dist[0] );calculatedVertex[2].setY2(vertex[2].getY()+move[1]+dist[1] );calculatedVertex[2].setZ2(vertex[2].getZ()+move[2]+dist[2] );
//		calculatedVertex[3].setX2(vertex[3].getX()+move[0]+dist[0] );calculatedVertex[3].setY2(vertex[3].getY()+move[1]+dist[1] );calculatedVertex[3].setZ2(vertex[3].getZ()+move[2]+dist[2] );
//		calculatedVertex[4].setX2(vertex[4].getX()+move[0]+dist[0] );calculatedVertex[4].setY2(vertex[4].getY()+move[1]+dist[1] );calculatedVertex[4].setZ2(vertex[4].getZ()+move[2]+dist[2] );
//		calculatedVertex[5].setX2(vertex[5].getX()+move[0]+dist[0] );calculatedVertex[5].setY2(vertex[5].getY()+move[1]+dist[1] );calculatedVertex[5].setZ2(vertex[5].getZ()+move[2]+dist[2] );
//		calculatedVertex[6].setX2(vertex[6].getX()+move[0]+dist[0] );calculatedVertex[6].setY2(vertex[6].getY()+move[1]+dist[1] );calculatedVertex[6].setZ2(vertex[6].getZ()+move[2]+dist[2] );
//		calculatedVertex[7].setX2(vertex[7].getX()+move[0]+dist[0] );calculatedVertex[7].setY2(vertex[7].getY()+move[1]+dist[1] );calculatedVertex[7].setZ2(vertex[7].getZ()+move[2]+dist[2] );
		
		calculatedVertex[0].setX2(vertex[0].getX()+move[0]+dist[0]);calculatedVertex[0].setY2(vertex[0].getY()+move[1]-dist[1] );calculatedVertex[0].setZ2(vertex[0].getZ()+move[2]-dist[2] );
		calculatedVertex[1].setX2(vertex[1].getX()+move[0]+dist[0] );calculatedVertex[1].setY2(vertex[1].getY()+move[1]-dist[1] );calculatedVertex[1].setZ2(vertex[1].getZ()+move[2]-dist[2] );
		calculatedVertex[2].setX2(vertex[2].getX()+move[0]+dist[0] );calculatedVertex[2].setY2(vertex[2].getY()+move[1]-dist[1] );calculatedVertex[2].setZ2(vertex[2].getZ()+move[2]-dist[2] );
		calculatedVertex[3].setX2(vertex[3].getX()+move[0]+dist[0] );calculatedVertex[3].setY2(vertex[3].getY()+move[1]-dist[1] );calculatedVertex[3].setZ2(vertex[3].getZ()+move[2]-dist[2] );
		calculatedVertex[4].setX2(vertex[4].getX()+move[0]+dist[0] );calculatedVertex[4].setY2(vertex[4].getY()+move[1]-dist[1] );calculatedVertex[4].setZ2(vertex[4].getZ()+move[2]-dist[2] );
		calculatedVertex[5].setX2(vertex[5].getX()+move[0]+dist[0] );calculatedVertex[5].setY2(vertex[5].getY()+move[1]-dist[1] );calculatedVertex[5].setZ2(vertex[5].getZ()+move[2]-dist[2] );
		calculatedVertex[6].setX2(vertex[6].getX()+move[0]+dist[0] );calculatedVertex[6].setY2(vertex[6].getY()+move[1]-dist[1] );calculatedVertex[6].setZ2(vertex[6].getZ()+move[2]-dist[2] );
		calculatedVertex[7].setX2(vertex[7].getX()+move[0]+dist[0] );calculatedVertex[7].setY2(vertex[7].getY()+move[1]-dist[1] );calculatedVertex[7].setZ2(vertex[7].getZ()+move[2]-dist[2] );
		
//		calculatedVertex[0].setX2(vertex[0].getX()+move[0]-dist[0]);calculatedVertex[0].setY2(vertex[0].getY()+move[1]-dist[1] );calculatedVertex[0].setZ2(vertex[0].getZ()+move[2]-dist[2] );
//		calculatedVertex[1].setX2(vertex[1].getX()+move[0]-dist[0] );calculatedVertex[1].setY2(vertex[1].getY()+move[1]-dist[1] );calculatedVertex[1].setZ2(vertex[1].getZ()+move[2]-dist[2] );
//		calculatedVertex[2].setX2(vertex[2].getX()+move[0]-dist[0] );calculatedVertex[2].setY2(vertex[2].getY()+move[1]-dist[1] );calculatedVertex[2].setZ2(vertex[2].getZ()+move[2]-dist[2] );
//		calculatedVertex[3].setX2(vertex[3].getX()+move[0]-dist[0] );calculatedVertex[3].setY2(vertex[3].getY()+move[1]-dist[1] );calculatedVertex[3].setZ2(vertex[3].getZ()+move[2]-dist[2] );
//		calculatedVertex[4].setX2(vertex[4].getX()+move[0]-dist[0] );calculatedVertex[4].setY2(vertex[4].getY()+move[1]-dist[1] );calculatedVertex[4].setZ2(vertex[4].getZ()+move[2]-dist[2] );
//		calculatedVertex[5].setX2(vertex[5].getX()+move[0]-dist[0] );calculatedVertex[5].setY2(vertex[5].getY()+move[1]-dist[1] );calculatedVertex[5].setZ2(vertex[5].getZ()+move[2]-dist[2] );
//		calculatedVertex[6].setX2(vertex[6].getX()+move[0]-dist[0] );calculatedVertex[6].setY2(vertex[6].getY()+move[1]-dist[1] );calculatedVertex[6].setZ2(vertex[6].getZ()+move[2]-dist[2] );
//		calculatedVertex[7].setX2(vertex[7].getX()+move[0]-dist[0] );calculatedVertex[7].setY2(vertex[7].getY()+move[1]-dist[1] );calculatedVertex[7].setZ2(vertex[7].getZ()+move[2]-dist[2] );
		
//		calculatedVertex[0].setX2(vertex[0].getX()+move[0]);calculatedVertex[0].setY2(vertex[0].getY()+move[1]-dist[1] );calculatedVertex[0].setZ2(vertex[0].getZ()+move[2]-dist[2] );
//		calculatedVertex[1].setX2(vertex[1].getX()+move[0] );calculatedVertex[1].setY2(vertex[1].getY()+move[1]-dist[1] );calculatedVertex[1].setZ2(vertex[1].getZ()+move[2]-dist[2] );
//		calculatedVertex[2].setX2(vertex[2].getX()+move[0] );calculatedVertex[2].setY2(vertex[2].getY()+move[1]-dist[1] );calculatedVertex[2].setZ2(vertex[2].getZ()+move[2]-dist[2] );
//		calculatedVertex[3].setX2(vertex[3].getX()+move[0] );calculatedVertex[3].setY2(vertex[3].getY()+move[1]-dist[1] );calculatedVertex[3].setZ2(vertex[3].getZ()+move[2]-dist[2] );
//		calculatedVertex[4].setX2(vertex[4].getX()+move[0] );calculatedVertex[4].setY2(vertex[4].getY()+move[1]-dist[1] );calculatedVertex[4].setZ2(vertex[4].getZ()+move[2]-dist[2] );
//		calculatedVertex[5].setX2(vertex[5].getX()+move[0] );calculatedVertex[5].setY2(vertex[5].getY()+move[1]-dist[1] );calculatedVertex[5].setZ2(vertex[5].getZ()+move[2]-dist[2] );
//		calculatedVertex[6].setX2(vertex[6].getX()+move[0] );calculatedVertex[6].setY2(vertex[6].getY()+move[1]-dist[1] );calculatedVertex[6].setZ2(vertex[6].getZ()+move[2]-dist[2] );
//		calculatedVertex[7].setX2(vertex[7].getX()+move[0] );calculatedVertex[7].setY2(vertex[7].getY()+move[1]-dist[1] );calculatedVertex[7].setZ2(vertex[7].getZ()+move[2]-dist[2] );
		
		Log.d("dist적용", "dist적용");
		printCalculedVertex();
		this.calued = true;
	}
	
	public void setVertexDependOnCamera(double horizontal, double radius, double vertical, float eyeX, float eyeY, float eyeZ, float upX, float upY, float upZ, float x, float y) {
		// TODO Auto-generated method stub
		//double horizontal, double radius, double vertical, float eyeX, float eyeY, float eyeZ, float upX, float upY, float upZ) {
		
//		Log.d("horizontal", "horizontal : " + horizontal);
//		Log.d("radius", "radius : " + radius);
//		Log.d("vertical", "vertical : " + vertical);
//		
//		Log.d("eyeX", "eyeX : " + eyeX);
//		Log.d("eyeY", "eyeY : " + eyeY);
//		Log.d("eyeZ", "eyeZ : " + eyeZ);
//		
//		Log.d("upX", "upX : " + upX);
//		Log.d("upY", "upY : " + upY);
//		Log.d("upZ", "upZ : " + upZ);
		
//		calculatedVertex[0].setX(vertex[0].getX()+eyeX);calculatedVertex[0].setY(vertex[0].getY()+eyeY);calculatedVertex[0].setZ(vertex[0].getZ()+eyeZ);
//		calculatedVertex[1].setX(vertex[1].getX()+eyeX);calculatedVertex[1].setY(vertex[1].getY()+eyeY);calculatedVertex[1].setZ(vertex[1].getZ()+eyeZ);
//		calculatedVertex[2].setX(vertex[2].getX()+eyeX);calculatedVertex[2].setY(vertex[2].getY()+eyeY);calculatedVertex[2].setZ(vertex[2].getZ()+eyeZ);
//		calculatedVertex[3].setX(vertex[3].getX()+eyeX);calculatedVertex[3].setY(vertex[3].getY()+eyeY);calculatedVertex[3].setZ(vertex[3].getZ()+eyeZ);
//		calculatedVertex[4].setX(vertex[4].getX()+eyeX);calculatedVertex[4].setY(vertex[4].getY()+eyeY);calculatedVertex[4].setZ(vertex[4].getZ()+eyeZ);
//		calculatedVertex[5].setX(vertex[5].getX()+eyeX);calculatedVertex[5].setY(vertex[5].getY()+eyeY);calculatedVertex[5].setZ(vertex[5].getZ()+eyeZ);
//		calculatedVertex[6].setX(vertex[6].getX()+eyeX);calculatedVertex[6].setY(vertex[6].getY()+eyeY);calculatedVertex[6].setZ(vertex[6].getZ()+eyeZ);
//		calculatedVertex[7].setX(vertex[7].getX()+eyeX);calculatedVertex[7].setY(vertex[7].getY()+eyeY);calculatedVertex[7].setZ(vertex[7].getZ()+eyeZ);
//		
		Log.d("setVertexDependOnCamera에서 호출함", "setVertexDependOnCamera에서 호출함 ");
//		this.printOriginalVertex();
//		this.printCalculedVertex();
//		
		getCurrentPosition(x,y);
		getdis(x,y);
		Log.d("createdCameraPosition[0]", "createdCameraPosition = " +createdCameraPosition[0]+" "+createdCameraPosition[1] + " " + createdCameraPosition[2]);
		Log.d("currentCameraPosition[0]", "currentCameraPosition = " +currentCameraPosition[0]+" "+currentCameraPosition[1] + " " + currentCameraPosition[2]);
		Log.d("dist", "dist : " + dist[0] + " " + dist[1]+ " " + dist[2]);
		//setCalculatedVertex(this.vertex, getCalculatedMovePoints(calculatedMovePoints), getCalculatedRotationAngle(calculatedRotationAngle),dist);
	}
	
	public void printCalculedVertex()
	{
		Log.d("calculatedVertex[0]", "calculatedVertex[0]=  x : " + (calculatedVertex[0].getX()) + " y : " +(calculatedVertex[0].getY()  + " z : " +(calculatedVertex[0].getZ())));
		Log.d("calculatedVertex[1]", "calculatedVertex[1]=  x : " + (calculatedVertex[1].getX()) + " y : " +(calculatedVertex[1].getY()  + " z : " +(calculatedVertex[1].getZ())));
		Log.d("calculatedVertex[2]", "calculatedVertex[2]=  x : " + (calculatedVertex[2].getX()) + " y : " +(calculatedVertex[2].getY()  + " z : " +(calculatedVertex[2].getZ())));
		Log.d("calculatedVertex[3]", "calculatedVertex[3]=  x : " + (calculatedVertex[3].getX()) + " y : " +(calculatedVertex[3].getY()  + " z : " +(calculatedVertex[3].getZ())));
		Log.d("calculatedVertex[4]", "calculatedVertex[4]=  x : " + (calculatedVertex[4].getX()) + " y : " +(calculatedVertex[4].getY()  + " z : " +(calculatedVertex[4].getZ())));
		Log.d("calculatedVertex[5]", "calculatedVertex[5]=  x : " + (calculatedVertex[5].getX()) + " y : " +(calculatedVertex[5].getY()  + " z : " +(calculatedVertex[5].getZ())));
		Log.d("calculatedVertex[6]", "calculatedVertex[6]=  x : " + (calculatedVertex[6].getX()) + " y : " +(calculatedVertex[6].getY()  + " z : " +(calculatedVertex[6].getZ())));
		Log.d("calculatedVertex[7]", "calculatedVertex[7]=  x : " + (calculatedVertex[7].getX()) + " y : " +(calculatedVertex[7].getY()  + " z : " +(calculatedVertex[7].getZ())));

	}
	public void printOriginalVertex()
	{
		Log.d("vertex[0]", "vertex[0] =  x : " + vertex[0].getX() + " y : " +vertex[0].getY() + " z : " +vertex[0].getZ());
		Log.d("vertex[1]", "vertex[1] =  x : " + vertex[1].getX() + " y : " +vertex[1].getY() + " z : " +vertex[1].getZ());
		Log.d("vertex[2]", "vertex[2] =  x : " + vertex[2].getX() + " y : " +vertex[2].getY() + " z : " +vertex[2].getZ());
		Log.d("vertex[3]", "vertex[3] =  x : " + vertex[3].getX() + " y : " +vertex[3].getY() + " z : " +vertex[3].getZ());
		Log.d("vertex[4]", "vertex[4] =  x : " + vertex[4].getX() + " y : " +vertex[4].getY() + " z : " +vertex[4].getZ());
		Log.d("vertex[5]", "vertex[5] =  x : " + vertex[5].getX() + " y : " +vertex[5].getY() + " z : " +vertex[5].getZ());
		Log.d("vertex[6]", "vertex[6] =  x : " + vertex[6].getX() + " y : " +vertex[6].getY() + " z : " +vertex[6].getZ());
		Log.d("vertex[7]", "vertex[7] =  x : " + vertex[7].getX() + " y : " +vertex[7].getY() + " z : " +vertex[7].getZ());
		
	}
	
	@Override
	public float[] getCurrentPosition(float x, float y) {
		// TODO Auto-generated method stub
		
		currentCameraPosition = renderer.convert2dTo3d(x, y);
		
//		Log.d("renderer.convert2dTo3d(513, 968)", "this,activity.gettHandler().renderer.convert2dTo3d(513, 968) : " + renderer.convert2dTo3d(513, 968)[0] + " " + renderer.convert2dTo3d(513, 968)[1] + " " + renderer.convert2dTo3d(513, 968)[2]);
//		Log.d("renderer.convert2dTo3d(200, 400)", "this,activity.gettHandler().renderer.convert2dTo3d(200, 400) : " + renderer.convert2dTo3d(200, 400)[0] + " " + renderer.convert2dTo3d(200, 400)[1] + " " + renderer.convert2dTo3d(200, 400)[2]);
//		Log.d("renderer.convert2dTo3d(700, 600)", "this,activity.gettHandler().renderer.convert2dTo3d(700, 600) : " + renderer.convert2dTo3d(700, 600)[0] + " " + renderer.convert2dTo3d(700, 600)[1] + " " + renderer.convert2dTo3d(700, 600)[2]);
		return currentCameraPosition;
	}
	@Override
	public void getdis(float x, float y) {
		// TODO Auto-generated method stub
		
		Log.d("dist 변환", "dist 변환");
		Log.d("dist[0]", "dist[0] " + dist[0]);
		Log.d("dist[1]", "dist[1] " + dist[1]);
		Log.d("dist[2]", "dist[2] " + dist[2]);
		
		this.currentCameraPosition=getCurrentPosition(x, y);
		dist[0] = createdCameraPosition[0] - currentCameraPosition[0];
		dist[1] = createdCameraPosition[1] - currentCameraPosition[1];
		dist[2] = createdCameraPosition[2] - currentCameraPosition[2];
		
		
	}
	public float[] getdist() {
		// TODO Auto-generated method stub
		
		return this.dist;
	}
	public void setPtVertex(float size) {
		for(int i=0; i<vertex.length; i++) {
			vertex[i] = new Vertex();
		}
		vertex[0].setX(-1f*size);	vertex[0].setY(-1f*size);	vertex[0].setZ(-1f*size);
		vertex[1].setX(+1f*size);	vertex[1].setY(-1f*size);	vertex[1].setZ(-1f*size);
		vertex[2].setX(+1f*size);	vertex[2].setY(+1f*size);	vertex[2].setZ(-1f*size);
		vertex[3].setX(-1f*size);	vertex[3].setY(+1f*size);	vertex[3].setZ(-1f*size);
		vertex[4].setX(-1f*size);	vertex[4].setY(-1f*size);	vertex[4].setZ(+1f*size);
		vertex[5].setX(+1f*size);	vertex[5].setY(-1f*size);	vertex[5].setZ(+1f*size);
		vertex[6].setX(+1f*size);	vertex[6].setY(+1f*size);	vertex[6].setZ(+1f*size);
		vertex[7].setX(-1f*size);	vertex[7].setY(+1f*size);	vertex[7].setZ(+1f*size);

		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {1,2,5,6, 6,2,7,3, 3,0,7,4, 4,0,5,1, 1,0,2,3, 3,5, 5,6,4,7};
		
		for(int i=0, k=0; i<tmpIndices.length; i++, k+=3) {
			vertices[k] = vertex[tmpIndices[i]].getX();
			vertices[k+1] = vertex[tmpIndices[i]].getY();
			vertices[k+2] = vertex[tmpIndices[i]].getZ();
		}		
		for(int i=0, k=0; i<8; i++, k+=4) {
			colors[k]=vertex[i].getR();
			colors[k+1]=vertex[i].getG();
			colors[k+2]=vertex[i].getB();
			colors[k+3]=vertex[i].getAlpha();
		}
		
		float texturecoords[]= {0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 0f, 0f, 1f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f};
		
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
	public void setCalculatedVertexXaxis(int selectedPtPrism, float f) {
		// TODO Auto-generated method stub
		rememberX=f;
		setPtVertex(f,selectedPtPrism,1,rememberX,rememberY,rememberZ);
		
			//vertices[3*selectedPtPrism] = vertex[selectedPtPrism].getX();
				
		
		
	}
	
//	this.calculatedVertex[selectedPtPrism].setY(this.calculatedVertex[selectedPtPrism].getY());
//	this.calculatedVertex[selectedPtPrism].setZ(this.calculatedVertex[selectedPtPrism].getZ());
//	
	public void setCalculatedVertexYaxis(int selectedPtPrism, float f) {
		// TODO Auto-generated method stub
		rememberY=f;
		setPtVertex(f,selectedPtPrism,2,rememberX,rememberY,rememberZ);
		
		//vertices[3*selectedPtPrism+1] = vertex[selectedPtPrism].getY();
	}
	
	public void setCalculatedVertexZaxis(int selectedPtPrism, float f) {
		// TODO Auto-generated method stub
		rememberZ=f;
		setPtVertex(f,selectedPtPrism,3,rememberX,rememberY,rememberZ);
		//vertices[3*selectedPtPrism+2] = vertex[selectedPtPrism].getZ();
	}
	
	public void setPtVertex(float size, int selectedPtPrism,int mode,float rememberX,float rememberY,float rememberZ) {//mode가 1이면 x움직이고 2면 y 3이면 z움직인다.
		for(int i=0; i<vertex.length; i++) {
			vertex[i] = new Vertex();
		}
		//size만큼 selectedPtprism번째 버택스의 mode좌표를 이동
		
		vertex[0].setX(-1f);	vertex[0].setY(-1f);	vertex[0].setZ(-1f);
		vertex[1].setX(+1f);	vertex[1].setY(-1f);	vertex[1].setZ(-1f);
		vertex[2].setX(+1f);	vertex[2].setY(+1f);	vertex[2].setZ(-1f);
		vertex[3].setX(-1f);	vertex[3].setY(+1f);	vertex[3].setZ(-1f);
		vertex[4].setX(-1f);	vertex[4].setY(-1f);	vertex[4].setZ(+1f);
		vertex[5].setX(+1f);	vertex[5].setY(-1f);	vertex[5].setZ(+1f);
		vertex[6].setX(+1f);	vertex[6].setY(+1f);	vertex[6].setZ(+1f);
		vertex[7].setX(-1f);	vertex[7].setY(+1f);	vertex[7].setZ(+1f);
		
		if(mode==1)
		{	vertex[selectedPtPrism].setX(vertex[selectedPtPrism].getX()+size);
			//vertex[selectedPtPrism].setY(rememberY);
			//vertex[selectedPtPrism].setZ(rememberZ);
		}
		else if(mode==2){
			vertex[selectedPtPrism].setY(vertex[selectedPtPrism].getY()+size);
			//vertex[selectedPtPrism].setX(rememberX);
			//vertex[selectedPtPrism].setY(rememberZ);
		}
		else if(mode==3){
			vertex[selectedPtPrism].setZ(vertex[selectedPtPrism].getZ()+size);
			//vertex[selectedPtPrism].setX(rememberX);
			//vertex[selectedPtPrism].setY(rememberY);
		}
		
		//vertex order for GL_TRIANGLE_STRIP
		int tmpIndices[] = {1,2,5,6, 6,2,7,3, 3,0,7,4, 4,0,5,1, 1,0,2,3, 3,5, 5,6,4,7};
		
		for(int i=0, k=0; i<tmpIndices.length; i++, k+=3) {
			vertices[k] = vertex[tmpIndices[i]].getX();
			vertices[k+1] = vertex[tmpIndices[i]].getY();
			vertices[k+2] = vertex[tmpIndices[i]].getZ();
		}		
		for(int i=0, k=0; i<8; i++, k+=4) {
			colors[k]=vertex[i].getR();
			colors[k+1]=vertex[i].getG();
			colors[k+2]=vertex[i].getB();
			colors[k+3]=vertex[i].getAlpha();
		}
		
		float texturecoords[]= {0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    1f, 1f, 0f, 1f, 1f, 0f, 0f, 0f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f,
			    1f, 0f, 0f, 1f,
			    0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f};
		
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
	public int checkPick2(float[] result) {
		// TODO Auto-generated method stub
		//Log.d("movedPoints", "MovedPoint = x : "+getcalculatedMovePoints(calculatedMovePoints)[0] + " y : "+getcalculatedMovePoints(calculatedMovePoints)[1]+" z : " +getcalculatedMovePoints(calculatedMovePoints)[2]);
		
		//this.printOriginalVertex();
		
		
		setCalculatedVertex(this.vertex, getCalculatedMovePoints(calculatedMovePoints), getCalculatedRotationAngle(calculatedRotationAngle),dist);
		
		
		//Log.d("checkPick에서 호출", "checkPick에서 호출 ");
		//this.printCalculedVertex();
		//Log.d("calculatedVertex", "calculatedVertex : " + calculatedVertex[0].getX());
		
		
		boolean r = false;
		float min;
		int minindex=0;
		
		min = (float) Math.pow(this.calculatedVertex[0].getX()-result[0], 2) + (float) Math.pow(this.calculatedVertex[0].getY()-result[1], 2) + (float) Math.pow(this.calculatedVertex[0].getZ()-result[2], 2);
		
		for(int i=1;i<8;i++)
		{
			if(min>((float) Math.pow(this.calculatedVertex[i].getX()-result[0], 2) + (float) Math.pow(this.calculatedVertex[i].getY()-result[1], 2) + (float) Math.pow(this.calculatedVertex[i].getZ()-result[2], 2))){
				min=(float) Math.pow(this.calculatedVertex[i].getX()-result[0], 2) + (float) Math.pow(this.calculatedVertex[i].getY()-result[1], 2) + (float) Math.pow(this.calculatedVertex[i].getZ()-result[2], 2);
				minindex=i;
			}
		}
		
		
		return minindex;
	}
}

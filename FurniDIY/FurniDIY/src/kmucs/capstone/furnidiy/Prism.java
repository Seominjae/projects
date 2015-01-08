package kmucs.capstone.furnidiy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import kmucs.capstone.furnidiy.Vertex;




public class Prism {
	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer colorBuffer;
	protected ByteBuffer  indexBuffer;
	protected FloatBuffer textureBuffer;
	
	protected FloatBuffer t_vertexBuffer;
	protected FloatBuffer t_colorBuffer;

	protected Vertex read_vertex[];
	protected Vertex vertex[];
	protected Vertex t_vertex[];
	protected Vertex calculatedVertex[];
	
	protected float vertices[];
	protected float colors[];
	protected float t_vertices[];
	protected float t_colors[];
	
	protected float rotationAngle[] = new float[3];
	protected float rotationAngle2[] = new float[3];
	protected float movePoints[] = new float[3];
	protected float movePoints2[] = new float[3];
	protected float calculatedMovePoints[] = new float[3];
	protected float calculatedRotationAngle[] = new float[3];
		
	protected boolean textureMode = false;
	
	protected int prismSize;
	
	protected boolean isSelected = false;
	
	protected int[] texHandles = new int[1];
	
	protected boolean isTextureSet = false;
	protected int textureIndex = -1;
	
	protected float opacity = 1.0f;
	public boolean calued = false;
	
	protected float[] createdCameraPosition = new float[3];
	protected float[] currentCameraPosition = new float[3];
	protected float[] dist = new float[3];
	
	protected EditRenderer renderer;
		
	public Prism(int prismSize) {
		this.prismSize = prismSize;
		
		vertex = new Vertex[prismSize*2];
		t_vertex = new Vertex[prismSize*6];
		calculatedVertex = new Vertex[prismSize*2];
		
		vertices = new float[(prismSize*6+2)*3];
		colors = new float[prismSize*8];
		t_vertices = new float[prismSize*18];
		t_colors = new float[prismSize*24];
	}
	
	public void draw(GL10 gl) {
		if(textureMode && isTextureSet) {						
			//Set the face rotation
			gl.glFrontFace(GL10.GL_CCW);
			
			//Point to our buffers
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			
			//Enable the vertex and color state
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
						
			gl.glColor4f(1.0f, 1.0f, 1.0f, opacity);
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texHandles[textureIndex]);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);			
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);			
			
			
			gl.glDisable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		else {
			//Set the face rotation
			gl.glFrontFace(GL10.GL_CW);
			
			//Point to our buffers
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, t_vertexBuffer);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, t_colorBuffer);
			
			//Enable the vertex and color state
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawArrays(GL10.GL_LINES, 0, t_vertex.length);			
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
	}
	
	public void setTexId(int[] tex) {
		texHandles = tex;
	}
	
	public boolean isTextureMode() {
		return textureMode;
	}
		
	protected void setVertex() {
		
	}
	
	public void setTextureIndex(int index){
		textureIndex = index;
		isTextureSet = true;
		textureMode = true;	
	}
		
	protected void setTransparencyVertex() {
		for(int i=0; i<prismSize*2; i++) {
			if(i==prismSize-1) {
				t_vertex[i*2] = vertex[0];
				t_vertex[i*2+1] = vertex[prismSize-1];
			}
			else if(i==prismSize*2-1) {
				t_vertex[i*2] = vertex[prismSize];
				t_vertex[i*2+1] = vertex[prismSize*2-1];
			}
			else {
				t_vertex[i*2] = vertex[i];
				t_vertex[i*2+1] = vertex[i+1];
			}
		}
		
		//옆면 선분 배치
		for(int i=0; i<prismSize; i++) {
			t_vertex[i*2+prismSize*4] = vertex[i];
			t_vertex[i*2+prismSize*4+1] = vertex[i+prismSize];
		}
		
		//set
		for(int i=0,k=0; i<prismSize*6; i++,k+=3) {
			t_vertices[k]=t_vertex[i].getX();
			t_vertices[k+1]=t_vertex[i].getY();
			t_vertices[k+2]=t_vertex[i].getZ();
		}		
		for(int i=0, k=0; i<prismSize*6; i++, k+=4) {
			t_colors[k]=t_vertex[i].getR();
			t_colors[k+1]=t_vertex[i].getG();
			t_colors[k+2]=t_vertex[i].getB();
			t_colors[k+3]=t_vertex[i].getAlpha();
		}
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(t_vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		t_vertexBuffer = byteBuf.asFloatBuffer();
		t_vertexBuffer.put(t_vertices);
		t_vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(t_colors.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		t_colorBuffer = byteBuf.asFloatBuffer();
		t_colorBuffer.put(t_colors);
		t_colorBuffer.position(0);
	}
	
	protected void changeLineColor() {
		if(isSelected) {
			for(int i=0, k=0; i<prismSize*6; i++, k+=4) {
				t_colors[k]=204f;
			}
			
			opacity = 0.7f;

			ByteBuffer byteBuf = ByteBuffer.allocateDirect(t_colors.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			t_colorBuffer = byteBuf.asFloatBuffer();
			t_colorBuffer.put(t_colors);
			t_colorBuffer.position(0);
		}
		else {
			for(int i=0, k=0; i<prismSize*6; i++, k+=4) {
				t_colors[k]=t_vertex[i].getR();
			}
			
			opacity = 1.0f;

			ByteBuffer byteBuf = ByteBuffer.allocateDirect(t_colors.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			t_colorBuffer = byteBuf.asFloatBuffer();
			t_colorBuffer.put(t_colors);
			t_colorBuffer.position(0);
		}
	}
	
	protected void selectPrism(boolean isSelected) {
		this.isSelected = isSelected;
	}
	protected boolean getSelectPrism() {
		return this.isSelected;
	}
	
	public float[] getRotationAngle() {
		return rotationAngle;
	}
	
	public float[] getMovingPoints() {
		return movePoints;
	}
	
	
	public float[] getRotationAngle2() {
		return rotationAngle2;
	}
	
	public float[] getMovingPoints2() {
		return movePoints2;
	}
	
	public int getPrismSize() {
		return prismSize;
	}
	
	public void setRotationAngle(int axis, float angle) {
		if(axis==0)
			return;
		
		rotationAngle[axis-1] += angle;
	}
	
	public void setMovingPoint(int axis, float number) {
		if(axis==0)
			return;
		
		movePoints[axis-1] += number;
	}
	
	public void setMovingPoint2(int axis, float number) {
		if(axis==0)
			return;
		
		movePoints2[axis-1] = number;
	}
	public void swtichMode() {
		if(textureMode)
			textureMode = false;
		else
			textureMode = true;
	}
	public void switchModeOne(boolean bSet) {
		textureMode = bSet;
	}
	public Vertex findNormalVector(Vertex p, Vertex q, Vertex r)
	{
		Vertex normal = new Vertex();
		Vertex pq = new Vertex();
		Vertex qr = new Vertex();
		
		pq.setX(q.getX()-p.getX());
		pq.setY(q.getY()-p.getY());
		pq.setZ(q.getZ()-p.getZ());
		
		qr.setX(r.getX()-q.getX());
		qr.setY(r.getY()-q.getY());
		qr.setZ(r.getZ()-q.getZ());
		
		normal.setX(pq.getY()*qr.getZ() - pq.getZ()*qr.getY());
		normal.setY(-(pq.getX()*qr.getZ() - pq.getZ()*qr.getX()));
		normal.setZ(pq.getX()*qr.getY() - pq.getY()*qr.getX());
		
		normal.setR(p.getX());
		normal.setG(p.getY());
		normal.setB(p.getZ());
		
		//Log.d("normal Vector", "normal Vector = x : "+normal.getX() +" y : "+normal.getY()+" z: "+normal.getZ());
		//Log.d("normal Vector", "normal Vector = R : "+normal.getR() +" G : "+normal.getG()+" B: "+normal.getB());
		return normal;
		
		//점 P,Q,R 이 만드는 평면의 법선벡터를 구하는 함수
		//pq은 점 P와Q가 만드는 벡터
		//qr은 점 Q와R이 만드는 벡터
		// PQ벡터 X QR벡터(외적)으로 PQR이 만드는 평면의 법선벡터를 구한다
		// normal.x = PQR평면 법선벡터의 X방향
		// normal.y = PQR평면 법선벡터의 Y방향
		// normal.z = PQR평면 법선벡터의 Z방향
		// normal.r = PQR평면의 방정식에서 YZ절편
		// normal.g = PQR평면의 방정식에서 XZ절편
		// normal.b = PQR평면의 방정식에서 XY절편
		// 0 = normal.x(선택된X-normal.r)+normal.y(선택된Y-normal.g)+normal.z(선택된Z-normal.b)
	}
	
	public boolean findSign(Vertex normal, float[] result)
	{
		//Log.d("getB", "getB is "+normal.getB());
		//Log.d("result[2]", "result[2]" + result[2]);
		
		//Log.d("result", "result = x : " + result[0] + " y : " +result[1] + " z : " +result[2]);
		//Log.d("normal Vector", "normal Vector = x : "+normal.getX() +" y : "+normal.getY()+" z: "+normal.getZ());
		//Log.d("normal Vector", "normal Vector = R : "+normal.getR() +" G : "+normal.getG()+" B: "+normal.getB());
		
		if(normal.getX()*(result[0]-normal.getR()) 
		 + normal.getY()*(result[1]-normal.getG())
		 + normal.getZ()*(result[2]-normal.getB()) > 0){
			//Log.d("sign", "sign is up!!");
			return true;
		}
		else{
			//Log.d("sign", "sign is down!!");
			return false;
		}
	}
	protected boolean checkPick(float[] result) 
	{
		//findSign(findNormalVector(vertex[0],vertex[1],vertex[2]));
		return true;
	}
	public void setCalculatedMovePoints(float[] move){
		this.calculatedMovePoints=move;
	}
	public void setCalculatedRotationAngle(float[] angle){
		this.calculatedRotationAngle=angle;
	}
	public float[] getCalculatedMovePoints(float[] move){
		return this.calculatedMovePoints;
	}
	public float[] getCalculatedRotationAngle(float[] angle){
		return this.calculatedRotationAngle;
	}

	protected Vertex[] getCalculatedVertex() {
		return calculatedVertex;
	}

	public void initCalculatedVertex(Vertex[] vertex) {
		this.calculatedVertex = new Vertex[vertex.length];
	}
	public void setCalculatedVertex(Vertex[] vertex, float[] move, float[] angle, float[] dist) {
	
	}
	public void setRead_vertex(float x, float y, float z, int idx) {
		this.read_vertex[idx].setX(x);
		this.read_vertex[idx].setY(y);
		this.read_vertex[idx].setZ(z);
	}
	public Vertex[] getVertex() {
		return vertex;
	}
	
	public void setVertexDependOnCamera(double horizontal, double radius, double vertical, float eyeX, float eyeY, float eyeZ, float upX, float upY, float upZ,float x, float y) {
	
	}
	public void printCalculedVertex()
	{
	}
	public void printOriginalVertex()
	{
	}
	public float[] getCurrentPosition() {
		// TODO Auto-generated method stub
		
		float[] a = new float[3];
		return a;
	}
	public void getdis()
	{
		float[] a = new float[3];
		
	}

	public float[] getCurrentPosition(float x, float y) {
		// TODO Auto-generated method stub
		return null;
	}

	public void getdis(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	public float[] getdist() {
		// TODO Auto-generated method stub
		
		return this.dist;
	}

	public void setCalculatedMovePoints(float[] move2, float[] getdist) {
		// TODO Auto-generated method stub
			
		float[] a = new float[3];
		a[0] =this.calculatedMovePoints[0]+move2[0]+getdist[0];
		a[1] =this.calculatedMovePoints[1]+move2[1]+getdist[1];
		a[2] =this.calculatedMovePoints[2]+move2[2]+getdist[2];
			this.calculatedMovePoints=a;
		
		
	}

	public void setCalculatedRotationAngle(float[] angle2, float[] getdist) {
		// TODO Auto-generated method stub
		float[] a = new float[3];
		a[0] =this.calculatedRotationAngle[0]+angle2[0]+getdist[0];
		a[1] =this.calculatedRotationAngle[1]+angle2[1]+getdist[1];
		a[2] =this.calculatedRotationAngle[2]+angle2[2]+getdist[2];
			this.calculatedRotationAngle=a;
	}
	

	public void setCalculatedVertexXaxis(int selectedPtPrism, float f) {
		// TODO Auto-generated method stub
		this.vertex[selectedPtPrism].setX(this.vertex[selectedPtPrism].getX()+f);
	}
	
//	this.calculatedVertex[selectedPtPrism].setY(this.calculatedVertex[selectedPtPrism].getY());
//	this.calculatedVertex[selectedPtPrism].setZ(this.calculatedVertex[selectedPtPrism].getZ());
//	
	public void setCalculatedVertexYaxis(int selectedPtPrism, float f) {
		// TODO Auto-generated method stub
		this.vertex[selectedPtPrism].setY(this.vertex[selectedPtPrism].getY()+f);
	}
	
	public void setCalculatedVertexZaxis(int selectedPtPrism, float f) {
		// TODO Auto-generated method stub
		this.vertex[selectedPtPrism].setZ(this.vertex[selectedPtPrism].getZ()+f);
	}

	public int checkPick2(float[] result) {
		// TODO Auto-generated method stub
		return 2;
	}
}

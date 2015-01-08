package kmucs.capstone.furnidiy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.io.*;
import java.nio.IntBuffer;


public class EditRenderer implements Renderer {

	int[] viewport = new int[4];
	
	private float[] viewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mMVMatrix = new float[16];
    private float width = 720, height = 1184;
    float[] mouseRayProjection = new float[3];
	
    public GroupManager groupManager;
    EditActivity activity;
	public List<Prism> prismList = new ArrayList<Prism>();
	public List<Prism> ptPrismList = new ArrayList<Prism>();
	
	private Prism prism;
	private Axis axis;
		
	protected float left, right, top, bottom, near,far,ratio;
	protected float eyeX=30f, eyeY=0f, eyeZ=0f, centerX=0f, centerY=0f, centerZ=0f, upX=0f, upY=0f, upZ=1f;
	protected float eyeXO=30f, eyeYO=0f, eyeZO=0f, centerXO=0f, centerYO=0f, centerZO=0f, upXO=0f, upYO=0f, upZO=1f;
	public double vertical, horizontal, upVertical, upHorizontal;
	public double radius = 30;
	public float tmpEyeY, tmpEyeZ, tmpRotateZ;
	
	private float oriTheta = 90f, oriPi = 0f, oriRadius = 30f;
	private float realWidth = 14.9f, realHeight = 24.6f;
	
	public int[] tex_ids;
	public boolean isBitmapReady;
	public ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
	
	private List<Boolean> textureModeList = new ArrayList<Boolean>(); 
	
	private Background background;
	private Bitmap bgBmp;
	private boolean bgBmpReady = false;
	private boolean bgMode = false;
	
	// yhchung
	private boolean screenCapture = false;
	private String imageFilePath = "";
	private boolean startedFlag = false;
	float[] prev_result = {0,0,0};
	float[] prevCurr = new float[3];
	

	
	public void setScreenCapture(boolean screenCapture, String path) {
		this.screenCapture = screenCapture;
		this.imageFilePath = path;
	}
	
	public void setTextureModeList() { // capture위해 강제 투명화
		textureModeList.clear();
		
		for(int i=0; i<prismList.size(); i++) {
			textureModeList.add(prismList.get(i).isTextureMode());
			prismList.get(i).switchModeOne(false);
		}
	}
	
	public void restoreTextureMode() { // 다시 원복
		for(int i=0; i<textureModeList.size(); i++) {
			 prismList.get(i).switchModeOne( textureModeList.get(i) );
		}
	}
	/**
	 * Instance the Pyramid and Cube objects
	 */
	public EditRenderer(EditActivity activity) {
		vertical = 90;
		horizontal = 0;
		upVertical = 0;
		upHorizontal = 0;
		this.activity=activity;
		groupManager = new GroupManager(this,activity);
		axis = new Axis();
	}
	public void makePrism(int n,float x, float y)
	{
		startedFlag=true;
		
		if(n==3)
			prism = new TrigonalPrism(mouseRayProjection,this);
		else if(n==4){
			prism = new SquarePrism(mouseRayProjection,this,activity.gettHandler().renderer.convert2dTo3d(x, y));
			
//			Log.d("this,activity.gettHandler().renderer.convert2dTo3d(513, 968)", "this,activity.gettHandler().renderer.convert2dTo3d(513, 968) : " + activity.gettHandler().renderer.convert2dTo3d(513, 968)[0] + " " + activity.gettHandler().renderer.convert2dTo3d(513, 968)[1] + " " + activity.gettHandler().renderer.convert2dTo3d(513, 968)[2]);
//			Log.d("this,activity.gettHandler().renderer.convert2dTo3d(200, 400)", "this,activity.gettHandler().renderer.convert2dTo3d(200, 400) : " + activity.gettHandler().renderer.convert2dTo3d(200, 400)[0] + " " + activity.gettHandler().renderer.convert2dTo3d(200, 400)[1] + " " + activity.gettHandler().renderer.convert2dTo3d(200, 400)[2]);
//			Log.d("this,activity.gettHandler().renderer.convert2dTo3d(700, 600)", "this,activity.gettHandler().renderer.convert2dTo3d(700, 600) : " + activity.gettHandler().renderer.convert2dTo3d(700, 600)[0] + " " + activity.gettHandler().renderer.convert2dTo3d(700, 600)[1] + " " + activity.gettHandler().renderer.convert2dTo3d(700, 600)[2]);
		}
		else if(n==5)
			prism = new PentagonalPrism(mouseRayProjection,this);
		else if(n==6)
			prism = new HexagonalPrism(mouseRayProjection,this);
		else if(n==0)
			prism = new Cylinder(mouseRayProjection,this);
		else
			prism = new SquarePrism(mouseRayProjection,this,activity.gettHandler().renderer.convert2dTo3d(513, 968));
		
		prismList.add(prism);
		Log.i("cubeList", Integer.toString(prismList.size()));
	}
	public void makePrism(int n, Vertex[] vPos)
	{
		if(n==3)
			prism = new TrigonalPrism(mouseRayProjection, vPos,this);
		else if(n==4)
			prism = new SquarePrism(mouseRayProjection, vPos,this,activity.gettHandler().renderer.convert2dTo3d(513, 968));
		else if(n==5)
			prism = new PentagonalPrism(mouseRayProjection, vPos,this);
		else if(n==6)
			prism = new HexagonalPrism(mouseRayProjection, vPos,this);
		else if(n==0)
			prism = new SquarePrism(mouseRayProjection,this,activity.gettHandler().renderer.convert2dTo3d(513, 968));
		else
			prism = new SquarePrism(mouseRayProjection,this,activity.gettHandler().renderer.convert2dTo3d(513, 968));
		
		prismList.add(prism);
		Log.i("cubeList", Integer.toString(prismList.size()));
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(1, 1, 1, 1); 				//White Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		Matrix.setLookAtM(viewMatrix, 0, eyeXO, eyeYO, eyeZO, centerXO, centerYO, centerZO, upXO, upYO, upZO);
		
		
	}

	/**
	 * Here we do our drawing
	 */
	public void onDrawFrame(GL10 gl) {
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
		
		moveScreen(0f,0f);		
					
		if(isBitmapReady)
			updateTexture(gl);
		
		if(bgBmpReady) {
			background = new Background(gl, bgBmp);
			bgBmpReady = false;
			bgMode = true;
		}
		
		if(bgMode) {
			GLU.gluLookAt(gl, 40f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f);
			background.draw(gl);
			gl.glTranslatef(0f, tmpEyeY, tmpEyeZ);
			gl.glRotatef(tmpRotateZ, 0f, 0f, 1f);
		}		
		else {
			GLU.gluLookAt(gl, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
			
			if(!screenCapture) {// capture할때는 축 line 안그리기
				gl.glLineWidth(5.0f);
				gl.glDisableClientState(GL10.GL_LINE_SMOOTH);			
				axis.draw(gl);		
				
				gl.glLineWidth(1.0f);
				gl.glEnableClientState(GL10.GL_LINE_SMOOTH);
			}
		}		
		
		for(int i=0; i<prismList.size(); i++) {
			float[] angle = prismList.get(i).getRotationAngle();
			float[] move = prismList.get(i).getMovingPoints();
			
			float[] angle2 = new float[3];
			float[] move2 = new float[3];
			
			prismList.get(i).setCalculatedMovePoints(move);
			prismList.get(i).setCalculatedRotationAngle(angle);
			
			//gl.glLoadIdentity();
			gl.glPushMatrix();
			
			gl.glTranslatef(move[0], move[1], move[2]);
			gl.glRotatef(angle[0], 1.0f, 0.0f, 0.0f);
			gl.glRotatef(angle[1], 0.0f, 1.0f, 0.0f);
			gl.glRotatef(angle[2], 0.0f, 0.0f, 1.0f);
			
			if(prismList.get(i).isTextureMode()) {									
				prismList.get(i).setTexId(tex_ids);
			}
			
			prismList.get(i).draw(gl);
			gl.glPopMatrix();
			
			if(ptPrismList.size()!=0){
				for(int j=0;j<ptPrismList.size();j++){
				angle2 = ptPrismList.get(j).getRotationAngle2();
				move2 = ptPrismList.get(j).getMovingPoints2();
				
//				setMovingPoint2(1, pos[0]);
//				setMovingPoint2(2, pos[1]);
//				setMovingPoint2(3, pos[2]);
				
				//ptPrismList.get(j).setCalculatedMovePoints(move2,prismList.get(i).getdist());
				//ptPrismList.get(j).setCalculatedRotationAngle(angle2,prismList.get(i).getdist());
				
				ptPrismList.get(j).setCalculatedMovePoints(move2);
				ptPrismList.get(j).setCalculatedRotationAngle(angle2);
				
				gl.glPushMatrix();
				//Log.d("move2", "move2 : " + move2[0] + " "+move2[1] + " " + move[2]);
				gl.glTranslatef(move2[0], move2[1], move2[2]);
				gl.glRotatef(angle2[0], 1.0f, 0.0f, 0.0f);
				gl.glRotatef(angle2[1], 0.0f, 1.0f, 0.0f);
				gl.glRotatef(angle2[2], 0.0f, 0.0f, 1.0f);
				//ptPrismList.get(j).draw(gl);
				gl.glPopMatrix();
				}
			}
			
		}
		//Log.d("ptPrismList.size()", "ptPrismList.size() : " + ptPrismList.size());
//		for(int i=0; i<ptPrismList.size(); i++) {
//			
//			ptPrismList.get(i).draw(gl);	
//			
//			
//		}
		
		if(screenCapture) { // capture였던 부분 원복
			saveImageFile(gl, width, height);
			screenCapture = false;
			restoreTextureMode();
		}
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);		
		
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.setLookAtM(viewMatrix, 0, eyeXO, eyeYO, eyeZO, centerXO, centerYO, centerZO, upXO, upYO, upZO);
		Matrix.multiplyMM(mMVMatrix, 0, viewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0);
		
		gl.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);
		
		gl.glFlush();
	}

	/**
	 * If the surface changes, reset the view
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}		

		//gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix
		
		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix		
		this.width = width;
		this.height = height;
		ratio = (float) width / height;
        left = -ratio;
        right = ratio;
        bottom = -1.0f;
        top = 1.0f;
        near = 1.0f;
        far = 10.0f;
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}
	
	public void moveScreen(float x, float y) {	
				
		horizontal -= x*0.3;
		vertical -= y*0.3;		
		
		upHorizontal -= x*0.3;
		upVertical -= y*0.3;
		
		moveCamera();
	}
	
	public void zoomScreen(float dist) {
		radius -= dist;
		
		if(radius < 5)
			radius = 5;
		if(radius > 60)
			radius = 60;
		
		moveCamera();
	}	
	
	public void moveObject(float x, float y) {
		tmpEyeY += x*0.1;
		tmpEyeZ -= y*0.1;
	}
	
	public void rotateObject(float z) {
		tmpRotateZ += z;
	}
	
	public void moveCamera() {		
		eyeX = (float) ((Math.cos(Math.toRadians(horizontal)) * radius * Math.sin(Math.toRadians(vertical)))); 
		eyeY = (float) ((Math.sin(Math.toRadians(horizontal)) * radius * Math.sin(Math.toRadians(vertical))));
		eyeZ = (float) ((Math.cos(Math.toRadians(vertical)) * radius));
				
		upX = (float) ((Math.cos(Math.toRadians(upHorizontal)) * Math.sin(Math.toRadians(upVertical)))); 
		upY = (float) ((Math.sin(Math.toRadians(upHorizontal)) * Math.sin(Math.toRadians(upVertical))));
		upZ = (float) ((Math.cos(Math.toRadians(upVertical))));
		
		
//		Log.d("eyeX", "eyeX " + eyeX);
//		Log.d("eyeY", "eyeY " + eyeY);
//		Log.d("eyeZ", "eyeZ " + eyeZ);
//		
//		Log.d("upX", "upX " + upX);
//		Log.d("upY", "upY " + upY);
//		Log.d("upZ", "upZ " + upZ);
		
		/*if ((prismList.size() != 0)&& this.startedFlag) {
			for (int i = 0; i < prismList.size(); i++) {
				if(prismList.get(i).calued){
					Log.d("몇번째 도형의 좌표가 이런지", (i+1)+"번째 도형의 좌표");
					prismList.get(i).setVertexDependOnCamera(horizontal, radius,vertical, eyeX, eyeY, eyeZ, upX, upY, upZ);
					prismList.get(i).printOriginalVertex();
					prismList.get(i).printCalculedVertex();
				}
			}
			}
			*/
	}
	
	public float[] onTouch(float touchX, float touchY)
    {			
		mouseRayProjection = convert2dTo3d(touchX, touchY);
				
		return mouseRayProjection;
    }
	
	
	public Prism getPrism() {
		return prism;
	}
	
	public void deletePrism(HashSet<Integer> index) {
		
		//HashSet<Integer> tempIndex = (HashSet<Integer>)index.clone();
		ArrayList<Integer> tempArray = new ArrayList<Integer>();
		Iterator<Integer> iterator = index.iterator();
		
		
		//HashSet으로 들어온 index를 ArrayList tempArray로 변환
		for(int i=0;i<index.size();i++)
		{
			int temp = iterator.next();
			tempArray.add(temp);
		}
		Log.d("prismList.size()", "prismList.size() is " + prismList.size());
		
		
		
		/*while(iterator.hasNext())
		{
			int temp = iterator.next();
			prismList.remove(temp);
			
		}		
		Log.d("prismList.size()", "prismList.size() is " + prismList.size());
		*/
	}
	
	/*int temp = iterator.next();
	Log.d("temp", "temp is " + temp);
	HashSet<Integer> tempIndex = (HashSet<Integer>) activity.getSelectIndex().clone();
	prismList.remove(temp);
	activity.setSelectIndex(prismList);
	*/
	
	public void deletePrism(int index) {
		Log.d("prismList.size()", "prismList.size() is " + prismList.size());
			prismList.remove(index);
			Log.d("prismList.size()", "prismList.size() is " + prismList.size());
		
	}
	
	public void copyPrism(HashSet<Integer> index) {
		

		Iterator<Integer> iterator = index.iterator();
		while(iterator.hasNext())
		{
			int temp = iterator.next();
			Log.d("temp", "temp is " + temp);
			
			int n = prismList.get(temp).getPrismSize();
			
			Log.d("n", "n is " + n);
			if(n==3) {
				prism = new TrigonalPrism(prismList.get(temp),this);
			}
			else if(n==4) {
				prism = new SquarePrism(prismList.get(temp),this,activity.gettHandler().renderer.convert2dTo3d(513, 968));
			}
			else if(n==5) {
				prism = new PentagonalPrism(prismList.get(temp),this);
			}
			else if(n==6) {
				prism = new HexagonalPrism(prismList.get(temp),this);
			}
			else if(n==0) {
				prism = new Cylinder(prismList.get(temp),this);
			}
			
			prism.setMovingPoint(1, 1);
			prism.setMovingPoint(2, 1);
			prism.setMovingPoint(3, 1);
			Log.d("prismList.size()", "prismList.size() group is " + prismList.size());
			prismList.add(prism);
			Log.d("prismList.size()2", "prismList.size()2 group is " + prismList.size());
		}

		
		/*for(int i=0; i<index.length; i++) {
			int n = prismList.get(index[i]).getPrismSize();
			
			if(n==3) {
				prism = new TrigonalPrism(prismList.get(index[i]));
			}
			else if(n==4) {
				prism = new SquarePrism(prismList.get(index[i]));
			}
			else if(n==5) {
				prism = new PentagonalPrism(prismList.get(index[i]));
			}
			else if(n==6) {
				prism = new HexagonalPrism(prismList.get(index[i]));
			}
			else if(n==0) {
				//prism = new TrigonalPrism(prismList.get(index[i]));
			}
			
			prism.setMovingPoint(1, 1);
			prism.setMovingPoint(2, 1);
			prism.setMovingPoint(3, 1);
			prismList.add(prism);
		}
		*/
	}
	
	public void copyPrism(int index) {
			int n = prismList.get(index).getPrismSize();
			
			if(n==3) {
				prism = new TrigonalPrism(prismList.get(index),this);
			}
			else if(n==4) {
				prism = new SquarePrism(prismList.get(index),this,activity.gettHandler().renderer.convert2dTo3d(513, 968));
			}
			else if(n==5) {
				prism = new PentagonalPrism(prismList.get(index),this);
			}
			else if(n==6) {
				prism = new HexagonalPrism(prismList.get(index),this);
			}
			else if(n==0) {
				prism = new Cylinder(prismList.get(index),this);
			}
			
			prism.setMovingPoint(1, 1);
			prism.setMovingPoint(2, 1);
			prism.setMovingPoint(3, 1);
			Log.d("prismList.size()", "prismList.size() is " + prismList.size());
			prismList.add(prism);
			Log.d("prismList.size()", "prismList.size() is " + prismList.size());
		}
	
	
	public void transparentPrism(HashSet<Integer> index) {
		
		Iterator<Integer> iterator = index.iterator();
		while(iterator.hasNext())
		{
			prismList.get(iterator.next()).swtichMode();
		}
	}
	public void transparentPrism(int index) {
		prismList.get(index).swtichMode();
	}
	
	public void texturePrism(HashSet<Integer> index, int texIndex) {
		if(texIndex<0)
			return;
		
		Iterator<Integer> iterator = index.iterator();
		while(iterator.hasNext())
		{
			prismList.get(iterator.next()).setTextureIndex(texIndex);
		}
		isBitmapReady = true;
	}
	public void texturePrism(int index, int texIndex) {	
		if(texIndex<0)
			return;
		
		prismList.get(index).setTextureIndex(texIndex);	
		isBitmapReady = true;
	}
	
	public void setTextureImage( ArrayList<Bitmap> bitmap ) {
		if(bitmap == null)
			return;
		bmp = bitmap;
		isBitmapReady = true;
	}
	
	private void updateTexture( GL10 gl ) {
		// 텍스쳐 개수만큼 texture handler 등록
		tex_ids = new int[bmp.size()];		
		gl.glGenTextures(bmp.size(),tex_ids, 0);
		
		for(int i=0; i<bmp.size(); i++){
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex_ids[i]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);	
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp.get(i), 0);			
		}
		isBitmapReady = false;
	}
	
	public void setBackground(Bitmap bitmap) {
		bgBmp = bitmap;
		bgBmpReady = true;
		tmpEyeY = 0f;
		tmpEyeZ = 0f;
		tmpRotateZ = 0f;
	}
	
	public void offBackgroundMode() {
		bgMode = false;
		tmpRotateZ = 0f;
	}
	
	public void setRotationAngle(HashSet<Integer> index, int axis, float angle) {

		Iterator<Integer> iterator = index.iterator();
		while(iterator.hasNext())
		{
			prismList.get(iterator.next()).setRotationAngle(axis, angle);
		}
		
		
	}
	public void setRotationAngle(int index, int axis, float angle) {
			prismList.get(index).setRotationAngle(axis, angle);
	}
	
	public void setMovePoint(HashSet<Integer> index, int axis, float number) {
		
		Iterator<Integer> iterator = index.iterator();
		while(iterator.hasNext())
		{
			prismList.get(iterator.next()).setMovingPoint(axis, number);
		}
	}
	public void setMovePoint(int index, int axis, float number) {
			prismList.get(index).setMovingPoint(axis, number);
	}
	
	
	
	
	public float[] convert2dTo3d(float touchX, float touchY) {
		float wx = touchX;
		float wy = this.height - touchY;
		
		//Log.d("wx", "wx 의 값 : " + wx);
		//Log.d("wy", "wy 의 값 : " + wy);
		
		// 140327 그냥 3d좌표로 unproject 하지말고, 1픽셀당 거리 계산해서 처리
		//float [] result = unProject(wx, wy, 1, mMVPMatrix);
		float[] result = new float[3];
		
		float unitX = (realWidth * ((float)radius / oriRadius)) / width;
		float unitY = (realHeight * ((float)radius / oriRadius)) / height;
		
		//Log.d("unitX", "unitX 의 값 : " + unitX);
		//Log.d("unitY", "unitY 의 값 : " + unitY);
		
		float wxNew = wx - (width * 0.5f);
		//Log.d("width", "width 의 값 : " + width);
		//Log.d("height", "height 의 값 : " + height);
		float wyNew = wy - (height * 0.5f);
		
		wxNew *= unitX;
		wyNew *= unitY;
		
		//Log.d("wxNew*= unitX", "wxNew*= unitX 의 값 : " + wxNew);
		//Log.d("wxNew*= unitX", "wxNew*= unitX 의 값 : " + wyNew);
		
		//정면
			//result[2]가 잘못됫다고 가정합시다.	
		//회전
				
		result[0] = 0.0f; // 최초camera axis기준으로 x(depth)는 0
		result[1] = wxNew;
		result[2] = wyNew;
		
		//Log.d("result[0]", "result[0] 의 값 : " + result[0]);
		//Log.d("result[1]", "result[1] 의 값 : " + wxNew);
		//Log.d("result[2]", "result[2] 의 값 : " + wyNew);
		
		float r = (float) Math.sqrt( Math.pow(result[0], 2) + Math.pow(result[1], 2) + Math.pow(result[2], 2) );
		float theta = (float) Math.acos(result[2] / r);		//세타
		float pi = (float) Math.atan(result[1] / result[0]);	//파이
		
		//Log.d("DoRotate", "r값 : "+r+" "+"t값 : "+theta+" "+"p값 : "+pi+" ");
		
		float thetaToDegree = (float) Math.toDegrees(theta);
		//Log.d("thetaToDegree", "thetaToDegree : " + thetaToDegree);		
		if(thetaToDegree>=90 && thetaToDegree <= -90){
			thetaToDegree = thetaToDegree - (float)(oriTheta - vertical); // oriTheta = 90
			Log.d("thetaToDegree", "thetaToDegree 90과 -90이라서 변함 : "+thetaToDegree);
		}
		//thetaToDegree -= (float)(oriTheta - vertical); // oriTheta = 90
		//Log.d("vertical", "vertical : " + vertical);
		if(thetaToDegree<0)
		{
			Log.d("thetaToDegree", "thetaToDegree 0보다 작아서 변했음 : "+thetaToDegree);
			thetaToDegree= -thetaToDegree;
		}
		Log.d("ChangedThetaToDegree", "ChangedThetaToDegree : "+thetaToDegree);
		Log.d("eyeX", "eyeX " + eyeX);
		Log.d("eyeY", "eyeY " + eyeY);
		Log.d("eyeZ", "eyeZ " + eyeZ);
		float piToDegree = (float) Math.toDegrees(pi); // 최초camera axis기준으로 x(depth)는 0이므로, 항상 90
		Log.d("piToDegree", "piToDegree : " + piToDegree);
		
		
		//if(piToDegree<0)
		//	piToDegree=180+piToDegree;
		//Log.d("oriPi & horizontal", "oriPi : " + oriPi +"horizontal: " + horizontal);
		
		piToDegree += (float)(oriPi + horizontal); // oriPi = 0
		//piToDegree -= (float)(oriPi + horizontal); // oriPi = 0
		
		//Log.d("ChangedPiToDegree", "ChangedPiToDegree : " + piToDegree);
				
		float tempX = (float) (r * (float) Math.sin(Math.toRadians(thetaToDegree)) * (float) Math.cos(Math.toRadians(piToDegree)));
		float tempY = (float) (r * (float) Math.sin(Math.toRadians(thetaToDegree)) * (float) Math.sin(Math.toRadians(piToDegree)));
		float tempZ = (float) (r * (float) Math.cos(Math.toRadians(thetaToDegree)));
		
		// 140322 yhchung
		// 깊이가 없는 2d 좌표값을 3d 좌표로 변경하다보니 깊이 값이 문제가 된다.
		// 위에 near_xyz와 far_xyz의 x값이(깊이) 내가 생각하는 값하고 좀 다른데,
		// 아무튼 x값을 무시한 far_xyz의 y,z좌표값이 얼추 원하는 위치값이다. 
		
		result[0] = tempX;
		result[1] = tempY;
		result[2] = tempZ;
		
//		Log.d("prev_result[0]", "prev_result[0] : "+prev_result[0]);
//		Log.d("prev_result[1]", "prev_result[1] : "+prev_result[1]);
//		Log.d("prev_result[1]", "prev_result[2] : "+prev_result[2]);
//		
		Log.d("x", "x 의 값 : " + result[0]);
		Log.d("y", "y 의 값 : " + result[1]);
		Log.d("z", "z 의 값 : " + result[2]);
		
		prevCurr[0] = prev_result[0]-result[0];
		prevCurr[1] = prev_result[1]-result[1];
		prevCurr[2] = prev_result[2]-result[2];
		
//		Log.d("prevCurr[0]", "prevCurr[0] : " +prevCurr[0]);
//		Log.d("prevCurr[1]", "prevCurr[1] : " +prevCurr[1]);
//		Log.d("prevCurr[2]", "prevCurr[2] : " +prevCurr[2]);
//		
		
		
		//if((prev_result[0]==0) && (prev_result[1]==0) && (prev_result[2]==0))
		prev_result=result;
		
//		else
//		{
//			
//		}
//		
		return result;
	}
	
	private  float[] unProject(float winx, float winy, float winz, float[] mvpMatrix) {
		float[] matrix = new float[16];
		float[] in = new float[4];
		float[] out = new float[4];

		Matrix.invertM(matrix, 0, mvpMatrix, 0);

		in[0] = (winx / width) * 2 - 1;
		in[1] = (winy / height) * 2 - 1;
		in[2] = 2 * winz - 1;
		in[3] = 1;

		Matrix.multiplyMV(out, 0, matrix, 0, in, 0);

		if (out[3] == 0) 
			return null;
		
		out[3] = 1 / out[3];

		return new float[] {out[0] * out[3], out[1] * out[3], out[2] * out[3]};			
	}
	public void saveImageFile(GL10 gl, float w, float h) {
		int width = (int)w;
		int height = (int)h;
		
		Bitmap bitmap = createBitmapFromGLSurface(0, 0, width, height, gl);
		
		try {
			String path = imageFilePath;
			FileOutputStream fos = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			bitmap.recycle();
		} 
		catch (Exception e) {
			// handle
		}
	}
	
	private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl)
	        throws OutOfMemoryError {
	    int bitmapBuffer[] = new int[w * h];
	    int bitmapSource[] = new int[w * h];
	    IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
	    intBuffer.position(0);

	    try {
	        gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
	        int offset1, offset2;
	        for (int i = 0; i < h; i++) {
	            offset1 = i * w;
	            offset2 = (h - i - 1) * w;
	            for (int j = 0; j < w; j++) {
	                int texturePixel = bitmapBuffer[offset1 + j];
	                int blue = (texturePixel >> 16) & 0xff;
	                int red = (texturePixel << 16) & 0x00ff0000;
	                int pixel = (texturePixel & 0xff00ff00) | red | blue;
	                bitmapSource[offset2 + j] = pixel;
	            }
	        }
	    } catch(Exception e) {
			System.out.println (e.toString());
			return null;
		}

	    return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
	}
}

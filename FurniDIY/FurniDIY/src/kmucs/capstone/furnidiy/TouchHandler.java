package kmucs.capstone.furnidiy;

import java.util.HashSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchHandler implements OnTouchListener {

	private static final String TAG = "TouchEventHandler";

	private static final int MODE_DEFAULT = 0;
	private static final int MODE_VIEWMOVE = 1;
	private static final int MODE_VIEWZOOM = 2;
	private static final int MODE_ADD = 3;
	private static final int MODE_COPY = 4;
	private static final int MODE_SELECT = 5;
	private static final int MODE_SELECTPOINT = 6;
	private static final int MODE_BACKGROUND = 7;

	protected float[] start_pos;
	protected float[] prev_pos;
	protected float[] curr_pos;
	private int screenWidth;
	private int screenHeight;

	protected EditRenderer renderer;
	protected EditActivity activity;
	protected TouchModeHandler touchModeHanlder;

	private int mode = MODE_DEFAULT;
	private float oldDist = 1f;
	private float newDist = 1f;
	private int prismSize;

	public boolean alreadyGrouped = false;
	public int groupListID = -1;

	boolean checkSelected = false;
	HashSet<Integer> newGroup = new HashSet<Integer>();
	
	public float[] resultBeforeMoving = new float[3];
	public float[] resultAfterMoving = new float[3];
	public float[] dist = new float[3];
	

	public TouchHandler(EditRenderer renderer, int screenHeight,
			int screenWidth, EditActivity activity) {
		this.renderer = renderer;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.activity = activity;
		this.touchModeHanlder=new TouchModeHandler(this.renderer,this.activity,this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		curr_pos = new float[2];
		curr_pos[0] = event.getX();
		curr_pos[1] = event.getY();

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			start_pos = curr_pos;
			prev_pos = curr_pos;
			//resultBeforeMoving=renderer.convert2dTo3d(513,968);
			if (mode == MODE_DEFAULT)
				mode = MODE_VIEWMOVE;
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = space(event);
			newDist = space(event);
			mode = MODE_VIEWZOOM; // zoom mode
			break;

		case MotionEvent.ACTION_UP:
			resultAfterMoving = renderer.convert2dTo3d(513,968);
			
//			dist[0] = resultBeforeMoving[0]-resultAfterMoving[0];
//			dist[1] = resultBeforeMoving[1]-resultAfterMoving[1];
//			dist[2] = resultBeforeMoving[2]-resultAfterMoving[2];
//			
//			Log.d("resultBeforeMoving", "resultBeforeMoving : " + resultBeforeMoving[0] + " " + resultBeforeMoving[1] + " " + resultBeforeMoving[2]);
//			Log.d("resultAfterMoving", "resultAfterMoving : " + resultAfterMoving[0] + " " + resultAfterMoving[1] + " " + resultAfterMoving[2]);
//			Log.d("dist", "dist : " + dist[0] + " " + dist[1] + " " + dist[2]);
			
			if (mode == MODE_ADD) {
				touchModeHanlder.addMode(curr_pos[0],curr_pos[1],prismSize);

			} else if (mode == MODE_COPY) {

			} else if (mode == MODE_SELECT) {

			} else if (mode == MODE_SELECTPOINT) {

			} else if (mode == MODE_VIEWMOVE) {

				if ((renderer.prismList.size() != 0)) {
					for (int i = 0; i < renderer.prismList.size(); i++) {
						if(renderer.prismList.get(i).calued){
							Log.d("몇번째 도형의 좌표가 이런지", (i+1)+"번째 도형의 좌표");
							renderer.prismList.get(i).setVertexDependOnCamera(renderer.horizontal, renderer.radius,renderer.vertical, renderer.eyeX, renderer.eyeY, renderer.eyeZ, renderer.upX, renderer.upY, renderer.upZ,curr_pos[0],curr_pos[1]);
						}
					}
					}
					
				touchModeHanlder.viewMove(curr_pos[0],curr_pos[1]);
				break;
			}
			
			
			
			
			
		case MotionEvent.ACTION_POINTER_UP:
			mode = MODE_DEFAULT;
			break;

		case MotionEvent.ACTION_MOVE:
			if (mode == MODE_VIEWMOVE) {
				Log.i(TAG, "ACTION_MOVE : " + prev_pos[0] + ", " + prev_pos[1]
						+ "->" + curr_pos[0] + ", " + curr_pos[1]);
				float disPosX = curr_pos[0] - prev_pos[0], disPosY = curr_pos[1]- prev_pos[1];
				renderer.moveScreen(disPosX * 0.2f, disPosY * 0.2f);
			} else if (mode == MODE_VIEWZOOM) {
				newDist = space(event);
				float scale = (newDist - oldDist) * 0.02f;
				renderer.zoomScreen(scale * 0.03f);
			} else if (mode == MODE_BACKGROUND) {

			}
			// else if(mode == MODE_MOVE) {
			// Log.i(TAG,
			// "ACTION_MOVE : "+prev_pos[0]+", "+prev_pos[1]+"->"+curr_pos[0]+", "+curr_pos[1]);
			// float disPosX = curr_pos[0]-prev_pos[0], disPosY =
			// curr_pos[1]-prev_pos[1];
			// float distance = FloatMath.sqrt(disPosX*disPosX +
			// disPosY*disPosY);
			// if(disPosX < 0)
			// distance *= -1f;
			// renderer.setMovePoint(tempIndex, selectAxis,distance*0.01f);
			// }
			// else if(mode == MODE_ROTATE) {
			// Log.i(TAG,
			// "ACTION_MOVE : "+prev_pos[0]+", "+prev_pos[1]+"->"+curr_pos[0]+", "+curr_pos[1]);
			// float disPosX = curr_pos[0]-prev_pos[0], disPosY =
			// curr_pos[1]-prev_pos[1];
			// float distance = FloatMath.sqrt(disPosX*disPosX +
			// disPosY*disPosY);
			// if(disPosX < 0)
			// distance *= -1f;
			// renderer.setRotationAngle(tempIndex, selectAxis, distance);
			// }

			prev_pos = curr_pos;
			break;

		default:
			return false;
		}
		return true;
	}

	public float[] getCurr_pos() {
		return curr_pos;
	}

	private float space(MotionEvent e) {
		float x = e.getX(0) - e.getX(1);
		float y = e.getY(0) - e.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public void setAddNumber(int prismSize) {
		this.prismSize = prismSize;
	}

	public int pickedPrismID(float[] result) {
		for (int i = 0; i < renderer.prismList.size(); i++) {
			if (renderer.prismList.get(i).checkPick(result)) {
				activity.isExplorerMode = false;
				activity.setMode();
				checkSelected = true;
				renderer.prismList.get(i).selectPrism(true);
				renderer.prismList.get(i).changeLineColor();
				activity.setSelectedPrism(i);
				return i;
			}
		}

		return -1;
	}
	public int groupPickedPrismID(float[] result)
	{
		for (int i = 0; i < renderer.prismList.size(); i++) {
			if (renderer.prismList.get(i).checkPick(result)) {
				activity.isExplorerMode = false;
				activity.setMode();
				return i;
			}
		}

		return -1;
	}

	public HashSet<Integer> getNewGroup() {
		return newGroup;
	}

	public void setNewGroup(HashSet<Integer> newGroup) {
		this.newGroup = newGroup;
	}

	public void clearGroup() {
		Log.d("pass1", "pass1");
		if(this.groupListID!=-1)
			renderer.groupManager.groupDelete(this.groupListID);
	}

	public int getMode() {
		return mode;
	}

	public static int getModeDefault() {
		return MODE_DEFAULT;
	}

	public static int getModeViewmove() {
		return MODE_VIEWMOVE;
	}

	public static int getModeViewzoom() {
		return MODE_VIEWZOOM;
	}

	public static int getModeAdd() {
		return MODE_ADD;
	}

	public static int getModeCopy() {
		return MODE_COPY;
	}

	public static int getModeSelect() {
		return MODE_SELECT;
	}

	public static int getModeSelectpoint() {
		return MODE_SELECTPOINT;
	}

	public static int getModeBackground() {
		return MODE_BACKGROUND;
	}

}
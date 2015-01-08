package kmucs.capstone.furnidiy;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MoveHandler implements OnTouchListener {

	protected float[] start_pos;
	protected float[] prev_pos;
	protected float[] curr_pos;

	protected EditRenderer renderer;

	public MoveHandler( EditRenderer renderer )	{
		this.renderer = renderer;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		curr_pos = new float[2];
		curr_pos[0] = event.getX();
		curr_pos[1] = event.getY();

		switch(event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			start_pos = curr_pos;
			prev_pos = curr_pos;
			break;
		case MotionEvent.ACTION_UP:		
			start_pos = null;
			prev_pos = null;
			curr_pos = null;			
			break;
		case MotionEvent.ACTION_MOVE:					
			float disPosX = curr_pos[0]-prev_pos[0], disPosY = curr_pos[1]-prev_pos[1];
			renderer.moveObject(disPosX*0.2f, disPosY*0.2f);			
			prev_pos = curr_pos;
			break;			
		default:
			return false;
		}
		return true;
	}	
}

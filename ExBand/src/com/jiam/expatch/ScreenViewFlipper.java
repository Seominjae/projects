package com.jiam.expatch;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 뷰플리퍼의 기능을 담고 있는 클래스를 정의합니다.
 *
 * @author Mike
 */
public class ScreenViewFlipper extends LinearLayout implements OnTouchListener {
	
	private static BitmapFactory.Options option;
	private static Bitmap bitmap;
	private static Drawable d;
	/**
	 * Count of index buttons. Default is 3
	 */
	public static int countIndexes = 3;

	/**
	 * Button Layout
	 */
	LinearLayout buttonLayout;

	/**
	 * Index button images
	 */
	ImageView[] indexButtons;

	/**
	 * Views for the Flipper
	 */
	View[] views;
	
	

	/**
	 * Flipper instance
	 */
    ViewFlipper flipper;

    /**
     * X coordinate for touch down
     */
    float downX;

    /**
     * X coordinate for touch up
     */
    float upX;

    /**
     * Current index
     */
    int currentIndex = 0;


	public ScreenViewFlipper(Context context) {
		super(context);
		
		init(context);
	}

	public ScreenViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
	}

    /**
     * Initialize
     *
     * @param context
     */
	
	
	
	public void init(Context context) {
		
		setBackgroundColor(0x00000000);

		// Layout inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.screenview, this, true);

		buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.setOnTouchListener(this);

		

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = 0;

		indexButtons = new ImageView[countIndexes];
		views = new TextView[countIndexes];
		
		option = new BitmapFactory.Options();
		option.inSampleSize = 1;
		option.inPurgeable = true;
		option.inDither = true;
		
		
		//mLinearLayout = (LinearLayout)findViewById(R.id.FragmentMainTestSecond);

//		bitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.excer01_explain, option);
//		d = new BitmapDrawable(bitmap);
		//mLinearLayout.setBackgroundDrawable(d);
		
		for(int i = 0; i < countIndexes; i++) {
			indexButtons[i] = new ImageView(context);

			if (i == currentIndex) {
				indexButtons[i].setImageResource(R.drawable.green);
			} else {
				indexButtons[i].setImageResource(R.drawable.white);
			}

			indexButtons[i].setPadding(5, 5, 5, 5);
			buttonLayout.addView(indexButtons[i], params);

			TextView curView = new TextView(context);
			curView.setText("View #" + i);
			
			/**
			 * 이지미 변환 하는 부분 수정 필요함
			 */
			if(i==0)
			{
				//curView.setBackgroundResource(R.drawable.backf);
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.mainimg010, option);
				d = new BitmapDrawable(bitmap);
				curView.setBackgroundDrawable(d);
			}
			else if(i==1)
			{
				//curView.setBackgroundResource(R.drawable.backff);
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.mainimg020, option);
				d = new BitmapDrawable(bitmap);
				curView.setBackgroundDrawable(d);
			}
			else if(i==2)
			{
				//curView.setBackgroundResource(R.drawable.backm);
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.mainimg030, option);
				d = new BitmapDrawable(bitmap);
				curView.setBackgroundDrawable(d);
			}
			else
			{
				//curView.setBackgroundResource(R.drawable.backm);
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.mainimg020, option);
				d = new BitmapDrawable(bitmap);
				curView.setBackgroundDrawable(d);
			}

			
			curView.setTextColor(Color.RED);
			curView.setTextSize(32);
			views[i] = curView;

	        flipper.addView(views[i]);
		}
		


	}
    
	/**
	 * Update the display of index buttons
	 */
	private void updateIndexes() {
		for(int i = 0; i < countIndexes; i++) {
			if (i == currentIndex) {
				indexButtons[i].setImageResource(R.drawable.green);
			} else {
				indexButtons[i].setImageResource(R.drawable.white);
			}
		}
	}

	/**
	 * onTouch event handling
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if(v != flipper) return false;

		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = event.getX();
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			upX = event.getX();

			if( upX < downX ) {  // in case of right direction
 
				flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
		        		R.anim.push_left_in));
		        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
		        		R.anim.push_left_out));

		        if (currentIndex < (countIndexes-1)) {
		        	flipper.showNext();

		        	// update index buttons
		        	currentIndex++;
		        	updateIndexes();
		        }
			} else if (upX > downX){ // in case of left direction

				flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
		        		R.anim.push_right_in));
		        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
		        		R.anim.push_right_out));

		        if (currentIndex > 0) {
		        	flipper.showPrevious();

		        	// update index buttons
		        	currentIndex--;
		        	updateIndexes();
		        }
			}
		}

		return true;
	}
 
}

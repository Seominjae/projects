package com.jiam.touchriding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/*
 * 2014-06-02
 */
public class SquareLayout extends LinearLayout {
	
	public SquareLayout(Context context) {
	    super(context);
	}
	
	public SquareLayout(Context context, AttributeSet attrs) {
	    super(context, attrs);	
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		if(widthMeasureSpec > heightMeasureSpec)
			super.onMeasure(heightMeasureSpec, heightMeasureSpec);
		else
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);	
	}

}

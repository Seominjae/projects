package com.jiam.touchriding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class PassportActivity extends Activity {
	
	private final static int MAX_SPOTS = 6;
	ImageButton []spots;
	User user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_passport);
		
		user = new User();		
		Intent intent = getIntent();
		user = (User)intent.getSerializableExtra("user_info");
		
		initWidgets();
		setImage();		
	}
	
	/*
	 * 2014-06-23
	 * bind image buttons 
	 */
	private void initWidgets() {
		spots = new ImageButton[MAX_SPOTS];
		
		for(int i=0; i<MAX_SPOTS; i++) {
			final int temp = i;
			spots[i] = (ImageButton) findViewById(R.id.stamp01 + i);
			spots[i].setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					toSpotActivity(temp);
				}
			});
		}
	}
	
	/*
	 * 2014-06-23
	 * bind image buttons 
	 */
	private void toSpotActivity(int spotId) {
		Intent mIntent = new Intent(getApplicationContext(), SpotActivity.class);
		mIntent.putExtra("spot_number", spotId+1);
		mIntent.putExtra("user_info", user);
		startActivity(mIntent);
		finish();
	}	
	
	/*
	 * 2014-06-08
	 * set image on imagebuttons
	 */
	private void setImage() {
		
		for(int i=0; i<user.getIsVisited().length; i++) {
			if(user.getIsVisited()[i])
				spots[i].setBackgroundResource(R.drawable.num1_certified + i*2);
			else
				spots[i].setBackgroundResource(R.drawable.num1 + i*2);
		}
	}
}
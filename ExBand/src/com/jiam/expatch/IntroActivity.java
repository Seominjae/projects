package com.jiam.expatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class IntroActivity extends Activity {
//Handler handler;
	
	//private ImageView mSignView;
	private Button mSignButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		
		
		
		mSignButton = (Button)findViewById(R.id.sign_button);
		mSignButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(IntroActivity.this, LobbyActivity.class);
				startActivity(i);
			}
		});
		
		
		
		
		
		//handler = new Handler();
		//handler.postDelayed(irun, 1000);
	}
	
//	Runnable irun = new Runnable() {
//		public void run() {
//			Intent mIntent = new Intent(IntroActivity.this, LobbyActivity.class);
//			startActivity(mIntent);
//			finish();
//			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//		}
//	};
	
	
	
	
//	//back key
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		handler.removeCallbacks(irun);
//	}
}

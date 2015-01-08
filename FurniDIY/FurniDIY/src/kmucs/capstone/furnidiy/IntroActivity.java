package kmucs.capstone.furnidiy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import com.google.cloud.backend.R;

public class IntroActivity extends Activity {
	
	Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		
		handler = new Handler();
		handler.postDelayed(irun, 2000);
	}
	
	Runnable irun = new Runnable() {
		public void run() {
			Intent intent = new Intent(IntroActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
	};
	
	//back key
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.removeCallbacks(irun);
	}
}
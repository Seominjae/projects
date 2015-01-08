
package kmucs.capstone.furnidiy;

import android.app.Activity;

import com.google.cloud.backend.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageButton startBtn = (ImageButton) this.findViewById(R.id.bt1); 
		ImageButton loadBtn = (ImageButton) this.findViewById(R.id.bt2); 
		ImageButton boardBtn = (ImageButton) this.findViewById(R.id.bt3); 
		ImageButton exitBtn = (ImageButton) this.findViewById(R.id.bt4); 
		
		startBtn.setBackgroundResource(R.drawable.start_btn);
		loadBtn.setBackgroundResource(R.drawable.load_btn);
		boardBtn.setBackgroundResource(R.drawable.board_btn);
		exitBtn.setBackgroundResource(R.drawable.exit_btn);
		
		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), EditActivity.class);
				startActivity(myIntent);
			}
		}); 
		
		loadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), FileOpenActivity.class);
				startActivity(myIntent);
			}
		}); 
		
		boardBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), TabMainActivity.class);
				startActivity(myIntent);
			}
		}); 
		
		exitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		}); 
		
		
	
	}


}

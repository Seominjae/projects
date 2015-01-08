package com.jiam.touchriding;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final int REQUEST_CODE_NFC_READ = 1003;
	
	User user;
	DBSpotHelper db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//get user information from intent
		user = new User();		
		Intent intent = getIntent();
		try {
			user = (User)intent.getSerializableExtra("user_info");
		}
		catch(Exception e) {
			e.printStackTrace();
			user = new User("guest@email.com");
		}
			
		initWidgets();		
		displayUserInfo();
		createDB(this);		
	}
	
	
	/*
	 * 2014-06-23
	 * display user information
	 */
	private void displayUserInfo() {
		String userInfoText = "";
		TextView textview_userinfo = (TextView)findViewById(R.id.text_userinfo);
		userInfoText += "Welcome\nEmail: " + user.getEmail() 
					 + "\nNickname: " + user.getNickname() 
					 + "\n최다 방문 장소: " 
					 + "\n최근 방문 시간: ";
		textview_userinfo.setText(userInfoText);		
	}

	/*
	 * 2014-06-06
	 * bind Buttons (passport, map, nfcread) and set OnClickListener
	 */
	private void initWidgets() {
		Button passportBtn = (Button)findViewById(R.id.btn_passport);
		passportBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mIntent = new Intent(getApplicationContext(), PassportActivity.class);
				mIntent.putExtra("user_info", user);
				startActivity(mIntent);
			}
		});	
		
		Button mapBtn = (Button)findViewById(R.id.btn_map);
		mapBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mIntent = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(mIntent);
			}
		});	
		
		Button nfcreadBtn = (Button)findViewById(R.id.btn_nfcread);
		nfcreadBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mIntent = new Intent(getApplicationContext(), NfcReadActivity.class);
				mIntent.putExtra("user_info", user);
				startActivityForResult(mIntent, REQUEST_CODE_NFC_READ);
			}
		});	
	}
	
	
	/*
	 * 2014-06-08
	 * create Database and print log
	 */
	private void createDB(Context context) {
		db = new DBSpotHelper(context);
				
		Log.d("Reading: ", "Reading all spots..");
		List<Spot> spots = db.getAllSpot();
		
		for(Spot s : spots) {
			String log = "Id: " + s.getId() + ", Name: " + s.getName() + " ,NFC ID: " + s.getNfcId();
			Log.d("Name: ", log);
		}
	}
	
	
	/*
	 * 2014-06-06
	 * This is getting result from another activity which is started on MainActivity
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
		super.onActivityResult(requestCode, resultCode, Data);
		
		if(requestCode == REQUEST_CODE_NFC_READ && Data != null) {
			int spot_id = Data.getIntExtra("spot_id", -1);
			user.setIsVisited(spot_id);
		}
	}
}

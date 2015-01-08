package com.jiam.expatch;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MainActivity extends Activity {
	
	private User user;
//	int selectedMenu=0;
	private static String FRAGMENT_FIRST="FRAGMENT_FIRST";
	private static String FRAGMENT_SECOND="FRAGMENT_SECOND";
	private static String FRAGMENT_THREE="FRAGMENT_THREE";
	private static String FRAGMENT_FOUR="FRAGMENT_FOUR";
	private static String FRAGMENT_FIVE="FRAGMENT_FIVE";
	private static LinearLayout mLinearLayout;
	private static Bitmap bitmap;
	private static Drawable d;
	
	FragmentMainMainpage fragmentMainMainpage = new FragmentMainMainpage();
	FragmentMainHomePlan fragmentMainHomePlan = new FragmentMainHomePlan();
	FragmentMainHomeMypage fragmentMainHomeMypage = new FragmentMainHomeMypage();
	FragmentMainConfigure fragmentMainConfigure = new FragmentMainConfigure();
	FragmentMainBluetoothTest fragmentMainBluetoothTest = new FragmentMainBluetoothTest();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		
		
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inSampleSize = 1;
		option.inPurgeable = true;
		option.inDither = true;
		
		mLinearLayout = (LinearLayout)findViewById(R.id.activitymain);
		//bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backff);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backffff, option);
		
		d = new BitmapDrawable(bitmap);
		mLinearLayout.setBackgroundDrawable(d);
		
		
		 
		
		
//		//ImageView create
//				ImageView iv = new ImageView(this);
//				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backff);
//				iv.setImageBitmap(bitmap);
//				iv.setAdjustViewBounds(true);
//				iv.setLayoutParams(new LayoutParams(
//						android.widget.LinearLayout.LayoutParams.MATCH_PARENT
//						,android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
//				iv.setScaleType(ScaleType.FIT_XY);
//				
////		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.excer01_act1);
//		d = new BitmapDrawable(bitmap);
//		mLinearLayout.setBackground(d);
		
		//setContentView(R.layout.activity_main);
		initWidgets();
		changeView();
		//displayUserInfo();
	}

	private void changeView() {
		// TODO Auto-generated method stub
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.FragmentMainHome, fragmentMainMainpage,FRAGMENT_FIRST);
		ft.commit();
	}

	/*
	 * 2014-06-23
	 * display user information
	 */
	private void displayUserInfo() {
		//TextView textview_userinfo = (TextView)findViewById(R.id.text_userinfo);
		//textview_userinfo.setText("EXPATCH\n\nWelcome " + user.getName()+"!");
	}

	/*
	 * 2014-06-06
	 * initiate widgets
	 */
	private void initWidgets() {
		Button mainpageBtn = (Button)findViewById(R.id.btn_mainpage);
		mainpageBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Log.d(CLASSNAME,"onClick R.id.btnFirst");
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.FragmentMainHome,fragmentMainMainpage,FRAGMENT_FIRST);
				ft.addToBackStack(null);
				ft.commit();
		    	Toast.makeText(getApplicationContext(), "mainpage", Toast.LENGTH_SHORT).show();
			}
		});	
		
		Button mypageBtn = (Button)findViewById(R.id.btn_mypage);
		mypageBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//Log.d(CLASSNAME,"onClick R.id.btnFirst");
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.FragmentMainHome,fragmentMainHomeMypage,FRAGMENT_THREE);
				ft.addToBackStack(null);
				ft.commit();
		    	Toast.makeText(getApplicationContext(), "mypage", Toast.LENGTH_SHORT).show();
			}
		});	
		
		Button planningBtn = (Button)findViewById(R.id.btn_planning);
		planningBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.FragmentMainHome,fragmentMainHomePlan,FRAGMENT_SECOND);
				ft.addToBackStack(null);
				ft.commit();	
		    	Toast.makeText(getApplicationContext(), "plan", Toast.LENGTH_SHORT).show();
			}
		});	
		
		Button testBtn = (Button)findViewById(R.id.btn_test);
		testBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.FragmentMainHome,fragmentMainBluetoothTest,FRAGMENT_FOUR);
				ft.addToBackStack(null);
				ft.commit();	
		    	Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
			}
		});	
		
		Button configBtn = (Button)findViewById(R.id.btn_config);
		configBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.FragmentMainHome,fragmentMainConfigure,FRAGMENT_FIVE);
				ft.addToBackStack(null);
				ft.commit();	
		    	Toast.makeText(getApplicationContext(), "configure", Toast.LENGTH_SHORT).show();
			}
		});
	}	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bitmap.recycle();
		bitmap = null;
		System.gc();
		super.onDestroy();
	}
}
package com.jiam.expatch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class LobbyActivity extends Activity {

	private static final int REQUEST_CODE_REGISTER = 1001;

	private static final String SHARED_PREFERENCES_NAME = "EXPATCH_PREFERENCES";

	private static final String USER01_EXIST = "EXPATCH_USER01_EXIST";
	private static final String USER01_NAME = "EXPATCH_USER01_NAME";
	private static final String USER01_AGE = "EXPATCH_USER01_AGE";
	private static final String USER01_SEX = "EXPATCH_USER01_SEX";
	private static final String USER01_HEIGHT = "EXPATCH_USER01_HEIGHT";
	private static final String USER01_WEIGHT = "EXPATCH_USER01_WEIGHT";

	private static final String USER02_EXIST = "EXPATCH_USER02_EXIST";
	private static final String USER02_NAME = "EXPATCH_USER02_NAME";
	private static final String USER02_AGE = "EXPATCH_USER02_AGE";
	private static final String USER02_SEX = "EXPATCH_USER02_SEX";
	private static final String USER02_HEIGHT = "EXPATCH_USER02_HEIGHT";
	private static final String USER02_WEIGHT = "EXPATCH_USER02_WEIGHT";

	private static final String USER03_EXIST = "EXPATCH_USER03_EXIST";
	private static final String USER03_NAME = "EXPATCH_USER03_NAME";
	private static final String USER03_AGE = "EXPATCH_USER03_AGE";
	private static final String USER03_SEX = "EXPATCH_USER03_SEX";
	private static final String USER03_HEIGHT = "EXPATCH_USER03_HEIGHT";
	private static final String USER03_WEIGHT = "EXPATCH_USER03_WEIGHT";
	
	private static final String[][] userInfo = {
			{USER01_EXIST,USER01_NAME,USER01_AGE,USER01_SEX,USER01_HEIGHT,USER01_WEIGHT},
			{USER02_EXIST,USER02_NAME,USER02_AGE,USER02_SEX,USER02_HEIGHT,USER02_WEIGHT},
			{USER03_EXIST,USER03_NAME,USER03_AGE,USER03_SEX,USER03_HEIGHT,USER03_WEIGHT}
	};
	
	// private TextView textUser01, textUser02, textUser03;
	private Button userBtn01, userBtn02, userBtn03;
	
	
	
	// private ImageView imgUser01, imgUser02, imgUser03;

	private SharedPreferences sp;

	private User user;
	private int selectSlot = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lobby);

		sp = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		user = new User();
		//findViewById(R.id.user01).setOnLongClickListener(mListener);

		initWidgets();
		displayUser();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		Log.d("sp.getBoolean(USER03_EXIST, false)", "sp.getBoolean(USER03_EXIST, false) : " + sp.getBoolean(USER03_EXIST, false));
		
		if (sp.getBoolean(USER01_EXIST, false))
			if (v == userBtn01) {
				menu.add(0,1,0, sp.getString(USER01_NAME, "EMPTY")+" delete");
			}
		else if (sp.getBoolean(USER02_EXIST, false))
			if (v == userBtn02) {
				menu.add(0,2,0, sp.getString(USER02_NAME, "EMPTY")+" delete");
			}
		else if (sp.getBoolean(USER03_EXIST, false))
			Log.d("v == userBtn03", "v == userBtn03 : " + (v == userBtn03) );
			if (v == userBtn03) {
				menu.add(0,3,0, sp.getString(USER03_NAME, "EMPTY")+" delete");
			}
	}
//	
//	OnLongClickListener mListener = new OnLongClickListener() {
//
//		@Override
//		public boolean onLongClick(View v) {
//
//			SharedPreferences.Editor editor = sp.edit();
//			DBPlanHelper db = new DBPlanHelper(getApplicationContext());
//
//			db.deleteUserPlans(sp.getString(USER01_NAME, "Guest"));
//			editor.putBoolean(USER01_EXIST, false);
//			editor.commit();
//
//			displayUser();
//
//			return true;
//		}
//	};

	/*
	 * 2014-07-18 Display user
	 */
	private void displayUser() {

		if (sp.getBoolean(USER01_EXIST, false)) {
			userBtn01.setText(sp.getString(USER01_NAME, "EMPTY"));
			//userBtn01.setBackgroundResource(R.drawable.peobtn);
			if(selectSlot==1){
				userBtn01.setBackgroundResource(R.drawable.peobtn);
			}
			else{
				userBtn01.setBackgroundResource(R.drawable.userselected);
			}
		} else {
			userBtn01.setText("Add User");
			userBtn01.setBackgroundResource(R.drawable.addbtn);

		}

		if (sp.getBoolean(USER02_EXIST, false)) {
			userBtn02.setText(sp.getString(USER02_NAME, "EMPTY"));
			if(selectSlot==2){
				userBtn02.setBackgroundResource(R.drawable.peobtn);
			}
			else{
				userBtn02.setBackgroundResource(R.drawable.userselected);
			}
		} else {
			userBtn02.setText("Add User");
			userBtn02.setBackgroundResource(R.drawable.addbtn);
		}

		if (sp.getBoolean(USER03_EXIST, false)) {
			userBtn03.setText(sp.getString(USER03_NAME, "EMPTY"));
			if(selectSlot==3){
				userBtn03.setBackgroundResource(R.drawable.peobtn);
			}
			else{
				userBtn03.setBackgroundResource(R.drawable.userselected);
			}
		} else {
			userBtn03.setText("Add User");
			userBtn03.setBackgroundResource(R.drawable.addbtn);
		}
	}

	/*
	 * 2014-07-18 initiate widgets
	 */
	private void initWidgets() {
		// LinearLayout layoutUser01 = (LinearLayout)findViewById(R.id.user01);
		userBtn01 = (Button) findViewById(R.id.text_user01);
		// imgUser01 = (ImageView)findViewById(R.id.img_user01);
		// textUser01 = (TextView)findViewById(R.id.text_user01);

		// LinearLayout layoutUser02 = (LinearLayout)findViewById(R.id.user02);
		userBtn02 = (Button) findViewById(R.id.text_user02);
		// imgUser02 = (ImageView)findViewById(R.id.img_user02);
		// textUser02 = (TextView)findViewById(R.id.text_user02);

		// LinearLayout layoutUser03 = (LinearLayout)findViewById(R.id.user03);
		userBtn03 = (Button) findViewById(R.id.text_user03);
		// imgUser03 = (ImageView)findViewById(R.id.img_user03);
		// textUser03 = (TextView)findViewById(R.id.text_user03);
		
		registerForContextMenu(userBtn01);
		registerForContextMenu(userBtn02);
		registerForContextMenu(userBtn03);
		//Button btnOk = (Button) findViewById(R.id.lobby_btn_ok);
		//Button btnDel = (Button) findViewById(R.id.lobby_btn_del);

		// userBtn01.setOnLongClickListener(new OnLongClickListener() {

		
		
		

		 

		// userBtn01.setOnLongClickListener(new OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View v) {
		// // TODO Auto-generated method stub
		// return false;
		// }
		// });

		userBtn01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectSlot==1)
					checkGoToMainActivity(1);
				
				if (sp.getBoolean(USER01_EXIST, false)) {
					selectSlot(1);
					displayUser();
				} else {
					selectSlot = 1;
					Intent intent = new Intent(getApplicationContext(),
							RegisterActivity.class);
					displayUser();

					startActivityForResult(intent, REQUEST_CODE_REGISTER);
				}
			}
		});
		
		
		userBtn02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectSlot==2)
				checkGoToMainActivity(2);
				
				if (sp.getBoolean(USER02_EXIST, false)) {
					selectSlot(2);
					displayUser();
				} else {
					selectSlot = 2;
					Intent intent = new Intent(getApplicationContext(),
							RegisterActivity.class);
					displayUser();
					startActivityForResult(intent, REQUEST_CODE_REGISTER);
				}
			}
		});

		userBtn03.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectSlot==3)
				checkGoToMainActivity(3);
				
				if (sp.getBoolean(USER03_EXIST, false)) {
					selectSlot(3);
					displayUser();
				} else {
					selectSlot = 3;
					Intent intent = new Intent(getApplicationContext(),
							RegisterActivity.class);
					displayUser();
					startActivityForResult(intent, REQUEST_CODE_REGISTER);
				}
			}
		});

//		btnOk.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (selectSlot == 1) {
//					user.setName(sp.getString(USER01_NAME, "Guest"));
//					user.setAge(sp.getInt(USER01_AGE, 20));
//					user.setSex(sp.getString(USER01_SEX, "Male"));
//					user.setHeight(sp.getFloat(USER01_HEIGHT, 180f));
//					user.setWeight(sp.getFloat(USER01_WEIGHT, 75f));
//					Intent mIntent = new Intent(getApplicationContext(),
//							MainActivity.class);
//					mIntent.putExtra("user_info", user);
//					//selectSlot = 0;
//					startActivity(mIntent);
//				} else if (selectSlot == 2) {
//					user.setName(sp.getString(USER02_NAME, "Guest"));
//					user.setAge(sp.getInt(USER02_AGE, 20));
//					user.setSex(sp.getString(USER02_SEX, "Male"));
//					user.setHeight(sp.getFloat(USER02_HEIGHT, 180f));
//					user.setWeight(sp.getFloat(USER02_WEIGHT, 75f));
//					Intent mIntent = new Intent(getApplicationContext(),
//							MainActivity.class);
//					mIntent.putExtra("user_info", user);
//					//selectSlot = 0;
//					startActivity(mIntent);
//				} else if (selectSlot == 3) {
//					user.setName(sp.getString(USER03_NAME, "Guest"));
//					user.setAge(sp.getInt(USER03_AGE, 20));
//					user.setSex(sp.getString(USER03_SEX, "Male"));
//					user.setHeight(sp.getFloat(USER03_HEIGHT, 180f));
//					user.setWeight(sp.getFloat(USER03_WEIGHT, 75f));
//					Intent mIntent = new Intent(getApplicationContext(),
//							MainActivity.class);
//					mIntent.putExtra("user_info", user);
//					//selectSlot = 0;
//					startActivity(mIntent);
//				} else
//					showMessage("Please select user.");
//			}
//		});
		
		
//		btnDel.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				SharedPreferences.Editor editor = sp.edit();
//				DBPlanHelper db = new DBPlanHelper(getApplicationContext());
//				
//				
//				db.deleteUserPlans(sp.getString(userInfo[selectSlot-1][1], "Guest"));
//				editor.putBoolean(userInfo[selectSlot-1][0], false);
//				editor.commit();
//				
////				if (selectSlot == 1) {
////					db.deleteUserPlans(sp.getString(USER01_NAME, "Guest"));
////					editor.putBoolean(USER01_EXIST, false);
////					editor.commit();
////				} else if (selectSlot == 2) {
////					db.deleteUserPlans(sp.getString(USER02_NAME, "Guest"));
////					editor.putBoolean(USER02_EXIST, false);
////					editor.commit();
////				} else if (selectSlot == 3) {
////					db.deleteUserPlans(sp.getString(USER03_NAME, "Guest"));
////					editor.putBoolean(USER03_EXIST, false);
////					editor.commit();
////				} else
////					showMessage("Please select user.");
//
//				displayUser();
//			}
//		});
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		SharedPreferences.Editor editor = sp.edit();
		DBPlanHelper db = new DBPlanHelper(getApplicationContext());
		
		
		db.deleteUserPlans(sp.getString(userInfo[item.getItemId()-1][1], "Guest"));
		editor.putBoolean(userInfo[item.getItemId()-1][0], false);
		editor.commit();
		
		displayUser();
		
		switch (item.getItemId()) {
		case 1:
			showMessage(sp.getString(USER01_NAME, "EMPTY")+" deleted");
			return true;
		case 2:
			showMessage(sp.getString(USER02_NAME, "EMPTY")+" deleted");
			return true;
		case 3:
			showMessage(sp.getString(USER03_NAME, "EMPTY")+" deleted");
			return true;
		}
		return super.onContextItemSelected(item);
	}
	/*
	 * 2014-07-18 change slot image (select/unselect)
	 */
	private void selectSlot(int slotNumber) {
		// temporary message
		if (selectSlot != slotNumber) {
			selectSlot = slotNumber;
			if(selectSlot==1)
				showMessage("Select User " + sp.getString(USER01_NAME, "EMPTY"));
			if(selectSlot==2)
				showMessage("Select User " + sp.getString(USER02_NAME, "EMPTY"));
			if(selectSlot==3)
				showMessage("Select User " + sp.getString(USER03_NAME, "EMPTY"));
			
			
		} 
//		else {
//			showMessage("Unselect");
//			selectSlot = 0;
//		}

	}

	/*
	 * 2014-07-18 get result from RegisterActivity and set user
	 * information
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_REGISTER && data != null) {
			User tmpUser = new User();
			tmpUser = (User) data.getSerializableExtra("user_info");
			
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean(userInfo[selectSlot-1][0], true);
			editor.putString(userInfo[selectSlot-1][1], tmpUser.getName());
			editor.putInt(userInfo[selectSlot-1][2], tmpUser.getAge());
			editor.putString(userInfo[selectSlot-1][3], tmpUser.getSex());
			editor.putFloat(userInfo[selectSlot-1][4], tmpUser.getHeight());
			editor.putFloat(userInfo[selectSlot-1][5], tmpUser.getWeight());
			editor.commit();
			
//			if (selectSlot == 1) {
//				SharedPreferences.Editor editor = sp.edit();
//				editor.putBoolean(USER01_EXIST, true);
//				editor.putString(USER01_NAME, tmpUser.getName());
//				editor.putInt(USER01_AGE, tmpUser.getAge());
//				editor.putString(USER01_SEX, tmpUser.getSex());
//				editor.putFloat(USER01_HEIGHT, tmpUser.getHeight());
//				editor.putFloat(USER01_WEIGHT, tmpUser.getWeight());
//				editor.commit();
//			} else if (selectSlot == 2) {
//				SharedPreferences.Editor editor = sp.edit();
//				editor.putBoolean(USER02_EXIST, true);
//				editor.putString(USER02_NAME, tmpUser.getName());
//				editor.putInt(USER02_AGE, tmpUser.getAge());
//				editor.putString(USER02_SEX, tmpUser.getSex());
//				editor.putFloat(USER02_HEIGHT, tmpUser.getHeight());
//				editor.putFloat(USER02_WEIGHT, tmpUser.getWeight());
//				editor.commit();
//			} else {
//				SharedPreferences.Editor editor = sp.edit();
//				editor.putBoolean(USER03_EXIST, true);
//				editor.putString(USER03_NAME, tmpUser.getName());
//				editor.putInt(USER03_AGE, tmpUser.getAge());
//				editor.putString(USER03_SEX, tmpUser.getSex());
//				editor.putFloat(USER03_HEIGHT, tmpUser.getHeight());
//				editor.putFloat(USER03_WEIGHT, tmpUser.getWeight());
//				editor.commit();
//			}
			//selectSlot = 0;
			displayUser();
		}
	}

	/*
	 * 2014-06-26 show toast message
	 */
	private void showMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	private void checkGoToMainActivity(int i) {
		// TODO Auto-generated method stub
		
		user.setName(sp.getString(userInfo[i - 1][1], "Guest"));
		user.setAge(sp.getInt(userInfo[i - 1][2], 20));
		user.setSex(sp.getString(userInfo[i - 1][3], "Male"));
		user.setHeight(sp.getFloat(userInfo[i - 1][4], 180f));
		user.setWeight(sp.getFloat(userInfo[i - 1][5], 75f));

		Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
		mIntent.putExtra("user_info", user);
		// selectSlot = 0;
		startActivity(mIntent);

	}
}

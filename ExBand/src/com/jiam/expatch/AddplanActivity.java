package com.jiam.expatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddplanActivity extends Activity {
	
	private Spinner exerSpinner;
	private EditText timesText;
	private int id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//blur background
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);
		
		setContentView(R.layout.activity_addplan);
		
		Intent intent = getIntent();
		id = intent.getIntExtra("list_number", 0) + 1;
		
		initWidgets();
	}
	
	/*
	 * 2014-07-18
	 * initiate widgets
	 */
	private void initWidgets() {
		exerSpinner = (Spinner)findViewById(R.id.exercise_spinner);
		timesText = (EditText)findViewById(R.id.times_text);
		Button okBtn = (Button)findViewById(R.id.ok_btn);
		Button cancleBtn = (Button)findViewById(R.id.cancle_btn);
		
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(checkForm()) {
					Intent intent = new Intent();
					intent.putExtra("plan_info", getPlan());
					setResult(-1, intent);
					finish();
				}
			}
		});
		
		cancleBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(1, intent);
				finish();
			}
		});
	}	
	
	/*
	 * 2014-07-18
	 * check valid form
	 */
	private boolean checkForm() {
		try {
			Integer.parseInt(timesText.getText().toString());
		}
		catch(NumberFormatException e) {
			showMessage("횟수 입력 오류");
			return false;
		}
		
		if(exerSpinner.getSelectedItem().equals("Select")) {
			showMessage("운동 종류를 선택해주세요.");
			exerSpinner.requestFocus();
			return false;
		}
		return true;
	}
	
	/*
	 * 2014-07-18
	 * get Exercise
	 */
	private Plan getPlan() {
		Plan plan = new Plan();
		
		plan.setId(id);
		plan.setName(exerSpinner.getSelectedItem().toString());
		plan.setTimes(Integer.parseInt(timesText.getText().toString()));
		
		return plan;
	}
	
	/*
	 * 2014-06-26
	 * show toast message
	 */
	private void showMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}

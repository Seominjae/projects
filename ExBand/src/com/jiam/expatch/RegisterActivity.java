package com.jiam.expatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	EditText nameText, ageText, heightText, weightText;
	User user;
	Spinner spinnerSex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);		
		
		user = new User();
		initWidgets();			
	}	
	
	/*
	 * 2014-06-08
	 * bind Button and set OnClickListener
	 */
	private void initWidgets() {	
		
		nameText = (EditText) findViewById(R.id.text_name);
		ageText = (EditText) findViewById(R.id.text_age);	
		heightText = (EditText) findViewById(R.id.text_height);		
		weightText = (EditText) findViewById(R.id.text_weight);	
		spinnerSex = (Spinner)findViewById(R.id.spinner_sex);	
		
		Button registerBtn = (Button)findViewById(R.id.btn_register);
		
		registerBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {				
				
				if(checkForm()) {
					user.setName(nameText.getText().toString());
					user.setAge(Integer.parseInt(ageText.getText().toString()));
					user.setSex(spinnerSex.getSelectedItem().toString());
					user.setHeight(Float.parseFloat(heightText.getText().toString()));
					user.setWeight(Float.parseFloat(weightText.getText().toString()));
					
					Intent intent = new Intent();
					intent.putExtra("user_info", user);
					setResult(-1, intent);
					finish();
				}	
			}
		});
	}	
	
	/*
	 * 2014-07-17
	 * check valid form
	 * (need to use TextWatcher)
	 */
	private boolean checkForm() {
		
		try {
			Integer.parseInt(ageText.getText().toString());
		}
		catch (NumberFormatException e) {
			showMessage("나이 입력 오류");
			return false;
		}
		
		if(spinnerSex.getSelectedItem().equals("Select")) {
			showMessage("성별을 선택해주세요.");
			spinnerSex.requestFocus();
			return false;
		}
		
		try {
			Float.parseFloat(heightText.getText().toString());
		}
		catch (NumberFormatException e) {
			showMessage("키 입력 오류");
			return false;
		}
		
		try {
			Float.parseFloat(weightText.getText().toString());
		}
		catch (NumberFormatException e) {
			showMessage("몸무게 입력 오류");
			return false;
		}
		
		return true;
	}
	
	/*
	 * 2014-06-26
	 * show toast message
	 */
	private void showMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}
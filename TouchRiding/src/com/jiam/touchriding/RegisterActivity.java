package com.jiam.touchriding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	EditText text_id, text_pw, text_nickname;
	User user;
	Spinner spinnerAge, spinnerSex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		text_id = (EditText) findViewById(R.id.text_id);
		text_pw = (EditText) findViewById(R.id.text_pw);	
		text_nickname = (EditText) findViewById(R.id.text_nickname);
		
		initWidgets();			
	}	
	
	/*
	 * 2014-06-08
	 * bind Button and set OnClickListener
	 */
	private void initWidgets() {	
		
		spinnerAge = (Spinner)findViewById(R.id.spinner_age);
		spinnerSex = (Spinner)findViewById(R.id.spinner_sex);		
		
		Button registerBtn = (Button)findViewById(R.id.btn_register);
		registerBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				boolean validForm;
				
				if(spinnerAge.getSelectedItem().equals("Select")) {
					showMessage("Please select age.");
					spinnerAge.requestFocus();
					return;
				}
				else if(spinnerSex.getSelectedItem().equals("Select")) {
					showMessage("Please select sex.");
					spinnerSex.requestFocus();
					return;
				}				
				
				validForm = Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", text_id.getText().toString());
				
				if(!validForm)
					showMessage("Please match email form");				
				else
					new registerTask().execute();				
			}
		});
	}
	
	/*
	 * 2014-06-26
	 * AsyncTask for register user account
	 */
	private class registerTask extends AsyncTask<Void, Void, Character> {

		private String nickname;
		private String id;
		private String password;
		private String age;
		private String sex;
		private char state;
		private Socket socket;
		private BufferedOutputStream outstream;
		private BufferedInputStream instream;
		
		public registerTask() {
			this.nickname = text_nickname.getText().toString();
			this.id = text_id.getText().toString();
			this.password = text_pw.getText().toString();
			this.age = (String)spinnerAge.getAdapter().getItem(spinnerAge.getSelectedItemPosition());
			this.sex = (String)spinnerSex.getAdapter().getItem(spinnerSex.getSelectedItemPosition());
			this.state = 'n';
			
			if(sex.equals("Male"))//male is 1
				sex="1";
			else
				sex="0";
		}		
				
		@Override
		protected Character doInBackground(Void... params) {
			
			try {
				//connect
				socket = new Socket("210.121.154.95", 4002);
				System.out.println("Socket OK");
				outstream = new BufferedOutputStream(socket.getOutputStream());
				System.out.println("Outstream OK");
				instream = new BufferedInputStream(socket.getInputStream());
				System.out.println("Instream OK");
				
				//registration. "s" is header, means "simple registration."
				String data = "s".concat("+").concat(nickname).concat("+").concat(id).concat("+").concat(password).concat("+").concat(age).concat("+").concat(sex);
				byte[] ref = data.getBytes("UTF-8");
				
				System.out.println("Byte alloc OK");
				outstream.write(ref);
				outstream.flush();
				
				int stat = instream.read();
				System.out.println("Stat is : " + stat);
				if(stat != -1)
					this.state = (char)stat;
			
			} catch (Exception e) {
				this.state = 'n';
				e.printStackTrace();
			}			
			
			System.out.println("srreceive ����");
			System.out.println(getStat());			
						
			return getStat();
		}
		
		@Override
		protected void onPostExecute(Character stat) {
			if(stat == 's'){
				socketClose();
				System.out.println("Success");
				
				Intent myIntent = new Intent();
				myIntent.putExtra("user_id", getId());
				setResult(-1, myIntent);
				finish();
			}
			else if(stat == 'f'){
				System.out.println("Failed : DB");
				showMessage("No available nickname. please retry.");
			}
			else if(stat == 'n'){
				System.out.println("Failed : disconnection");
				showMessage("Sever disconnection.");
			}
		}
		
		public String getId() {
			return this.id;
		}
		
		public char getStat() {
			return this.state;
		}
		
		public void socketClose() {
			try {
				socket.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*
	 * 2014-06-26
	 * show toast message
	 */
	private void showMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}

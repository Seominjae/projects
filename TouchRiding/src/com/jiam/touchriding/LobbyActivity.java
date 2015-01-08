package com.jiam.touchriding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LobbyActivity extends Activity{
	
	public static final int REQUEST_CODE_REGISTER = 1004;
	
	EditText text_id, text_pw;
	User user;
	TextView idText;
	TextView pwText;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lobby);
		
		text_id = (EditText) findViewById(R.id.text_id);
		text_pw = (EditText) findViewById(R.id.text_pw);		

		initWidgets();			
	}	
	
	/*
	 * 2014-06-06
	 * start MainActivity with user information
	 */
	private void toMainActivity(User user) {
		
		//temporary user
		this.user =user; 
		
		Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
		mIntent.putExtra("user_info", user);
		startActivity(mIntent);
	}
	
	/*
	 * 2014-06-06
	 * bind Buttons (login, register) and set OnClickListener
	 */
	private void initWidgets() {
		Button loginBtn = (Button)findViewById(R.id.btn_login);
		loginBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				String id = text_id.getText().toString();
				String pw = text_pw.getText().toString();
				
				boolean bln = Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", id);
				if(!bln)
					showMessage("Please match email form");
				else
					new loginTask().execute();				
			}
		});		

		Button registerBtn = (Button)findViewById(R.id.btn_register);
		registerBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivityForResult(myIntent, REQUEST_CODE_REGISTER);
			}
		});
		
		Button autoLoginBtn = (Button)findViewById(R.id.btn_auto_login);
		autoLoginBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				text_id.setText("admin@admin.com");
				text_pw.setText("pwadmin");
				
				new loginTask().execute();				
			}
		});
	}
	
	/*
	 * 2014-06-26
	 * AsyncTask for login
	 */
	private class loginTask extends AsyncTask<Void, Void, Character> {
		
		private String id;
		private String password;
		private String nickname;
		private char state;
		private int indexSize;
		private boolean isVisited[] = {false, false, false, false, false, false};
		private Socket socket;
		private BufferedOutputStream outstream;
		private BufferedInputStream instream;
		
		
		public loginTask() {
			this.id = text_id.getText().toString();
			this.password = text_pw.getText().toString();
			this.state = 'n';
		}		
		
		@Override
		protected Character doInBackground(Void... params) {
			
			try {
				socket = new Socket("210.121.154.95",4002);
				System.out.println("Socket OK");
				outstream = new BufferedOutputStream(socket.getOutputStream());
				System.out.println("Outstream OK");
				instream = new BufferedInputStream(socket.getInputStream());
				System.out.println("Instream OK");
				
				//registration: "l" is header, means "check login"
				String data = "l".concat("+").concat(id).concat("+").concat(password);
				byte[] ref = data.getBytes("UTF-8");
				
				System.out.println("Byte alloc OK");
				outstream.write(ref);
				outstream.flush();
				
				byte[] contents = new byte[1024];
				int bytesRead = 0;
				String str = null;
				
				bytesRead = instream.read(contents);
				System.out.println("byte:" + bytesRead);
				if(bytesRead != -1)
					str = new String(contents, 0, bytesRead);
				
				if(str != null) {
					System.out.println("Received data : " + str);
					String[] data2 = str.split(",");
					state = data2[0].charAt(0);
					nickname = data2[1];
					System.out.println("nickname : " + nickname);
					
					System.out.println(data2[2]);
					if(!data2[2].equals("f")) {
						indexSize = Integer.parseInt(data2[2]) + 1;
						System.out.println("index size : " + indexSize);
						System.out.println(data2[3].substring(0,1));
						System.out.println(data2[3].substring(1,2));
						
						for(int i=0; i<indexSize; i++) {
							String temp = data2[3].substring(i, i+1);
							System.out.println(i + " " + temp);
							isVisited[Integer.parseInt(temp)-1] = true;
						}
					}
				}
				
			} catch(Exception e) {
				this.state = 'n';
				e.printStackTrace();
			}			
			
			System.out.println(getStat());			
			return getStat();
		}
		
		@Override
		protected void onPostExecute(Character stat) {
			if(stat == 's'){
				socketClose();
				System.out.println("Success");				
				toMainActivity(new User(getId(), getNickname(), 1, getIsVisited()));
				LobbyActivity.this.finish();
			}
			else if(stat == 'f'){
				System.out.println("Failed : DB");
				showMessage("Please check ID and Password.");
			}
			else if(stat == 'n'){
				System.out.println("Failed : disconnection");
				showMessage("Sever disconnection.");
			}
		}
		
		public String getId() {
			return this.id;
		}
		
		public String getNickname() {
			return this.nickname;
		}
		
		public boolean[] getIsVisited() {
			return this.isVisited;
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
	
	/*
	 * 2014-07-02
	 * This is getting a result from "RegisterActivity"
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==REQUEST_CODE_REGISTER && data!=null) {
			String userId = data.getStringExtra("user_id");
			text_id.setText(userId);
		}
	}
	

}
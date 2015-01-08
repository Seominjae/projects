package com.jiam.touchriding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SpotActivity extends Activity {

	private User user;
	private int spotId;
	int spotCnt;
	int userCnt;
	String mostVisitId;
	String mostVisitCount;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_spot);

		//get user information from intent
		user = new User();		
		Intent intent = getIntent();
		try {
			user = (User)intent.getSerializableExtra("user_info");
			spotId = intent.getIntExtra("spot_number", 0);
		}
		catch(Exception e) {
			e.printStackTrace();
			user = new User("guest@email.com");
		}
		
		System.out.println("String.valueOf(spotId) : " + String.valueOf(spotId));
		
			
		
		new spotServerTask().execute();
	}
	
	/*
	 * 2014-07-02
	 * AsyncTask for send spot data to server
	 */
	private class spotServerTask extends AsyncTask<Void, Void, Character> {
		
		private String id;
		private String key_name;
		private char state;
		private String key_id;
		private String userCnt;
		private String mostVisitor;
		private String mostUserCnt;
		
		private Socket socket;
		private BufferedOutputStream outstream;
		private BufferedInputStream instream;
		
		public spotServerTask() {
			this.id = user.getEmail();
			this.key_name = String.valueOf(spotId);
			this.state = 'n';
		}

		@Override
		protected Character doInBackground(Void... params) {
			try	{
				//connect
				socket = new Socket("210.121.154.95",4002);
				System.out.println("Socket OK");
				outstream = new BufferedOutputStream(socket.getOutputStream());
				System.out.println("Outstream OK");
				instream = new BufferedInputStream(socket.getInputStream());
				System.out.println("Instream OK");				
				System.out.println(key_name);
				
				//registration. "n" is header, means "NFC".
				String data = "p".concat("+").concat(id).concat("+").concat(key_name);
				byte[] ref = data.getBytes("UTF-8");
				
				System.out.println("byte alloc ok");
				outstream.write(ref);
				outstream.flush();
				
				System.out.println("outstream ok");				
				
				byte[] contents = new byte[1024];
				int bytesRead=0;

				String str = null;
				bytesRead = instream.read(contents);
				System.out.println("byte:" + bytesRead);
				if(bytesRead != -1)
					str = new String(contents, 0, bytesRead);				
				
				if(str != null){
					System.out.println(str);
					String [] data2 = str.split(",");
					System.out.println(data2.length);
					System.out.println(data2[0]);
					
					if(data2[0].charAt(0)=='q')
						state = data2[0].charAt(0);
					else{
						state = data2[0].charAt(0);
						key_id = data2[1];
						System.out.println("key_id : " + key_id);
						userCnt = data2[1];
						System.out.println("userCnt : " + userCnt);
						mostVisitor = data2[3];
						System.out.println("mostVisitId : " + mostVisitor);
						mostUserCnt = data2[4];
						System.out.println("mostuserCnt : " + mostUserCnt);
					}					
				}
			}
			catch(Exception e) {
				this.state = 'n';
				e.printStackTrace();
			}
			
			return getStat();
		}
		
		@Override
		protected void onPostExecute(Character stat) {
			System.out.println(stat);
			
			if(stat=='s'){
				socketClose();
				System.out.println("success");
				
				spotCnt = Integer.parseInt(getUserCnt());
				mostVisitId = getmostVisitId();
				mostVisitCount = getmostuserCnt();
				System.out.println("Spot visitors count : "+spotCnt);				
			}
			else if(stat=='f'){
				System.out.println("failed : DB");
				showMessage("Please check.");
			}
			else if(stat=='n'){
				System.out.println("failed : disconnection");
				showMessage("Sever disconnection.");
			}
			else if(stat=='q'){
				System.out.println("no one visited this spot");
				showMessage("No one visited this spot.");
			}
			
			initWidgets();	
		}

		public char getStat() {
			return state;
		}
		public String getmostVisitId(){
			return this.mostVisitor;
		}
		public String getmostuserCnt(){
			return this.mostUserCnt;
		}
		public String getUserCnt(){
			return this.userCnt;
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
	 * 2014-06-23
	 * Initiate widgets and set texts
	 */
	private void initWidgets() {
		TextView spotInfo = (TextView) findViewById(R.id.text_spot_info);
		TextView numOfVisitors = (TextView) findViewById(R.id.text_num_of_visitors);
		TextView numberOneVisitor = (TextView) findViewById(R.id.text_num_one_visitor);
		
		spotInfo.setText("Spot #"+spotId);
		numOfVisitors.setText("Number of Visitors : "+ spotCnt);
		numberOneVisitor.setText("Number One Visitor ID : "+mostVisitId +" count : " + mostVisitCount);
	}
	
	/*
	 * 2014-07-02
	 * show toast message
	 */
	private void showMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}

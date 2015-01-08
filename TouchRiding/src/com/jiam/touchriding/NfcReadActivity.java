package com.jiam.touchriding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.jiam.touchriding.NFC.NdefMessageParser;
import com.jiam.touchriding.NFC.ParsedRecord;
import com.jiam.touchriding.NFC.TextRecord;
import com.jiam.touchriding.NFC.UriRecord;


public class NfcReadActivity extends Activity {
	
	TextView readResult;
	User user;
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	int spotCnt;
	int userCnt;
	String tagSpot;
	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nfc_read);
		
		user = new User();		
		Intent intent = getIntent();
		user = (User)intent.getSerializableExtra("user_info");
		
		readResult = (TextView) findViewById(R.id.readResult);
		
		// NFC create object
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		Intent targetIntent = new Intent(this, NfcReadActivity.class);
		targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mPendingIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}

		mFilters = new IntentFilter[] { ndef, };

		mTechLists = new String[][] { new String[] { NfcF.class.getName() } };

		Intent passedIntent = getIntent();
		if (passedIntent != null) {
			String action = passedIntent.getAction();
			if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
				processTag(passedIntent);
			}
		}		
	}	
	
	// if NFC is Enabled, turn on! 140604 by Sean
	@Override
    public void onStart() {
        super.onStart();

        if (!mAdapter.isEnabled()) {
        	
        	Toast.makeText(getBaseContext(), "Plz turn on NFC", Toast.LENGTH_LONG).show();
        	startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));}
    }
	
	/************************************
	 * about NFC method
	 ************************************/
	public void onResume() {
		super.onResume();

		if (mAdapter != null) {
			mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
					mTechLists);
		}
	}

	public void onPause() {
		super.onPause();

		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
		}
	}

	// NFC tag scan
	public void onNewIntent(Intent passedIntent) {
		// NFC Tag
		Tag tag = passedIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (tag != null) {
			byte[] tagId = tag.getId();
			readResult.append("Tag ID : " + toHexString(tagId) + "\n"); // TextView���쒓렇 ID �㏓텤��
			
			final String s = findNfcId(toHexString(tagId));
			
			java.util.Calendar cal = java.util.Calendar.getInstance();
			
			int year = cal.get ( cal.YEAR );
			int month = cal.get ( cal.MONTH ) + 1 ;
			int date = cal.get ( cal.DATE ) ;
			int hour = cal.get ( cal.HOUR_OF_DAY ) ;
			int min = cal.get ( cal.MINUTE );
			int sec = cal.get ( cal.SECOND );
			
			String nowTime = year+"."+month+"."+date+" "+hour+":"+min+":"+sec;

			new nfcTagServerTask(nowTime, findNfcId(toHexString(tagId))).execute();			
		}

		if (passedIntent != null) {
			processTag(passedIntent); // call processTag method 
		}
	}
	
	/*
	 * 2014-07-02
	 * AsyncTask for send nfc tag data to server
	 */
	private class nfcTagServerTask extends AsyncTask<Void, Void, Character> {
		
		private String id;
		private String key_name;
		private String tagTime;
		private String key_id;
		private String userCount;
		private char state;
		private String spotName;
		
		private Socket socket;
		private BufferedOutputStream outstream;
		private BufferedInputStream instream;
		
		public nfcTagServerTask(String tag_time, String spotName) {
			this.id = user.getEmail();
			this.key_name = String.valueOf(tagSpot);
			this.tagTime = tag_time;
			this.state = 'n';
			this.spotName = spotName;
		}

		@Override
		protected Character doInBackground(Void... params) {
			
			try	{
				//connect
				socket = new Socket("210.121.154.95",4002);
				System.out.println("socket ok");
				outstream = new BufferedOutputStream(socket.getOutputStream());
				System.out.println("outstream ok");
				instream = new BufferedInputStream(socket.getInputStream());
				System.out.println("instream ok");

				//registration. "n" is header, means "nfc".
				String data = "n".concat("+").concat(id).concat("+").concat(key_name).concat("+").concat(tagTime);
				byte[] ref = data.getBytes("UTF-8");
				
				System.out.println("byte alloc ok");
				outstream.write(ref);
				outstream.flush();
				
				System.out.println("outstream ok");
				
				
				byte[] contents = new byte[1024];
				int bytesRead=0;

				String str =null;
				bytesRead = instream.read(contents);
				System.out.println("byte:" + bytesRead);
				if(bytesRead != -1){
					str = new String(contents, 0, bytesRead);
				}
				
				if(str != null){
					System.out.println(str);
					String[] data2 = str.split(",");
					System.out.println(data2.length);
					System.out.println(data2[0]);
					state = data2[0].charAt(0);
					key_id = data2[1];
					System.out.println("key_id : " + key_id);
					userCount = data2[2];
					System.out.println("userCount : " + userCnt);					
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
				
				spotCnt = Integer.parseInt(getKey_id());
				userCnt = Integer.parseInt(getUserCnt());
				System.out.println(spotCnt);
				System.out.println(userCnt);
				
				System.out.println("displayinfo before : "+spotCnt);
				
				displayInfo(spotName, userCnt, spotCnt);
			}
			else if(stat=='f'){
				System.out.println("failed : DB");
				showMessage("please check.");
			}
			else if(stat=='n'){
				System.out.println("failed : disconnection");
				showMessage("sever disconnection.");
			}
		}
		
		public char getStat() {
			return state;
		}
		public String getKey_id(){
			return this.key_id;
		}
		public String getUserCnt(){
			return this.userCount;
		}
		
		public void socketClose() {
			try {
				socket.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	// NFC return tag ID method
	public static final String CHARS = "0123456789ABCDEF";
	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
					CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
	}

	// onNewIntent
	private void processTag(Intent passedIntent) {
		Parcelable[] rawMsgs = passedIntent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		if (rawMsgs == null) {
			return;
		}

		// ps! rawMsgs.length : count scanning tags
		Toast.makeText(getApplicationContext(), "Success", 1000).show();

		NdefMessage[] msgs;
		if (rawMsgs != null) {
			msgs = new NdefMessage[rawMsgs.length];
			for (int i = 0; i < rawMsgs.length; i++) {
				msgs[i] = (NdefMessage) rawMsgs[i];
				showTag(msgs[i]); // call showTag
			}
		}
	}

	// NFC reading tag method
	private int showTag(NdefMessage mMessage) {
		List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
		final int size = records.size();
		for (int i = 0; i < size; i++) {
			ParsedRecord record = records.get(i);

			int recordType = record.getType();
			String recordStr = ""; // NFC reading text from Tag
			if (recordType == ParsedRecord.TYPE_TEXT) {
				recordStr = "TEXT : " + ((TextRecord) record).getText();
			} else if (recordType == ParsedRecord.TYPE_URI) {
				recordStr = "URI : " + ((UriRecord) record).getUri().toString();
			}

			readResult.append(recordStr + "\n"); // append reading text to TextView
		}

		return size;
	}	
	
	/*
	 * 2014-06-08
	 * find matching nfc id 
	 */
	private String findNfcId(String nfcId) {
		DBSpotHelper db = new DBSpotHelper(this);
	
		List<Spot> spots = db.getAllSpot();
		
		for(Spot s : spots) {
			String log = "Id: " + s.getId() + ", Name: " + s.getName() + " ,NFC ID: " + s.getNfcId();
			Log.d("Name: ", log);
			
			if(nfcId.equals(s.getNfcId())) {
				Intent myIntent = new Intent();
				myIntent.putExtra("spot_id", s.getId()-1);
				setResult(-1, myIntent);
				tagSpot=String.valueOf(s.getId());
				return s.getName();
			}				
		}		
		return "not_found";
	}	
	
	/*
	 * 2014-06-08
	 * display spot information 
	 */
	private void displayInfo(String name, int userCnt, int spotCnt) {
		TextView userNameText = (TextView)findViewById(R.id.text_place_name);
		TextView userCountText = (TextView)findViewById(R.id.text_user_count);
		TextView countText = (TextView)findViewById(R.id.text_count);
		
		userNameText.setText(name);
		userCountText.setText(""+userCnt);
		countText.setText(""+spotCnt);
	}
	
	/*
	 * 2014-07-02
	 * show toast message
	 */
	private void showMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}
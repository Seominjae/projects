package kmucs.capstone.furnidiy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudEntity;
import com.google.cloud.backend.core.CloudBackendFragment.OnListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.provider.Settings.Secure;
//import com.google.appengine.api.datastore.Text;

public class WriteActivity extends Activity implements OnListener {

	String kindOfTitle;
	ArrayList<String> titleList;
	boolean idExist;
	Button registerBtn;
	EditText titleText;
	EditText textArea;
	private FragmentManager mFragmentManager;
    private CloudBackendFragment mProcessingFragment;
    private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";
    public static final String GUESTBOOK_SHARED_PREFS = "GUESTBOOK_SHARED_PREFS";
    private List<CloudEntity> mPosts = new LinkedList<CloudEntity>();
    private Button uploadBtn;
	private String filePath="";
	private String binary="";
	private String android_id;
	private boolean uploadBool;
	
	///
	private String[] temp;
	///
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.write_layout);
	    // TODO Auto-generated method stub
	    // ID존재여부

	    
	    
	    ///
	    idExist = true;
	    titleList = new ArrayList<String>();
	    titleList.add("잡담");
	    titleList.add("질문");
	    titleList.add("자랑");
	    uploadBtn = (Button)findViewById(R.id.upload);
	    Spinner titleSpinner =(Spinner)findViewById(R.id.titleSpinner);
	    registerBtn = (Button) findViewById(R.id.registerBtn);
	    textArea = (EditText) findViewById(R.id.textArea);
	    titleText = (EditText) findViewById(R.id.titleEditText);
	    android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
	    
		final ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, titleList);
		adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		titleSpinner.setAdapter(adapterSpinner);
		uploadBtn.setVisibility(View.GONE);
		titleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				kindOfTitle = adapterSpinner.getItem(position);
				if(position == 2) {
					uploadBtn.setVisibility(View.VISIBLE);
				}
				else {
					uploadBtn.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			
			}
		});
		uploadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle extra = new Bundle();
				uploadBool = true;
				extra.putInt("flag",0);
				
				Intent myIntent = new Intent(getApplicationContext(), FileOpenActivity.class);
				myIntent.putExtras(extra);
				startActivityForResult(myIntent, 870515);
			}
		});
		Button btnCancle = (Button)findViewById(R.id.registerCBtn1);
		
		btnCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		
		if(idExist) {
			
			uploadBtn.setVisibility(View.GONE);
		}
		else {
			uploadBtn.setVisibility(View.VISIBLE);
		}
		
		EditText textArea = (EditText)findViewById(R.id.textArea);
		textArea.setHint("내용을 입력하세요");
		registerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSendButtonPressed(v);
				finish();
				
			}
		});
		mFragmentManager = getFragmentManager();

        checkForPreferences();
		
	}
	private void checkForPreferences() {
        SharedPreferences settings =
                getSharedPreferences(GUESTBOOK_SHARED_PREFS, Context.MODE_PRIVATE);
    
        initiateFragments();
    }
	private void initiateFragments() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // Check to see if we have retained the fragment which handles
        // asynchronous backend calls
        mProcessingFragment = (CloudBackendFragment) mFragmentManager.
                findFragmentByTag(PROCESSING_FRAGMENT_TAG);
        // If not retained (or first time running), create a new one
        if (mProcessingFragment == null) {
            mProcessingFragment = new CloudBackendFragment();
            mProcessingFragment.setRetainInstance(true);
            fragmentTransaction.add(mProcessingFragment, PROCESSING_FRAGMENT_TAG);
        }

        // Add the splash screen fragment
            //mSplashFragment = new SplashFragment();
            //fragmentTransaction.add(R.id.activity_main, mSplashFragment, SPLASH_FRAGMENT_TAG);
            fragmentTransaction.commit();
    }
	public void onSendButtonPressed(View view) {

        // create a CloudEntity with the new post
        CloudEntity newPost = new CloudEntity("Guestbook");
        newPost.put("message", titleText.getText().toString());
        newPost.put("content", textArea.getText().toString());
        newPost.put("upload", uploadBool);
        newPost.setCreatedBy(android_id);
        newPost.put("file", temp);
        
//        if(uploadBool){
//        	CloudEntity newPostFile = new CloudEntity("UploadData");
//        	newPost.put("data1", );
//        }
        
        

        // create a response handler that will receive the result or an error
        CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
            @Override
            public void onComplete(final CloudEntity result) {
                mPosts.add(0, result);
                
                titleText.setText("");
                
                textArea.setText("");
                
            }
        };

        // execute the insertion with the handler
        mProcessingFragment.getCloudBackend().insert(newPost, handler);

    }
	@Override
	public void onCreateFinished() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 870515) {
			filePath=Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/_tempData_.txt";
			File path = new File(filePath);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(path);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String strPath;
			if(fis != null) {
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
				StringBuffer buf = new StringBuffer();
				
				try {
					while((strPath = bufferReader.readLine())!=null) {
						buf.append(strPath+"\n");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fis.close();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				binary = buf.toString();
				temp = binary.split("OneEnd");
				
			}
		}
	}
}

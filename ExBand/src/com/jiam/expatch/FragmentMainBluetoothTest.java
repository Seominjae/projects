package com.jiam.expatch;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class FragmentMainBluetoothTest extends Fragment {
	// Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;
    
    private Context mContext = null;
    private View rootView = null;
    
    
    
    private User user;
	private DBPlanHelper db;
	private static final int REQUEST_CODE_REGISTER = 1001;
	private static final int REQUEST_CODE_ADDPLAN = 1002;
	private CountDownTimer timer;
	private int value = 0;
	private int minute=0;
	private boolean timerflag = false;
	private LinearLayout mLinearLayout;
	private LinearLayout firstIconLinearLayout,secondIconLinearLayout,thirdIconLinearLayout;
	private Bitmap bitmap;
	Drawable d;
	BitmapFactory.Options option;
	private ListView planList;
	private PlanListAdapter adapter;
	private List<Plan> plans;
	private int excerOrder=0; 
	TextView countTextView;
	TextView timerTextView;
	static int firstIcon,secondIcon,thirdIcon;
	static int viewId=1;
	ImageView firstIv,secondIv,thirdIv;
	private static Bitmap secondBitmap,thirdBitmap;
	private static boolean planEndFlag=false;
	//private String pitch,roll;
	FragmentMainHomePlan fragmentMainHomePlan = new FragmentMainHomePlan();
	private static String FRAGMENT_SECOND="FRAGMENT_SECOND";
	
	
    //data 
    private int dataLength = 0;     //블루투스에서 받아온 데이터 개수
    private int chartLength = 0;    //차트에 그린 데이터 개수
    private double[] roll = new double[100000];     //roll 값 저장
    private double[] pitch = new double[100000];    //pitch 값 저장
    private double[] roll_v = new double[100000];   //roll 값의 순간속력 저장 (roll_v[x]=roll[x+1]-roll[x])
    private double[] pitch_v = new double[100000];   //pitch 값의 순간속력 저장 (roll_v[x]=roll[x+1]-roll[x])

    //etc
    private int roll_max_index, pitch_max_index, roll_min_index, pitch_min_index = 0;   //roll & pitch 최솟값과 최댓값 index 저장
    private boolean isIncreaseRoll, isIncreasePitch;   //roll이나 pitch가 증가상태인지 체크
    private double refValue = 0.5;  //증가나 감소 상태를 판정하는 기준값(순간속력이 refValue보다 크면 증가상태, -refValue보다 작으면 감소상태)
    
    //bluetooth serial port

    //문자열 분할 구분자
    private static char[] sp = {'p', 'r'};

    private static boolean _continue;  //Thread 진행을 위한 부울
    private Thread readThread;      //bluetooth thread

    private int count1, count2, count3, count4 = 0;     //각 운동별 카운트 수 저장
    private int matchCnt = 0;   //각 운동의 매칭을 확인하기 위한 카운트

    /*
     * ROLL_MAX_UP = roll값의 최댓값의 최댓값
     * ROLL_MAX_DOWN = roll값의 최댓값의 최솟값
     * ROLL_MIN_UP = roll값의 최솟값의 최댓값
     * ROLL_MIN_DOWN = roll값의 최솟값의 최솟값
     * PITCH_MAX_UP = pitch값의 최댓값의 최댓값
     * PITCH_MAX_DOWN = pitch값의 최댓값의 최솟값
     * PITCH_MIN_UP = pitch값의 최솟값의 최댓값
     * PITCH_MIN_DOWN = pitch값의 최솟값의 최솟값
     */
    
    //아령 - 기본
    private static double ONE_ROLL_MAX_UP = 75;
    private static double ONE_ROLL_MAX_DOWN = 55;
    private static double ONE_ROLL_MIN_UP = -50;
    private static double ONE_ROLL_MIN_DOWN = -60;
    private static double ONE_PITCH_MAX_UP = 45;
    //private static double ONE_PITCH_MAX_DOWN = 34.9;
    //private static double ONE_PITCH_MIN_UP = 18.9;
    private static double ONE_PITCH_MIN_DOWN = 5;

    //아령 - 양손위로
    private static double TWO_ROLL_MAX_UP = 80;
    private static double TWO_ROLL_MAX_DOWN = 55;
    private static double TWO_ROLL_MIN_UP = 45;
    private static double TWO_ROLL_MIN_DOWN = 30;
    private static double TWO_PITCH_MAX_UP = 30;
    //private static double TWO_PITCH_MAX_DOWN = 7.2;
    //private static double TWO_PITCH_MIN_UP = -39.9;
    private static double TWO_PITCH_MIN_DOWN = -60;

    //아령 - 한손목뒤로
    private static double THREE_ROLL_MAX_UP = 75;
    private static double THREE_ROLL_MAX_DOWN = 45;
    private static double THREE_ROLL_MIN_UP = 0;
    private static double THREE_ROLL_MIN_DOWN = -50;
    private static double THREE_PITCH_MAX_UP = 0;
    //private static double THREE_PITCH_MAX_DOWN = -20.9;
    //private static double THREE_PITCH_MIN_UP = -83.9;
    private static double THREE_PITCH_MIN_DOWN = -90;

    //아령 - 양손앞으로
    private static double FOUR_ROLL_MAX_UP = 45;
    private static double FOUR_ROLL_MAX_DOWN = 10;
    private static double FOUR_ROLL_MIN_UP = -30.0;
    private static double FOUR_ROLL_MIN_DOWN = -70.0;
    private static double FOUR_PITCH_MAX_UP = 50;
    //private static double FOUR_PITCH_MAX_DOWN = 23.6;
    //private static double FOUR_PITCH_MIN_UP = 19.1;
    private static double FOUR_PITCH_MIN_DOWN = -40;
    
    
    
//  //아령 - 기본
//    private static double ONE_ROLL_MAX_UP = 65;
//    private static double ONE_ROLL_MAX_DOWN = 45;
//    private static double ONE_ROLL_MIN_UP = -60;
//    private static double ONE_ROLL_MIN_DOWN = -70;
//    private static double ONE_PITCH_MAX_UP = 30;
//    //private static double ONE_PITCH_MAX_DOWN = 34.9;
//    //private static double ONE_PITCH_MIN_UP = 18.9;
//    private static double ONE_PITCH_MIN_DOWN = 5;
//
//    //아령 - 양손위로
//    private static double TWO_ROLL_MAX_UP = 75;
//    private static double TWO_ROLL_MAX_DOWN = 65;
//    private static double TWO_ROLL_MIN_UP = 48;
//    private static double TWO_ROLL_MIN_DOWN = 37;
//    private static double TWO_PITCH_MAX_UP = 13;
//    //private static double TWO_PITCH_MAX_DOWN = 7.2;
//    //private static double TWO_PITCH_MIN_UP = -39.9;
//    private static double TWO_PITCH_MIN_DOWN = -50;
//
//    //아령 - 한손목뒤로
//    private static double THREE_ROLL_MAX_UP = 75;
//    private static double THREE_ROLL_MAX_DOWN = 55;
//    private static double THREE_ROLL_MIN_UP = -15;
//    private static double THREE_ROLL_MIN_DOWN = -25;
//    private static double THREE_PITCH_MAX_UP = -15;
//    //private static double THREE_PITCH_MAX_DOWN = -20.9;
//    //private static double THREE_PITCH_MIN_UP = -83.9;
//    private static double THREE_PITCH_MIN_DOWN = -90;
//
//    //아령 - 양손앞으로
//    private static double FOUR_ROLL_MAX_UP = 30;
//    private static double FOUR_ROLL_MAX_DOWN = 20;
//    private static double FOUR_ROLL_MIN_UP = -40.0;
//    private static double FOUR_ROLL_MIN_DOWN = -55.0;
//    private static double FOUR_PITCH_MAX_UP = 40;
//    //private static double FOUR_PITCH_MAX_DOWN = 23.6;
//    //private static double FOUR_PITCH_MIN_UP = 19.1;
//    private static double FOUR_PITCH_MIN_DOWN = -17;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
//        setContentView(R.layout.activity_main);
        rootView = inflater.inflate(R.layout.fragment_bluetooth,container, false);
        mContext = getActivity();
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        setHasOptionsMenu(true);
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            getActivity().finish();
            return null;
        }
        
        Intent intent = getActivity().getIntent();
		user = (User)intent.getSerializableExtra("user_info");
		db = new DBPlanHelper(getActivity());
		plans = db.getUserPlans(user.getName());
		
		initBitmapFactoryOption();
		initSecondLayout(rootView);
		initWidgets(rootView);
        
        
        return rootView;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
        //Log.d("mConversationArrayAdapter", "mConversationArrayAdapter : " + mConversationArrayAdapter.toString());
        //mConversationView = (ListView) rootView.findViewById(R.id.in);
        //mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        //mOutEditText = (EditText) rootView.findViewById(R.id.edit_text_out);
        //Log.d("mOutEditText", "mOutEditText: " + mOutEditText);
        //mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        //mSendButton = (Button) rootView.findViewById(R.id.button_send);
//        mSendButton.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                // Send a message using content of the edit text widget
//                TextView view = (TextView) rootView.findViewById(R.id.edit_text_out);
//                String message = view.getText().toString();
//                sendMessage(message);
//            }
//        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(mContext, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }
    
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		bitmap.recycle();
//		bitmap = null;
//
//		super.onDestroy();
//	}
    @Override
    public void onDestroy() {
        super.onDestroy();
		bitmap.recycle();
		bitmap = null;
		
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(mContext, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //Log.d("mOutStringBuffer", "mOutStringBuffer : " + mOutStringBuffer); 이건 보낼때
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

    private final void setStatus(int resId) {
        //final ActionBar actionBar = getActionBar();
        //actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        //final ActionBar actionBar = getActionBar();
        //actionBar.setSubtitle(subTitle);
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
            	//Log.d("msg.obj", "msg.obj : " + msg.obj);
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                
                Log.d("mChatService.getStr()", "mChatService.getStr() : " + mChatService.getStr());
                String readMessage = new String(readBuf, 0, msg.arg1);
                //Log.d("readMessage",readMessage);
                
                String[] rsrs = readMessage.split("\n");
                
                for(int i=0; i<rsrs.length;i++)
                {
                	String[] array = rsrs[i].split(",");
                	//Log.d("array.length", "array.length : " + array.length);
                	
                	if(array.length==4){
                	String[] array1 = array[1].split(".");
                	String[] array3 = array[3].split(".");
                	
                	if(array1.length==0 && array3.length==0 && array[3].length()>1 && array[1].length()>1 && countComma(array[1]) && countComma(array[3])
                			&& countMinus(array[1]) && countMinus(array[3]))
                	{
//                		Log.d("pitch","pitch : " + Double.valueOf(array[1]));
//						Log.d("roll", "roll : " + Double.valueOf(array[3]));

						roll[dataLength] = Double.valueOf(array[1]);
						pitch[dataLength++] = Double.valueOf(array[3]);
						findMinMax(chartLength++);
                	}              	
                	}
                }
	        	    
	        	    
	        	    //피치롤 찾는곳
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getActivity().getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getActivity().getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, false);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                Toast.makeText(mContext, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }
	private static Boolean countComma(String input)
    {
    	int num=0;
    	char[] a = new char[10];
    	for(int i=0; i<input.length();i++)
    	{
    		a[i]=input.charAt(i);
    		if(a[i]=='.'){
    			num++;
    		}
    	}
    	if(num==1)
    		return true;
    	else
    		return false;
    }
	
	private static Boolean countMinus(String input)
    {
    	int num=0;
    	char[] a = new char[10];
    	for(int i=0; i<input.length();i++)
    	{
    		a[i]=input.charAt(i);
    		if(a[i]=='-'){
    			num++;
    		}
    	}
    	if(num==1 || num ==0)
    		return true;
    	else
    		return false;
    	
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option_menu, menu);
//        return true;
//    }
    

    

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.secure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(getActivity(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            return true;
        case R.id.insecure_connect_scan:
            // Launch the DeviceListActivity to see devices and do scan
            serverIntent = new Intent(getActivity(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.option_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	private void initSecondLayout(View view) {
		// TODO Auto-generated method stub
		mLinearLayout = (LinearLayout) view.findViewById(R.id.FragmentMainTestSecond);
		bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.excer01_explain, option);
		d = new BitmapDrawable(bitmap);
		mLinearLayout.setBackgroundDrawable(d);
	}

	private void initBitmapFactoryOption() {
		// TODO Auto-generated method stub
		option = new BitmapFactory.Options();
		option.inSampleSize = 1;
		option.inPurgeable = true;
		option.inDither = true;
	}

	private void initWidgets(final View view) {
//		if(plans.size()==0)
//		{	//토스트 메시지로 플랜먼저 하라고 하고 프래그먼트 변경
//			FragmentTransaction ft = getFragmentManager().beginTransaction();
//			ft.replace(R.id.FragmentMainHome,fragmentMainHomePlan,FRAGMENT_SECOND);
//			ft.addToBackStack(null);
//			ft.commit();	
//			showMessage("Plan your excercise first");
//			
//		}
//		else
//		{
		countTextView = (TextView)view.findViewById(R.id.count);
		countTextView.setText("plan : " + String.valueOf(plans.size()) + " cr : " + excerOrder);
		
		timerTextView = (TextView)view.findViewById(R.id.timer);
		
		timerSetting();
		
		final TextView startPauseBtn = (TextView)view.findViewById(R.id.start_pauseBtn);
		startPauseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!timerflag) {
					// 타이머 시작
					showMessage("start button clicked");
					timer.start();
					startPauseBtn.setText("Pause");
					timerflag=true;
				} else {
					showMessage("pause button clicked");
					timer.cancel();
					startPauseBtn.setText("Start");
					timerflag=false;
				}
			}
		});
		
		TextView nextBtn = (TextView)view.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!planEndFlag){
				//timerTextView.setText("00:00");
				value=0;
				minute=0;
				excerOrder++;
				int icon1;
				Log.d("excerOrder", " "+excerOrder);
				Log.d("plans.size()"," "+ plans.size());
				if(excerOrder>=plans.size()-1){
					icon1 = R.drawable.empty;
					planEndFlag=true;
				}
				else{
					icon1 = setExcerIcon(plans.get(excerOrder+1).getName());
				}
				int icon2 = setExcerIcon(plans.get(excerOrder).getName());
				int icon3 = setExcerIcon(plans.get(excerOrder-1).getName());
				thirdIv.setImageBitmap(BitmapFactory.decodeResource(getResources(),icon1, option));
				secondIv.setImageBitmap(BitmapFactory.decodeResource(getResources(),icon2, option));
				firstIv.setImageBitmap(BitmapFactory.decodeResource(getResources(),icon3, option));
				
				countTextView.setText("plan : " + String.valueOf(plans.size()) + " cr : " + excerOrder);
				showMessage("nextBtn button clicked");
				}
			}
		});
		
		TextView doneBtn = (TextView)view.findViewById(R.id.done);
		doneBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(getActivity().getApplicationContext(), ResultActivity.class);
				mIntent.putExtra("user_info", user);
				startActivity(mIntent);
				showMessage("doneBtn button clicked");
			}
		});
		
		initFirstLayout(view);
		//}
	}
	


	private void initFirstLayout(View view) {
		// TODO Auto-generated method stub
		boolean startFlag=false;
		boolean endFlag=false;
		
		if(excerOrder!=0){
			if(plans.size()==0)
			{
				
			}
			else
			{
			firstIcon=setExcerIcon(plans.get(excerOrder-1).getName());
			startFlag=false;
			}
		}
		else
			startFlag=true;
		
		if(plans.size()<1)
		{
			
		}
		else{
		secondIcon=setExcerIcon(plans.get(excerOrder).getName());
		}
		if(plans.size()<2)
			{
			
			}
		else{
			thirdIcon=setExcerIcon(plans.get(excerOrder+1).getName());
		}
		
		if(startFlag){
			excerIconSetting(1,firstIconLinearLayout,R.drawable.empty,R.id.firstIconExcer,70,view);
		}
		else{
			excerIconSetting(1,firstIconLinearLayout,firstIcon,R.id.firstIconExcer,70,view);
		}
		
		excerIconSetting(2,secondIconLinearLayout,secondIcon,R.id.secondIconExcer,100,view);
		excerIconSetting(3,thirdIconLinearLayout,thirdIcon,R.id.thirdIconExcer,70,view);
	}

	private int setExcerIcon(String name) {
		// TODO Auto-generated method stub
		if(name.endsWith("A"))
			return R.drawable.excer01_icon;
		else if(name.endsWith("B"))
			return R.drawable.excer02_icon;
		else if(name.endsWith("C"))
			return R.drawable.excer03_icon;
		else if(name.endsWith("D"))
			return R.drawable.excer04_icon;
		else if(name.endsWith("E"))
			return R.drawable.excer05_icon;
		else if(name.endsWith("F"))
			return R.drawable.excer06_icon;
		else
			return R.drawable.excer01_icon;
	}

	private void excerIconSetting(int order,LinearLayout mLinearLayout,int icon, int viewId, int transparent, final View view) {
		// TODO Auto-generated method stub
		ImageView iv = createImageView(order,icon,transparent);
		
		mLinearLayout = (LinearLayout) view.findViewById(viewId);
		mLinearLayout.addView(iv);
	}

	private ImageView createImageView(int order,int icon,int transparent) {
		// TODO Auto-generated method stub
		ImageView iv = new ImageView(getActivity());
		bitmap = BitmapFactory.decodeResource(getResources(),icon, option);
		if(order==2){
			secondBitmap=bitmap;
		}else if(order==3){
			thirdBitmap=bitmap;
		}
		iv.setImageBitmap(bitmap);
		iv.setId(viewId++);
		if(transparent!=100)
			iv.setAlpha(transparent);
		
		iv.setAdjustViewBounds(true);
		iv.setLayoutParams(new LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT
				,android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
		iv.setScaleType(ScaleType.FIT_XY);
		
		if(order==1){
			firstIv=iv;
			return firstIv;
		}else if(order==2){
			secondIv=iv;
			return secondIv;
		}else{
			thirdIv=iv;
			return thirdIv;
		}
		
	}

	private void timerSetting() {
		// TODO Auto-generated method stub
		timer = new CountDownTimer(60000 * 60, 1000) {//1분 * 60 동안, 1초간격으로
			@Override
			public void onTick(long millisUntilFinished) {
				value++;
				//timerTextView.setText("value = " + value); format.format(value)
				if(value>=60){
					minute++;
					value-=60;
				}
				if(minute<10){
					if(value<10)
						timerTextView.setText("0"+minute+":"+"0"+value);
					else
						timerTextView.setText("0"+minute+":"+value);
				}
				else{
					if(value<10)
						timerTextView.setText("0"+minute+":"+"0"+value);
					else
						timerTextView.setText("0"+minute+":"+value);
				}
			}
			@Override
			public void onFinish() {
			}
		};
		
	}

	private void showMessage(String msg) {
		Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	
	
	
	
	
	public void findMinMax(int index)
    {
        if (index > 0)
        {
            roll_v[index - 1] = roll[index] - roll[index - 1];      //roll의 순간속력
            //pitch_v[index - 1] = pitch[index] - pitch[index - 1];   //pitch의 순간속력

            recordMinMax(index);    //현상태의 최솟값과 최댓값 기록

            //roll
            if (roll_v[index - 1] >= refValue)   //증가상태
            {
                //극소점
                if (!isIncreaseRoll)
                {
                    //setMinMaxPoint(roll_min_index, roll[roll_min_index], true);

                    matchCnt += matchingOne(roll[roll_min_index], pitch[roll_min_index], false);    //매칭시 +1
                    matchCnt += matchingTwo(roll[roll_min_index], pitch[roll_min_index], false);    //매칭시 +10
                    matchCnt += matchingThree(roll[roll_min_index], pitch[roll_min_index], false);  //매칭시 +200
                    matchCnt += matchingFour(roll[roll_min_index], pitch[roll_min_index], false);   //매칭시 +1000

                    //setText("matchCnt=" + matchCnt);
                    Log.d("matchCnt=", "matchCnt= : " + matchCnt);
                    if (getChiper(matchCnt, 3) == 3)   //"한손목뒤로"는 극소에서 완료되므로 백의자리가 3이면 완료
                    {
                        //setText("아령(한손목뒤로) Count=" + ++count3);
                    	Log.d("**********OneHandBack", "OneHandBack : " + ++count3);
                        matchCnt = 0;
                    }
                    else if (getChiper(matchCnt, 3) > 1)   //"한손목뒤로"가 극소에서 완료되지 않고 카운트가 된 경우 백의자리 리셋 (ex: 극소두번카운트되서 400인경우)
                    {
                        matchCnt -= getChiper(matchCnt, 3) * 100;
                    }

                    if (getChiper(matchCnt, 4) > 1)   //"양손앞으로"는 극소에서 완료될 수 없으므로 리셋
                    {
                        matchCnt -= getChiper(matchCnt, 4)*1000;
                    }

                    if (getChiper(matchCnt, 2) > 1)   //"양손위로"는 극소에서 완료될 수 없으므로 리셋
                    {
                        matchCnt -= getChiper(matchCnt, 2)*10;
                    }

                    if (getChiper(matchCnt, 1) > 1)   //"기본"는 극소에서 완료될 수 없으므로 리셋
                    {
                        matchCnt -= getChiper(matchCnt, 1);
                    }

                    //초기화
                    roll_max_index = roll_min_index;
                    roll_min_index = 0;
                }
                isIncreaseRoll = true;
            }
            else if (roll_v[index - 1] <= -refValue)  //감소상태
            {
                //극대점
                if (isIncreaseRoll)
                {
                    //setMinMaxPoint(roll_max_index, roll[roll_max_index], true);

                    matchCnt += matchingOne(roll[roll_max_index], pitch[roll_max_index], true);     //매칭시 +2
                    matchCnt += matchingTwo(roll[roll_max_index], pitch[roll_max_index], true);     //매칭시 +20
                    matchCnt += matchingThree(roll[roll_max_index], pitch[roll_max_index], true);   //매칭시 +100
                    matchCnt += matchingFour(roll[roll_max_index], pitch[roll_max_index], true);    //매칭시 +2000

                    //setText("matchCnt=" + matchCnt);
                    Log.d("matchCnt=", "matchCnt= : " + matchCnt);

                    if (getChiper(matchCnt, 4) == 3)   //"양손앞으로"는 극대에서 완료되므로 천의자리가 3이면 완료
                    {
                    	//Log.d("ex, msg)
                        //setText("아령(양손앞으로) Count=" + ++count4);
                    	Log.d("**********bothHandFront", "bothHandFront : " + ++count4);
                        matchCnt = 0;
                    }
                    else if (getChiper(matchCnt, 4) > 1)   //"양손앞으로"가 극대에서 완료되지 않고 카운트가 된 경우 천의자리 리셋 (ex: 극대두번카운트되서 4000인경우)
                    {
                        matchCnt -= getChiper(matchCnt, 4) * 1000;
                    }

                    if (getChiper(matchCnt, 2) == 3) //"양손위로"는 극대에서 완료되므로 십의자리가 3이면 완료
                    {
                        //setText("아령(양손위로) Count=" + ++count2);
                    	Log.d("**********bothHandUp", "bothHandUp : " + ++count2);
                        matchCnt = 0;
                    }
                    else if (getChiper(matchCnt, 2) > 1)   //"양손위로"가 극대에서 완료되지 않고 카운트가 된 경우 십의자리 리셋 (ex: 극대두번카운트되서 40인경우)
                    {
                        matchCnt -= getChiper(matchCnt, 2) * 10;
                    }

                    if (getChiper(matchCnt, 1) == 3) //"기본"은 극대에서 완료되므로 일의자리가 3이면 완료
                    {
                        //setText("아령(기본) Count=" + ++count1);
                    	Log.d("**********excerBase", "excerBase : " + ++count1);
                        matchCnt = 0;
                    }
                    else if (getChiper(matchCnt, 1) > 1)   //"기본"이 극대에서 완료되지 않고 카운트가 된 경우 일의자리 리셋 (ex: 극대두번카운트되서 4인경우)
                    {
                        matchCnt -= getChiper(matchCnt, 1);
                    }

                    if (getChiper(matchCnt, 3) > 1)   //"한손목뒤로"는 극대에서 완료될 수 없으므로 리셋
                    {
                        matchCnt -= getChiper(matchCnt, 3)*100;
                    }

                    //초기화
                    roll_min_index = roll_max_index;
                    roll_max_index = 0;
                }
                isIncreaseRoll = false;
            }
            
            /*
            //pitch
            if (pitch_v[index - 1] >= refValue)   //증가상태
            {
                //극소점
                if (!isIncreasePitch)
                {
                    setMinMaxPoint(pitch_min_index, pitch[pitch_min_index], false);
                    pitch_max_index = pitch_min_index;
                    pitch_min_index = 0; //초기화
                }
                isIncreasePitch = true;
            }
            else if (pitch_v[index - 1] <= -refValue)  //감소상태
            {
                //극대점
                if (isIncreasePitch)
                {
                    setMinMaxPoint(pitch_max_index, pitch[pitch_max_index], false);
                    pitch_min_index = pitch_max_index;
                    pitch_max_index = 0; //초기화
                }
                isIncreasePitch = false;
            }
             */
            
        }
    }

    //자릿수 반환(value=계산할숫자   valNumber=계산할자릿수) (ex: value=1234, valNumber=2 이면 십의자리인 3을 반환)
    private int getChiper(int value, int valNumber)
    {
        int ten = 1;

        for (int i = 0; i < valNumber; i++)
            ten *= 10;

        return (value % ten) / (ten / 10);
    }

    //Roll, Pitch의 최댓값과 최솟값을 기록
    private void recordMinMax(int index)
    {
        if (roll[index] > roll[roll_max_index])  //최댓값의 인덱스 기록
        {
            roll_max_index = index;
            //Log.d("roll[roll_max_index]", "roll[roll_max_index] : " + roll[roll_max_index]);
//            Log.d("roll_max_index", "roll_max_index : " + roll_max_index);
        }
        if (roll[index] < roll[roll_min_index])  //최솟값 인덱스 기록
        {
            roll_min_index = index;
            //Log.d("roll[roll_min_index]", "roll[roll_min_index] : " + roll[roll_min_index]);
//            Log.d("roll_min_index", "roll_min_index : " + roll_min_index);
        }
        /*
        if (pitch[index - 1] > pitch[pitch_max_index])  //최댓값의 인덱스 기록            
            pitch_max_index = index;

        if (pitch[index - 1] < pitch[pitch_min_index])  //최솟값 인덱스 기록            
            pitch_min_index = index;
         */
    }

    //최댓값과 최솟값을 차트에 표시
//    private void setMinMaxPoint(int x_val, double y_val, boolean isRoll)
//    {
//        if (isRoll)
//        {                
//            chart1.Series["max_min_roll"].Points.AddXY((double)x_val, y_val);
//        }
//        else
//            chart1.Series["max_min_pitch"].Points.AddXY((double)x_val, y_val);
//    }

    //센서 데이터 그래프 그리기 (Roll / Pitch)
//    private void setChartValues(int index)
//    {
//        chart1.Series["gyro_Roll"].Points.AddXY((double)index, roll[index]);
//        chart1.Series["gyro_Pitch"].Points.AddXY((double)index, pitch[index]);
//    }

    //데이터 매칭 (아령-기본)
    private int matchingOne(double rollValue, double pitchValue, boolean isMaxValue)
    {
        if (isMaxValue) //최댓값인경우
        {
            if (rollValue >= ONE_ROLL_MAX_DOWN && rollValue <= ONE_ROLL_MAX_UP && pitchValue >= ONE_PITCH_MIN_DOWN && pitchValue <= ONE_PITCH_MAX_UP )
                return 2;
        }
        else  //최솟값인경우
        {
            if (rollValue >= ONE_ROLL_MIN_DOWN && rollValue <= ONE_ROLL_MIN_UP && pitchValue >= ONE_PITCH_MIN_DOWN && pitchValue <= ONE_PITCH_MAX_UP)
                return 1;
        }
        return 0;
    }

    //데이터 매칭 (아령-양손위로)
    private int matchingTwo(double rollValue, double pitchValue, boolean isMaxValue)
    {
        if (isMaxValue) //최댓값인경우
        {
            if (rollValue >= TWO_ROLL_MAX_DOWN && rollValue <= TWO_ROLL_MAX_UP && pitchValue >= TWO_PITCH_MIN_DOWN && pitchValue <= TWO_PITCH_MAX_UP)
                return 20;
        }
        else  //최솟값인경우
        {
            if (rollValue >= TWO_ROLL_MIN_DOWN && rollValue <= TWO_ROLL_MIN_UP && pitchValue >= TWO_PITCH_MIN_DOWN && pitchValue <= TWO_PITCH_MAX_UP)
                return 10;
        }
        return 0;
    }

    //데이터 매칭 (아령-한손목뒤로)
    private int matchingThree(double rollValue, double pitchValue, boolean isMaxValue)
    {
        if (isMaxValue) //최댓값인경우
        {
            if (rollValue >= THREE_ROLL_MAX_DOWN && rollValue <= THREE_ROLL_MAX_UP && pitchValue >= THREE_PITCH_MIN_DOWN && pitchValue <= THREE_PITCH_MAX_UP)
                return 100;
        }
        else  //최솟값인경우
        {
            if (rollValue >= THREE_ROLL_MIN_DOWN && rollValue <= THREE_ROLL_MIN_UP && pitchValue >= THREE_PITCH_MIN_DOWN && pitchValue <= THREE_PITCH_MAX_UP)
                return 200;
        }
        return 0;
    }

    //데이터 매칭 (아령-양손앞으로)
    private int matchingFour(double rollValue, double pitchValue, boolean isMaxValue)
    {
        if (isMaxValue) //최댓값인경우
        {
            if (rollValue >= FOUR_ROLL_MAX_DOWN && rollValue <= FOUR_ROLL_MAX_UP && pitchValue >= FOUR_PITCH_MIN_DOWN && pitchValue <= FOUR_PITCH_MAX_UP)
                return 2000;
        }
        else  //최솟값인경우
        {
            if (rollValue >= FOUR_ROLL_MIN_DOWN && rollValue <= FOUR_ROLL_MIN_UP && pitchValue >= FOUR_PITCH_MIN_DOWN && pitchValue <= FOUR_PITCH_MAX_UP)
                return 1000;
        }
        return 0;
    }

    //form1 create: initiate chart
//    private void Form1_Load(object sender, EventArgs e)
//    {
//        resetChart();
//    }

    //timer for draw chart
//    private void timer1_Tick_1(object sender, EventArgs e)
//    {
//        if (dataLength - 15 > chartLength && _continue) //들어온 데이터의 개수가 차트로 그린 데이터의 개수보다 15개보다 많은 경우
//        {
//            for (int i = 0; i < 15; i++)
//            {
//                setChartValues(chartLength);
//                findMinMax(chartLength++);
//            }
//        }
//
//        if (chartLength >= 1500 && _continue)
//            changeAxisX(chartLength);
//    }

    //X축 이동
//    private void changeAxisX(int length)
//    {
//        chart1.ChartAreas["ChartArea4"].AxisX.Minimum = length - 500;
//        chart1.ChartAreas["ChartArea4"].AxisX.Maximum = length + 500;
//
//        chart1.ChartAreas["ChartArea5"].AxisX.Minimum = length - 500;
//        chart1.ChartAreas["ChartArea5"].AxisX.Maximum = length + 500;
//    }

    //블루투스 연결
//    private void button1_Click(object sender, EventArgs e)
//    {
//        this.button1.Enabled = false;
//        if (BluetoothConnection.IsOpen)
//        {
//            _continue = false;
//
//            BluetoothConnection.Close();
//            this.button1.Text = "Connect";
//            setText("Bluetooth Disonnected");
//        }
//        else
//        {
//            this.button1.Text = "Disconnect";
//            BluetoothConnection.PortName = this.textBox1.Text.Trim();
//            BluetoothConnection.Open();
//            BluetoothConnection.ReadTimeout = 1500;
//
//            byte[] initMessage = { 0x00 };
//            BluetoothConnection.Write(initMessage, 0, initMessage.Length);
//            setText("Bluetooth Connected");
//        }
//        this.button1.Enabled = true;
//    }

    //차트 초기화
//    private void resetChart()
//    {
//        //clear chart
//        chart1.Series["gyro_Roll"].Points.Clear();
//        chart1.Series["gyro_Pitch"].Points.Clear();
//        chart1.Series["max_min_roll"].Points.Clear();
//        chart1.Series["max_min_pitch"].Points.Clear();
//
//        //init point
//        chart1.Series["gyro_Roll"].Points.AddXY(0, 0);
//        chart1.Series["gyro_Pitch"].Points.AddXY(0, 0);
//
//        //set axis X
//        chart1.ChartAreas["ChartArea4"].AxisX.Minimum = 0;
//        chart1.ChartAreas["ChartArea4"].AxisX.Maximum = 2000;
//        chart1.ChartAreas["ChartArea5"].AxisX.Minimum = 0;
//        chart1.ChartAreas["ChartArea5"].AxisX.Maximum = 2000;
//
//        //set axis Y
//        chart1.ChartAreas["ChartArea4"].AxisY.Minimum = -100;
//        chart1.ChartAreas["ChartArea4"].AxisY.Maximum = 100;
//        chart1.ChartAreas["ChartArea5"].AxisY.Minimum = -100;
//        chart1.ChartAreas["ChartArea5"].AxisY.Maximum = 100;
//    }

    //textbox에 출력
//    private void setText(string str)
//    {
//        textBox2.AppendText(str + Environment.NewLine);
//    }

    //"Get Data" button: get data from device
//    private void button2_Click(object sender, EventArgs e)
//    {
//        if (!_continue)
//        {
//            button2.Text = "Done";
//            _continue = true;
//            readThread.Start();
//            setText("Getting Data ...");
//        }
//        else
//        {
//            button2.Enabled = false;
//            _continue = false;
//            setText("Getting Data Completed");
//        }
//    }

    //"Reset" button: reset chart and data (초기화)
//    private void button5_Click(object sender, EventArgs e)
//    {
//        resetChart();
//
//        count1 = 0;
//        count2 = 0;
//        count3 = 0;
//        count4 = 0;
//        matchCnt = 0;
//
//        setText("Reset");
//
//        for (int i = 0; i < dataLength; i++)
//        {
//            roll[i] = 0;
//            pitch[i] = 0;
//            roll_v[i] = 0;
//            pitch_v[i] = 0;
//        }
//        dataLength = 0;
//        chartLength = 0;
//
//        roll_max_index = 0;
//        pitch_max_index = 0;
//        roll_min_index = 0;
//        pitch_min_index = 0;
//
//        readThread = new Thread(Read);
//
//        button2.Enabled = true;
//        button3.Enabled = true;
//        button2.Text = "Get Data";
//    }

    //"Save Log" button: save log text file
//    private void button7_Click(object sender, EventArgs e)
//    {
//        saveFileDialog1.Filter = "text file|*.txt";
//
//        if (saveFileDialog1.ShowDialog() == DialogResult.OK)
//        {
//            StreamWriter writer = new StreamWriter(saveFileDialog1.FileName);
//            
//            writer.Write("roll\tpitch\n");
//            for (int i = 0; i < dataLength; i++)
//            {
//                writer.Write(roll[i] + "\t" + pitch[i] + "\n");
//            }
//            writer.Close();
//        }
//    }

    //"Screen Shot" button: save screenshot as png format
//    private void button6_Click(object sender, EventArgs e)
//    {
//        //get size 
//        Size size = new Size();
//        size.Width = Form1.ActiveForm.Size.Width;
//        size.Height = Form1.ActiveForm.Size.Height;
//
//        Bitmap img = new Bitmap(size.Width, size.Height);
//
//        Graphics g = Graphics.FromImage(img);
//
//        g.CopyFromScreen(Form1.ActiveForm.Bounds.X, Form1.ActiveForm.Bounds.Y, 0, 0, size);
//
//        saveFileDialog1.Filter = "png 이미지|*.png";
//
//        if (saveFileDialog1.ShowDialog() == DialogResult.OK)
//        {
//            img.Save(saveFileDialog1.FileName, ImageFormat.Png);
//        }
//    }

    //"Result" button: display whole graph
//    private void button3_Click(object sender, EventArgs e)
//    {
//        button3.Enabled = false;
//
//        setText("Set Result");
//
//        chart1.ChartAreas["ChartArea4"].AxisX.Minimum = 0;
//        chart1.ChartAreas["ChartArea4"].AxisX.Maximum = dataLength;
//
//        chart1.ChartAreas["ChartArea5"].AxisX.Minimum = 0;
//        chart1.ChartAreas["ChartArea5"].AxisX.Maximum = dataLength;
//    }
    
}

package kmucs.capstone.furnidiy;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudBackendFragment.OnListener;
import com.google.cloud.backend.core.CloudQuery.Order;
import com.google.cloud.backend.core.CloudQuery.Scope;
import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudEntity;
import com.google.cloud.backend.core.Consts;
import com.google.cloud.backend.sample.guestbook.PostAdapter;
import com.google.cloud.backend.sample.guestbook.SplashFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BoardActivity extends Activity implements OnListener {
	
	public static final String GUESTBOOK_SHARED_PREFS = "GUESTBOOK_SHARED_PREFS";
	private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";
	private static final String SPLASH_FRAGMENT_TAG = "SPLASH_FRAGMENT";
	private static final String BROADCAST_PROP_DURATION = "duration";
	private static final String BROADCAST_PROP_MESSAGE = "message";
	
	private ListView mPostsView;
	private Button writeBtn;
	private FragmentManager mFragmentManager;
	private CloudBackendFragment mProcessingFragment;
	private SplashFragment mSplashFragment;
	
	private List<CloudEntity> mPosts = new LinkedList<CloudEntity>();
	private PostAdapter mAdap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.board_layout);
	    writeBtn = (Button)findViewById(R.id.writeBtn);
	    mPostsView = (ListView) findViewById(R.id.resultList);
	    
	    
	    writeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), WriteActivity.class));				
			}
		});
	   
	    mFragmentManager = getFragmentManager();
	    
	    checkForPreferences();
	}
	
	private void checkForPreferences() {
        SharedPreferences settings = getSharedPreferences(GUESTBOOK_SHARED_PREFS, Context.MODE_PRIVATE);
        
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
	
	private void updateGuestbookView() {
        writeBtn.setEnabled(true);
        if (!mPosts.isEmpty()) {
            mPostsView.setVisibility(View.VISIBLE);
            mAdap = new PostAdapter(this, android.R.layout.simple_list_item_1, mPosts);
            mPostsView.setAdapter(mAdap);
            
        } else {
            mPostsView.setVisibility(View.GONE);
        }
	}
	@Override
	public void onCreateFinished() {
		listPosts();		
	}
	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> l) {
		for (CloudEntity e : l) {
            String message = (String) e.get(BROADCAST_PROP_MESSAGE);
            int duration = Integer.parseInt((String) e.get(BROADCAST_PROP_DURATION));
            Toast.makeText(this, message, duration).show();
            Log.i(Consts.TAG, "A message was recieved with content: " + message);
        }
	}
	private void listPosts() {
        // create a response handler that will receive the result or an error
        CloudCallbackHandler<List<CloudEntity>> handler =
                new CloudCallbackHandler<List<CloudEntity>>() {
                    @Override
                    public void onComplete(List<CloudEntity> results) {
                        mPosts = results;
                        animateArrival();
                        updateGuestbookView();
                    }

                    @Override
                    public void onError(IOException exception) {
                        handleEndpointException(exception);
                    }
                };

        // execute the query with the handler
        mProcessingFragment.getCloudBackend().listByKind(
                "Guestbook", CloudEntity.PROP_CREATED_AT, Order.DESC, 50,
                Scope.FUTURE_AND_PAST, handler);
    }
	private boolean firstArrival = true;
    private void animateArrival() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        //mSplashFragment = (SplashFragment) mFragmentManager.findFragmentByTag(
        //        SPLASH_FRAGMENT_TAG);
        //if (mSplashFragment != null) {
        //    fragmentTransaction.remove(mSplashFragment);
        //    fragmentTransaction.commitAllowingStateLoss();
        //}

        if (firstArrival) {
            
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_progress);
            anim.setAnimationListener(new Animation.AnimationListener() {
    
                @Override
                public void onAnimationStart(Animation animation) {
                }
    
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
    
                @Override
                public void onAnimationEnd(Animation animation) {
                    
                }
            });      
            firstArrival = false;
        }
    }
	private void handleEndpointException(IOException e) {
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        writeBtn.setEnabled(true);
    }

}

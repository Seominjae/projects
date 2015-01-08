package kmucs.capstone.furnidiy;


import com.google.cloud.backend.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabMainActivity extends TabActivity {

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.board_main);
	    
	    TabHost mTabHost = getTabHost();
	    	    
	    Intent one = new Intent(getApplicationContext(), BoardActivity.class);
	    Intent two = new Intent(getApplicationContext(), FAQActivity.class);
	    
	    
	    mTabHost.addTab(mTabHost.newTabSpec("board").setIndicator("게사판").setContent(one).setIndicator("게시판"));
	    mTabHost.addTab(mTabHost.newTabSpec("FAQ").setIndicator("FAQ").setContent(two).setIndicator("FAQ"));
	        
	    
	    mTabHost.setCurrentTab(0);
	  	    
	}
}

package com.jiam.expatch;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ResultActivity extends Activity {
	
	private static final int REQUEST_CODE_ADDPLAN = 1002;
	private ListView planList;
	private ListView planList2;
	private PlanListAdapter adapter;
	private List<Plan> plans;
	private User user;
	private DBPlanHelper db;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);
		
		Intent intent = getIntent();
		user = (User)intent.getSerializableExtra("user_info");
				
		db = new DBPlanHelper(this);
		
		initWidget();
		initList(this);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		planList = (ListView) findViewById(R.id.plan_list);
		planList2 = (ListView) findViewById(R.id.actualExcer_list);
		
	}
	
	private void initList(Context context) {		
		plans = db.getUserPlans(user.getName());
		adapter = new PlanListAdapter(context, plans);
		planList.setAdapter(adapter);
		planList2.setAdapter(adapter);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==REQUEST_CODE_ADDPLAN && data!=null) {
			Plan plan = new Plan();
			plan = (Plan)data.getSerializableExtra("plan_info");
			plan.setUser(user.getName());
			db.addPlan(plan);
			initList(getApplicationContext());
		}
	}
	
}
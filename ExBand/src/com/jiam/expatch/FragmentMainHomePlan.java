package com.jiam.expatch;

import java.util.List;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FragmentMainHomePlan extends Fragment{
	
	private ListView planList;
	private PlanListAdapter adapter;
	private List<Plan> plans;
	private User user;
	private DBPlanHelper db;
	private static final int REQUEST_CODE_ADDPLAN = 1002;
	private Dialog mDialog;
	private Bitmap bitmap;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.fragmentmainplan, container,false);
		Intent intent = getActivity().getIntent();
		user = (User)intent.getSerializableExtra("user_info");
		db = new DBPlanHelper(getActivity());
		
		initWidgets(view);
		initList(getActivity());
		
		return view;
	}

	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==REQUEST_CODE_ADDPLAN && data!=null) {
			Plan plan = new Plan();
			plan = (Plan)data.getSerializableExtra("plan_info");
			plan.setUser(user.getName());
			db.addPlan(plan);
			
			plans = db.getUserPlans(user.getName());
			adapter = new PlanListAdapter(getActivity(), plans);
			planList.setAdapter(adapter);
		}
	}
	
	
	private void initWidgets(final View view) {
		planList = (ListView) view.findViewById(R.id.plan_list);
		
		ImageButton excer01Btn = (ImageButton)view.findViewById(R.id.excer01);
		excer01Btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(0);
			}
		});
		
		ImageButton excer02Btn = (ImageButton)view.findViewById(R.id.excer02);
		excer02Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(2);
			}
		});
		
		Button addBtn = (Button)view.findViewById(R.id.add_btn);
		addBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(view.getContext(), AddplanActivity.class);
				intent.putExtra("list_number", planList.getCount());
				startActivityForResult(intent, REQUEST_CODE_ADDPLAN);
			}
		});
	}
	
	private void initList(Context context) {
		plans = db.getUserPlans(user.getName());
		adapter = new PlanListAdapter(getActivity(), plans);
		planList.setAdapter(adapter);
	}
	
	private void showDialog(int i) {
		// TODO Auto-generated method stub
		
		//ImageView create
		ImageView iv = new ImageView(getActivity());
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.excer01_explain+i);
		iv.setImageBitmap(bitmap);
		iv.setAdjustViewBounds(true);
		iv.setLayoutParams(new LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT
				,android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
		iv.setScaleType(ScaleType.FIT_XY);

		//Dialog configure setting
		mDialog = new Dialog(getActivity());
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar
	    //mDialog.setContentView(innerView);
		mDialog.setContentView(iv);//input view to dialog
	    mDialog.setCancelable(true);
	    mDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				onDestroy();
			}
		});
	    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    
	    // Dialog size adjust
	    LayoutParams params = mDialog.getWindow().getAttributes();
	    params.width = LayoutParams.MATCH_PARENT;
	    params.height = LayoutParams.MATCH_PARENT;
	    mDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	     
	    mDialog.show();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bitmap.recycle();
		bitmap = null;
		System.gc();
		super.onDestroy();
	}
}

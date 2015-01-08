package com.jiam.expatch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/*
 * 2014-07-17
 * list adapter for planlist in PlanningActivity
 */
public class PlanListAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<Plan> exercises;
	
	public PlanListAdapter(Context context, List<Plan> eList) {
		this.exercises = eList;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return this.exercises.size();
	}

	@Override
	public Object getItem(int position) {
		return this.exercises.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null) {
			convertView = inflater.inflate(R.layout.plan_list, parent, false);
		}
		
		TextView planText = (TextView) convertView.findViewById(R.id.text_plan);
		planText.setText(exercises.get(position).getName() + " x " + exercises.get(position).getTimes());
		
		return convertView;
	}
	

}

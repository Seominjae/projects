package com.jiam.expatch;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentMainHomeMypage extends Fragment{
	
	private User user;
	private ImageView userImage;
	private String[] mDate = new String[] {	"8/1", "8/2" , "8/3", "8/4", "8/5", "8/6", "8/7"};
	private Bitmap bitmap;
	private LinearLayout mLinearLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View view = inflater.inflate(R.layout.fragmentmainhomemypage, container, false);
		Intent intent = getActivity().getIntent();
		user = (User)intent.getSerializableExtra("user_info");
		
		displayUserInfo(view);
		
//		//ImageView create
//				ImageView iv = new ImageView(getActivity());
//				bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.excer01_explain);
//				iv.setImageBitmap(bitmap);
//				iv.setAdjustViewBounds(true);
//				iv.setLayoutParams(new LayoutParams(
//						android.widget.LinearLayout.LayoutParams.MATCH_PARENT
//						,android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
//				iv.setScaleType(ScaleType.FIT_XY);
				
		mLinearLayout = (LinearLayout)view.findViewById(R.id.FragmentMainMypageSecond);
		openChart(view);
		return view;
	}
	
	
	
	
	private void displayUserInfo(final View view) {
		
		if(user.getSex().equals("Female"))
		{
			userImage = (ImageView)view.findViewById(R.id.userimage);
			userImage.setBackgroundResource(R.drawable.userimgfemale);
					
		}
		TextView userInfoName = (TextView)view.findViewById(R.id.userinfo_name);
		userInfoName.setText(user.getName());
		
		
		TextView userInfoText = (TextView)view.findViewById(R.id.userinfo_text);
		userInfoText.setText( user.getSex() + "    "
							+ user.getAge() + "    " 
							+ user.getHeight() + "Cm    " 
							+ user.getWeight() + "Kg");
	}
	
	
	
	
	
	private void openChart(View view){
    	int[] x = { 1,2,3,4,5};
    	int[] cal = { 100,60,30,40,60};
    	int[] kgram = {80, 77, 76, 74, 70};
    	
    	// Creating an  XYSeries for Cal
    	XYSeries calSeries = new XYSeries("Cal");
    	// Creating an  XYSeries for Kgram
    	XYSeries kgramSeries = new XYSeries("Kgram");

    	// Adding data to Income and Expense Series
    	for(int i=0;i<x.length;i++){
    		calSeries.add(x[i], cal[i]);
    		kgramSeries.add(x[i],kgram[i]);
    	}
    	
    	// Creating a dataset to hold each series
    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

    	// Adding Cal Series to the dataset
    	dataset.addSeries(calSeries);

    	// Adding Expense Series to dataset
    	dataset.addSeries(kgramSeries);    	
    	
    	
    	// Creating XYSeriesRenderer to customize calSeries
    	XYSeriesRenderer calRenderer = new XYSeriesRenderer();
    	calRenderer.setColor(Color.WHITE);
    	calRenderer.setPointStyle(PointStyle.CIRCLE);
    	calRenderer.setFillPoints(true);
    	calRenderer.setLineWidth(4);
    	calRenderer.setDisplayChartValues(true);
    	
    	// Creating XYSeriesRenderer to customize kgramSeries
    	XYSeriesRenderer kgramRenderer = new XYSeriesRenderer();
    	kgramRenderer.setColor(Color.parseColor("#01A6AB"));
    	kgramRenderer.setPointStyle(PointStyle.CIRCLE);
    	kgramRenderer.setFillPoints(true);
    	kgramRenderer.setLineWidth(4);
    	kgramRenderer.setDisplayChartValues(true);
    	
    	
    	// Creating a XYMultipleSeriesRenderer to customize the whole chart
    	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
    	multiRenderer.setXLabels(0);
    	multiRenderer.setChartTitle("Exband Chart");
    	//multiRenderer.setXTitle("Count");
    	//multiRenderer.setYTitle("Date");
    	//multiRenderer.setZoomButtonsVisible(true);    	    	
    	
    	multiRenderer.setChartTitleTextSize(30);
    	
    	
    	for(int i=0;i<x.length;i++){
    		multiRenderer.addXTextLabel(i+1, mDate[i]);    		
    	}    	
    	
    	
    	multiRenderer.setShowGrid(true);
    	multiRenderer.setLabelsTextSize(20);
    	
    	/**
    	 * legend 범례
    	 * Axis 축에 붙는 타이틀
    	 * labels 레이블
    	 * 
    	 * 
    	 * background color
    	 */
    	
    	
    	multiRenderer.setAxesColor(Color.WHITE);//축 컬러
    	multiRenderer.setApplyBackgroundColor(true);
    	multiRenderer.setBackgroundColor(Color.TRANSPARENT);
    	//multiRenderer.setMarginsColor(Color.TRANSPARENT);//투명시 거검정색이 나옴
    	multiRenderer.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
    	//multiRenderer.setGridColor(color.darker_gray);//모르겠음
    	multiRenderer.setLegendTextSize(20);
    	
    	
    	
    	
    	// Adding CalRenderer and kgramRenderer to multipleRenderer
    	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
    	// should be same
    	multiRenderer.addSeriesRenderer(calRenderer);
    	multiRenderer.addSeriesRenderer(kgramRenderer);
    	
    	// Creating an intent to plot line chart using dataset and multipleRenderer
    	//Intent intent = ChartFactory.getLineChartIntent(getActivity(), dataset, multiRenderer);
    	
		// ImageView create
		View iv = new View(getActivity());
		iv.setLayoutParams(new LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
    			
		iv = ChartFactory.getLineChartView(getActivity(), dataset, multiRenderer);
    	mLinearLayout.addView(iv);

//    	// Start Activity
    	//startActivity(intent);
    	
    }
	
	
	
	
}

package kmucs.capstone.furnidiy;

import java.util.ArrayList;

import com.google.cloud.backend.R;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TextureActivity extends Activity {
	
	private LayoutInflater mInflater;
	private ViewGroup mTrack;
	private List<ActionItem> actionItems = new ArrayList<ActionItem>();
	private List<View> containers = new ArrayList<View>();
	private int mInsertPos;
	private int selectIndex = -1;
	private int beforeIndex = -1;
	View container;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_texture);
		
		mTrack = (ViewGroup) findViewById(R.id.tex_tracks);
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		mInsertPos = 0;
		
		Button okBtn = (Button) findViewById(R.id.tex_okBtn);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent();
				myIntent.putExtra("tex_index", selectIndex);
				setResult(1, myIntent);
				finish();				
			}
		});
		Button cancleBtn = (Button) findViewById(R.id.tex_cancleBtn);
		cancleBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		ActionItem tex01 = new ActionItem(0, "Texture 1", getResources().getDrawable(R.drawable.tex01));
		ActionItem tex02 = new ActionItem(1, "Texture 2", getResources().getDrawable(R.drawable.tex02));
		ActionItem tex03 = new ActionItem(2, "Texture 3", getResources().getDrawable(R.drawable.tex03));
		ActionItem tex04 = new ActionItem(3, "Texture 4", getResources().getDrawable(R.drawable.tex04));
		ActionItem tex05 = new ActionItem(4, "Texture 5", getResources().getDrawable(R.drawable.tex05));
		ActionItem tex06 = new ActionItem(5, "Texture 6", getResources().getDrawable(R.drawable.tex06));
		ActionItem tex07 = new ActionItem(6, "Texture 7", getResources().getDrawable(R.drawable.tex07));
		ActionItem tex08 = new ActionItem(7, "Texture 8", getResources().getDrawable(R.drawable.tex08));
		ActionItem tex09 = new ActionItem(8, "Texture 9", getResources().getDrawable(R.drawable.tex09));
		
		addActionItem(tex01);
		addActionItem(tex02);
		addActionItem(tex03);
		addActionItem(tex04);
		addActionItem(tex05);
		addActionItem(tex06);
		addActionItem(tex07);
		addActionItem(tex08);
		addActionItem(tex09);
	}
	
	public void addActionItem(ActionItem action) {
		final int index = action.getActionId();
		
		actionItems.add(action);
		
		String title 	= action.getTitle();
		Drawable icon 	= action.getIcon();		
				
		container = mInflater.inflate(R.layout.item_texture, null);
		
		ImageView img = (ImageView) container.findViewById(R.id.tex_icon);
		TextView text = (TextView) container.findViewById(R.id.tex_title);
		
		if (icon != null) {
			img.setImageDrawable(icon);
		} else {
			img.setVisibility(View.GONE);
		}
		
		if (title != null) {
			text.setText(title);
		} else {
			text.setVisibility(View.GONE);
		}
		
		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectIndex = index;
				if(beforeIndex != selectIndex) {
					v.setBackgroundResource(R.drawable.action_item_selected);
					if(beforeIndex != -1)
						containers.get(beforeIndex).setBackgroundColor(Color.TRANSPARENT);
				}
				beforeIndex = index;
			}
		});
		
		container.setFocusable(true);
		container.setClickable(true);
		
		containers.add(container);
		
		mTrack.addView(container, mInsertPos++);		
	}
	
	public void changeBackground() {
		
	}

}

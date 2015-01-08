package com.google.cloud.backend.sample.guestbook;

import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudEntity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kmucs.capstone.furnidiy.ContentsActivity;
import android.view.View.OnClickListener;

/**
 * This ArrayAdapter uses CloudEntities as items and displays them as a post in
 * the guestbook. Layout uses row.xml.
 *
 */
public class PostAdapter extends ArrayAdapter<CloudEntity> implements OnClickListener {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ", Locale.US);
    private String downFile="";

    private LayoutInflater mInflater;
    TextView message;
    TextView signature;
    CloudEntity ce;
    /**
     * Creates a new instance of this adapter.
     *
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public PostAdapter(Context context, int textViewResourceId, List<CloudEntity> objects) {
        super(context, textViewResourceId, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : mInflater.inflate(R.layout.row_post, parent, false);
        ce = getItem(position);
        if (ce != null) {
            message = (TextView) view.findViewById(R.id.messageContent);
            signature = (TextView) view.findViewById(R.id.signature);
            if (message != null) {
                message.setText(ce.get("message").toString());
            }
            if (signature != null) {
                signature.setText(SDF.format(ce.getCreatedAt()));
            }

        }
        view.setTag(position);
        view.setOnClickListener(this);

        return view;
    }

    
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return this.getView(position, convertView, parent);
    }

    /**
     * Gets the author field of the CloudEntity.
     *
     * @param post the CloudEntity
     * @return author string
     */
    private String getAuthor(CloudEntity post) {
        if (post.getCreatedBy() != null) {
            return " " + post.getCreatedBy().replaceFirst("@.*", "");
        } else {
            return "<anonymous>";
        }
    }

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		ce = getItem(position);
		
		Bundle extra = new Bundle();
		extra.putString("title", ce.get("message").toString());
		extra.putString("textArea", ce.get("content").toString());
		extra.putString("bool", ce.get("upload").toString());
		extra.putString("data", ce.get("file").toString());
		Intent intent = new Intent(mInflater.getContext(), ContentsActivity.class);
		intent.putExtras(extra);
//		downFile = ce.get("file").toString();
//		saveText();
		mInflater.getContext().startActivity(intent);
		
	}
	private void saveText() {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/_tempData_.txt";
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(downFile.substring(1,(downFile.length()-1)));
			bw.close();
		} catch(Exception e) {
			System.out.println (e.toString());
		}
		
	}
}

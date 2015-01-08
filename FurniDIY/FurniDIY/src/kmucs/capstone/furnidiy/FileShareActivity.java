package kmucs.capstone.furnidiy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.google.cloud.backend.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.content.Intent;

// yhchung
// share

public class FileShareActivity extends ListActivity {
	
	private List<String> item = null;
	private List<String> path = null;
	private String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/"; 
	private TextView myPath;
	private List<String> index = null;
	public final static int REQUEST_FILE_SHARE = 1005;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.filemain);
	    myPath = (TextView)findViewById(R.id.path);
	    getDir(root);
	}
	    
	private void getDir(String dirPath)
	{
		myPath.setText("Location: " + dirPath);
	   
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		index = new ArrayList<String>();
	  
		File f = new File(dirPath);
		if(!f.isDirectory()) 
			f.mkdirs();
		
		File[] files = f.listFiles();
	  
		if(!dirPath.equals(root)) {
			item.add(root);
			path.add(root);  
			item.add("../");
			path.add(f.getParent());
		}
	     
	    for(int i=0; i < files.length; i++) {
	    	File file = files[i];
	    	path.add(file.getPath());
	    	
	    	if(file.isDirectory())
	    		item.add(file.getName() + "/");
	    	else
	    	{
	    		if(file.getName().matches(".*.3ds.*")) {
	    			index.add(Integer.toString(i));
	    			item.add(file.getName());
	    		}
	    	}
	    }

	    ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
	    setListAdapter(fileList); 
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String str = index.get(position);
		int nValue = Integer.parseInt(str);
		File file = new File(path.get(nValue));	
		final String filePath = path.get(nValue);
	  
		new AlertDialog.Builder(this)
		.setIcon(R.drawable.button)
		.setTitle("[" + file.getName() + "]")
		.setPositiveButton("Send", 
				new DialogInterface.OnClickListener() {
       
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					Intent sendIntent = new Intent(Intent.ACTION_SEND);  
					sendIntent.putExtra(Intent.EXTRA_SUBJECT, "from FurniDIY");  
					sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ filePath));
					sendIntent.setType("file/*");  
					startActivityForResult(Intent.createChooser(sendIntent, "Choose Send Client"), REQUEST_FILE_SHARE);					
				} 
				catch(Exception e) {
					System.out.println (e.toString());
				}
			}
			
		}).show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
		super.onActivityResult(requestCode, resultCode, Data);

		if(requestCode == REQUEST_FILE_SHARE) {
			finish();
		}
	}
}

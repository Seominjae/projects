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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;



import android.content.Intent;



// yhchung
// save

public class FileSaveActivity extends ListActivity {
	private List<String> item = null;
	private List<String> path = null;
	private String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/"; 
	private TextView myPath;
	private List<String> index = null;	
	
	public static final int TYPE_NONE = 0;
	public static final int TYPE_3DS = 1;
	public static final int TYPE_IMAGE = 2;
	private int saveType = TYPE_NONE;
	
	private boolean fileShare = false;
	
	public final static int REQUEST_FILE_SHARE = 1005;
	
	private boolean confirmCapture = false;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.savemain);
	    myPath = (TextView)findViewById(R.id.path);
	    
	    Intent intent = getIntent();
		this.saveType = intent.getIntExtra("SaveType", TYPE_NONE);
	    
	    getDir(root);
	    
	    CheckBox chkBox = (CheckBox) findViewById(R.id.checkBox1);

	    //chkBox.setVisibility(View.INVISIBLE);

	    chkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {              
	    	@Override            
	    	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    		if (buttonView.getId() == R.id.checkBox1) {
	    			fileShare = isChecked;
	    		}
	    	}
	    });
	    		
		Button saveBtn = (Button) this.findViewById(R.id.button1);
		saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				EditText inputText = (EditText)findViewById(R.id.editText1); 
				String strFileName = inputText.getText().toString();
				inputText.setText("");                 

				if(saveType == TYPE_3DS) {
					createFile(root + strFileName + ".3ds");
				}
				else {
					confirmCapture = true;
				}
				
				if(fileShare) {
//					Intent myIntent = new Intent(getApplicationContext(), FileShareActivity.class);
//					startActivityForResult(myIntent, REQUEST_FILE_SHARE);


					Intent sendIntent = new Intent(Intent.ACTION_SEND);  
					sendIntent.putExtra(Intent.EXTRA_SUBJECT, "from FurniDIY");  
					sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ root + strFileName + ".3ds"));
					sendIntent.setType("file/*");  
					startActivityForResult(Intent.createChooser(sendIntent, "Choose Send Client"), REQUEST_FILE_SHARE);
					
				}
				else {
					if(saveType == TYPE_IMAGE) {
						Intent myIntent = new Intent();
						myIntent.putExtra("confirmCapture", confirmCapture);
						myIntent.putExtra("ImageFilePath", root+strFileName+".png");
						setResult(RESULT_OK, myIntent);
					}
					finish();
				}
				
			}
		}); 
	}
	
	private void getDir(String dirPath)
	{
		myPath.setText("Location: " + dirPath);
	   
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		index = new ArrayList<String>();
	  
		File f = new File(dirPath);
		if(!f.isDirectory()) {
			f.mkdirs();
		}
		
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
	    	
	    	if(file.isDirectory()) {
	    		item.add(file.getName() + "/");
	    	}
	    	else
	    	{
	    		if( ((saveType == TYPE_3DS) && file.getName().matches(".*.3ds.*")) ||
	    				((saveType == TYPE_IMAGE) && file.getName().matches(".*.png.*")) ) {
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
		.setPositiveButton("Overwrite", 
				new DialogInterface.OnClickListener() {
       
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				try {
					if(saveType == TYPE_3DS) {
						createFile(filePath);
					}
					else {
						confirmCapture = true;
					}
					
					if(fileShare) {
//						Intent myIntent = new Intent(getApplicationContext(), FileShareActivity.class);
//						startActivityForResult(myIntent, REQUEST_FILE_SHARE);

						// yhchung 140527
						Intent sendIntent = new Intent(Intent.ACTION_SEND);  
						sendIntent.putExtra(Intent.EXTRA_SUBJECT, "from FurniDIY");  
						sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+ filePath));  
						sendIntent.setType("file/*");  
						startActivityForResult(Intent.createChooser(sendIntent, "Choose Send Client"), REQUEST_FILE_SHARE);
					}
					else {
						if(saveType == TYPE_IMAGE) {
							Intent myIntent = new Intent();
							myIntent.putExtra("confirmCapture", confirmCapture);
							myIntent.putExtra("ImageFilePath", filePath);
							setResult(RESULT_OK, myIntent);
						}
						finish();
					}
				} 
				catch(Exception e) {
					System.out.println (e.toString());
				}
			}
			
		}).show();
	}
	
	public void createFile(String filePath) {
		try {
			WriteFile wf = new WriteFile();
			wf.createFile(filePath);
		} 
		catch(Exception e) {
			System.out.println (e.toString());
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
		super.onActivityResult(requestCode, resultCode, Data);

		if(requestCode == REQUEST_FILE_SHARE) {
			finish();
		}
	}
}

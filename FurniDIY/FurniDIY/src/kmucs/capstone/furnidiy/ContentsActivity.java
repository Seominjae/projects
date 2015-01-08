package kmucs.capstone.furnidiy;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.google.android.gms.internal.dw;
import com.google.cloud.backend.R;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ContentsActivity extends ActivityGroup {

   private EditText textArea;
   private EditText title;
   private Button canBtn;
   private String loadFileString;
   private boolean loadFile=false;
   private String downFile="";
   private String temp="";

   
   /** Called when the activity is first created. */
   @SuppressWarnings("deprecation")
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.contentlayout);
      
      
       
       title = (EditText) findViewById(R.id.titleEditText1);
       textArea = (EditText) findViewById(R.id.textArea1);
       canBtn = (Button) findViewById(R.id.registerCBtn1);
       title.setFocusable(false);
       title.setClickable(false);
       textArea.setFocusable(false);
       textArea.setClickable(false);
       title.setText(getIntent().getStringExtra("title"));
       textArea.setText(getIntent().getStringExtra("textArea"));
//       textArea.setText(getIntent().getStringExtra("binary"));
       loadFileString = getIntent().getStringExtra("bool");
       if(loadFileString.equals("true")){
          loadFile = true;
       }
       if(loadFile)
       {
          downFile =getIntent().getStringExtra("data"); 
          //Log.i("downFile", downFile.substring(1));
       }

       saveText();
       
       LinearLayout inner = (LinearLayout) findViewById(R.id.innerActivity);
       LocalActivityManager m = getLocalActivityManager();
       Intent innerInt = new Intent();
       innerInt.putExtra("FileLoad", true);
       innerInt.setClass(this, EditActivity.class);
//       Intent innerInt = new Intent(getApplicationContext(), EditActivity.class);
//       innerInt.putExtra("aa", true);
       Window w = m.startActivity("top", innerInt);
       View v = w.getDecorView();
       inner.addView(v);
       
       //textArea.setText(downFile);
       canBtn.setOnClickListener(new OnClickListener() {
         
         @Override
         public void onClick(View v) {
            finish();
            
         }
      });
      
   }

   private void saveText() {
      String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/_tempData_.txt";
      temp = downFile.substring(1,(downFile.length()-1));
      temp.replaceAll(",", "OneEnd");
      try {
         FileWriter fw = new FileWriter(path);
         BufferedWriter bw = new BufferedWriter(fw);
         bw.write(temp);
         bw.close();
      } catch(Exception e) {
         System.out.println (e.toString());
      }
      
   }
   
}
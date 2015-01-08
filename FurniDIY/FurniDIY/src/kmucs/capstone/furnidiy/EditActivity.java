package kmucs.capstone.furnidiy;


import java.util.ArrayList;
import java.util.HashSet;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.cloud.backend.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class EditActivity extends Activity {
	
	private boolean existData = false;
	private String imageFilePath = "";
	
	private GLSurfaceView glSurface;
	private EditRenderer glRenderer;
	
	private static final int ID_ONE   = 1;
	private static final int ID_TWO   = 2;
	private static final int ID_THREE = 3;
	private static final int ID_FOUR  = 4;
	private static final int ID_FIVE  = 5;
	
	public static final int REQUEST_CODE_TEXTURE = 1001;
	public static final int REQUEST_CODE_CAMERA = 1002;
	public static final int REQUEST_CODE_GALLERY = 1003;
	public static final int REQUEST_CODE_SAVE_IMAGE = 1004;
	
	public boolean isExplorerMode = true;
	public boolean isTransformMode = false;
	public boolean isGroupMode = false;
	public boolean isPtSelectMode = false;
	public boolean isBackgroundMode = false;
	
	private TouchHandler tHandler;
	private DisplayMetrics metrics;
	private int screenWidth, screenHeight;	
	
	private static final String TAG = "EDIT_ACTIVITY";
	
	private ImageButton button;
	private Button xBtn, yBtn, zBtn;
	private SeekBar seekbar;	
	private int selectAxis = 0;	//x=1, y=2, z=3
	public int mode = 0; //move = 1, rotate = 2
	private int tmpProgress;
	private int tmpMode = 0;
		
	private QuickAction quickAction, quickAction101, quickAction102, quickAction103,
						quickAction2, quickAction201, quickAction202;
	
	private HashSet<Integer> selectIndex = new HashSet<Integer>();
	public int selectedPrism = 0;
	public int selectedPtPrism = 2;
	
	static TextView txtview;
	
	SquarePrism c0 ;
	SquarePrism c1 ;
	SquarePrism c2 ;
	SquarePrism c3 ;
	
	SquarePrism c4 ;
	SquarePrism c5 ;
	SquarePrism c6 ;
	SquarePrism c7 ;
		
	
	public ArrayList<Bitmap> bmp = new ArrayList<Bitmap>();
	private final int MAX_TEXTURE = 9;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
				
		glSurface = (GLSurfaceView) findViewById(R.id.surface_view);		
		glRenderer = new EditRenderer(this);
		glSurface.setRenderer(glRenderer);
		tHandler = new TouchHandler(glRenderer, screenHeight, screenWidth,this);
		glSurface.setOnTouchListener(tHandler);
		
		BitmapFactory.Options o=new BitmapFactory.Options();
		o.inSampleSize = 4;
		o.inDither=false;                     //Disable Dithering mode
		o.inPurgeable=true;					//Tell to gc that whether it needs free 
				
		for(int i=0; i<MAX_TEXTURE; i++){
			Bitmap tb = BitmapFactory.decodeResource(getResources(),R.drawable.tex01+i, o);
			bmp.add(tb);
		}
		glRenderer.setTextureImage(bmp);
		
		glRenderer.moveScreen(-135, 90);		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;		

		//팝업 메뉴 생성
        quickAction = new QuickAction(this, QuickAction.HORIZONTAL);
		quickAction101 = new QuickAction(this, QuickAction.VERTICAL);	
		quickAction102 = new QuickAction(this, QuickAction.VERTICAL);	
		quickAction103 = new QuickAction(this, QuickAction.VERTICAL);	
		quickAction2 = new QuickAction(this, QuickAction.HORIZONTAL);
		quickAction201 = new QuickAction(this, QuickAction.VERTICAL);
		quickAction202 = new QuickAction(this, QuickAction.VERTICAL);
		
		quickAction.setScrSize(screenWidth, screenHeight);
		quickAction101.setScrSize(screenWidth, screenHeight);
		quickAction102.setScrSize(screenWidth, screenHeight);
		quickAction103.setScrSize(screenWidth, screenHeight);
		quickAction2.setScrSize(screenWidth, screenHeight);
		quickAction201.setScrSize(screenWidth, screenHeight);
		quickAction202.setScrSize(screenWidth, screenHeight);					
		
		txtview = (TextView) this.findViewById(R.id.text);		
		
		xBtn = (Button) this.findViewById(R.id.axisXBtn); 
		yBtn = (Button) this.findViewById(R.id.axisYBtn); 
		zBtn = (Button) this.findViewById(R.id.axisZBtn); 
		seekbar = (SeekBar) this.findViewById(R.id.seekbar);		
		
		xBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				print("X축이 선택되었습니다.");
				selectAxis = 1;
				mode = 0;
				initSeekBar();
			}
		}); 
		
		yBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				print("Y축이 선택되었습니다.");
				selectAxis = 2;
				mode = 0;
				initSeekBar();
			}
		});
		
		zBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				print("Z축이 선택되었습니다.");
				selectAxis = 3;
				mode = 0;
				initSeekBar();
			}
		});
		
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(mode == 1) {
					if(tHandler.alreadyGrouped==true)
						glRenderer.setMovePoint(selectIndex, selectAxis, -1.0f*(tmpProgress-progress));
					else
						glRenderer.setMovePoint(selectedPrism, selectAxis, -1.0f*(tmpProgress-progress));
					
					if(selectAxis==1)
						print("X축으로 " + (progress-30) +" 만큼 이동합니다.");
					else if(selectAxis==2)
						print("Y축으로 " + (progress-30) +" 만큼 이동합니다.");
					else
						print("Z축으로 " + (progress-30) +" 만큼 이동합니다.");
				}
				else if(mode == 2) {
					if(tHandler.alreadyGrouped==true)
						glRenderer.setRotationAngle(selectIndex, selectAxis, -1.0f*(tmpProgress-progress)*5);
					else
						glRenderer.setRotationAngle(selectedPrism, selectAxis, -1.0f*(tmpProgress-progress)*5);
					
					if(selectAxis==1)
						print("X축으로 " + ((progress-36)*5) +" 만큼 회전합니다.");
					else if(selectAxis==2)
						print("Y축으로 " + ((progress-36)*5) +" 만큼 회전합니다.");
					else
						print("Z축으로 " + ((progress-36)*5) +" 만큼 회전합니다.");
				}
				else if(mode == 3) {
					glRenderer.rotateObject(-1.0f*(tmpProgress-progress)*5);
				}
				else if(mode ==4)
				{
					Log.d("selectAxis", "selectAxis : " + selectAxis);
					if(selectAxis==1){
						Log.d("selectedPrism", "selectedPrism : " +selectedPrism);
						//Log.d("touchModeHanlder.selectedPtPrism", "touchModeHanlder.selectedPtPrism : " + touchModeHanlder.selectedPtPrism);
						Log.d("1.0f*(tmpProgress-progress)", "1.0f*(tmpProgress-progress) : " + 1.0f*(tmpProgress-progress));
						glRenderer.prismList.get(selectedPrism).setCalculatedVertexXaxis(selectedPtPrism,1.0f*(tmpProgress-progress));
					}
					else if(selectAxis==2)
						glRenderer.prismList.get(selectedPrism).setCalculatedVertexYaxis(selectedPtPrism,1.0f*(tmpProgress-progress));
					else if(selectAxis==3)
						glRenderer.prismList.get(selectedPrism).setCalculatedVertexZaxis(selectedPtPrism,1.0f*(tmpProgress-progress));
					
					if(selectAxis==1)
						print("X축으로 " + (progress-30) +" 만큼 이동합니다.");
					else if(selectAxis==2)
						print("Y축으로 " + (progress-30) +" 만큼 이동합니다.");
					else
						print("Z축으로 " + (progress-30) +" 만큼 이동합니다.");
					
				}
				tmpProgress = progress;
				
			}
		});
		
		setMode();
		print("Welcome to Furni DIY");
		
		/*
		 * 탐색 메뉴
		 * 추가 - 3각, 4각, 5각, 6각, 원 기둥 생성
		 * 저장 - skp, pdf 파일 출력
		 * 배경 - 카메라, 갤러리 이미지 선택후 배경 설정
		 * 선택 - 도형 다중 선택
		 */
		ActionItem addItem 	= new ActionItem(ID_ONE, "추가", getResources().getDrawable(R.drawable.add_btn));
		ActionItem saveItem 	= new ActionItem(ID_TWO, "파일", getResources().getDrawable(R.drawable.file_btn));
        ActionItem backgroundItem 	= new ActionItem(ID_THREE, "배경", getResources().getDrawable(R.drawable.bg_btn));
        ActionItem selectItem 	= new ActionItem(ID_FOUR, "그룹", getResources().getDrawable(R.drawable.group_btn));
        
        addItem.setSticky(true);
        saveItem.setSticky(true);
        backgroundItem.setSticky(true); 
       
        quickAction.addActionItem(addItem);
		quickAction.addActionItem(saveItem);
        quickAction.addActionItem(backgroundItem);
        quickAction.addActionItem(selectItem);
        
        quickAction.setPopupView((View)button, 1,1);    
        quickAction.setScrSize(screenWidth, screenHeight);
        
        
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
        	@Override
        	public void onItemClick(QuickAction source, int pos, int actionId) {	
        		switch(actionId) {
        		case ID_ONE:
        			print("새로운 도형을 추가할 수 있습니다.");
        			quickAction101.showAddPopupWindow();        			
        			break;
        		case ID_TWO:
        			print("현재의 디자인을 도면용 이미지나 PC용 파일로 저장할 수 있습니다.");
        			quickAction102.showAddPopupWindow();        			
        			break;
        		case ID_THREE:
        			print("현재의 디자인을 불러온 이미지에 가상으로 배치할 수 있습니다.");
        			quickAction103.showAddPopupWindow();        			
        			break;
        		case ID_FOUR:
        			print("선택한 도형들을 그룹으로 지정할 수 있습니다.");
        			isGroupMode=true;
        			Log.d("isExplorerMode1", "isExplorerMode1 + " + isExplorerMode);
        			setMode();
        			Log.d("isExplorerMode1", "isExplorerMode1 + " + isExplorerMode);
        			break;
        		}
        	}
        }); 
                
        
        /*
         * 탐색-추가메뉴
         */
        ActionItem polygon3Item 	= new ActionItem(ID_ONE, "3각", getResources().getDrawable(R.drawable.trigonal_btn));
		ActionItem polygon4Item 	= new ActionItem(ID_TWO, "4각", getResources().getDrawable(R.drawable.square_btn));
        ActionItem polygon5Item 	= new ActionItem(ID_THREE, "5각", getResources().getDrawable(R.drawable.pentagonal_btn));
        ActionItem polygon6Item 	= new ActionItem(ID_FOUR, "6각", getResources().getDrawable(R.drawable.hexagonal_btn));
        ActionItem circleItem 	= new ActionItem(ID_FIVE, "원", getResources().getDrawable(R.drawable.cylinder_btn));
        	
		quickAction101.addActionItem(circleItem);
		quickAction101.addActionItem(polygon6Item);
		quickAction101.addActionItem(polygon5Item);
		quickAction101.addActionItem(polygon4Item);
		quickAction101.addActionItem(polygon3Item);
		
		quickAction101.setPopupView(button,1,4.9);          
        
		quickAction101.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {	
				switch(actionId) {
				case ID_ONE:        		
					print("삼각기둥을 추가합니다.\n원하는 위치에 터치해주세요.");
					tHandler.setMode(3);
					tHandler.setAddNumber(3);
					isExplorerMode = false;
					setMode();
					break;
				case ID_TWO:
					print("사각기둥을 추가합니다.\n원하는 위치에 터치해주세요.");
					tHandler.setMode(3);
					tHandler.setAddNumber(4);
					isExplorerMode = false;
					setMode();
					break;
				case ID_THREE:
					print("오각기둥을 추가합니다.\n원하는 위치에 터치해주세요.");
					tHandler.setMode(3);
					tHandler.setAddNumber(5);
					isExplorerMode = false;
					setMode();
					break;
				case ID_FOUR:
					print("육각기둥을 추가합니다.\n원하는 위치에 터치해주세요.");
					tHandler.setMode(3);
					tHandler.setAddNumber(6);
					isExplorerMode = false;
					setMode();
					break;
				case ID_FIVE:
					print("원기둥을 추가합니다.\n원하는 위치에 터치해주세요.");
					tHandler.setMode(3);
					tHandler.setAddNumber(0);
					isExplorerMode = false;
					setMode();
					break;
				}
			}
		});
                
        
        /*
         * 탐색-저장메뉴
         */
        ActionItem saveSkpItem 	= new ActionItem(ID_ONE, "저장", getResources().getDrawable(R.drawable.save_btn));
      	ActionItem savePdfItem 	= new ActionItem(ID_TWO, "공유", getResources().getDrawable(R.drawable.share_btn));
        	
		quickAction102.addActionItem(saveSkpItem);
		quickAction102.addActionItem(savePdfItem);
		
		quickAction102.setPopupView(button,1.9,2.9);          
        
		quickAction102.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {	
				switch(actionId) {
				case ID_ONE:        			
					saveFile(1);
					break;
				case ID_TWO:
					saveFile(2);
					break;
				}
			}
		});
        		
		
		/*
         * 탐색-배경합성메뉴
         */
        ActionItem cameraItem 	= new ActionItem(ID_ONE, "촬영", getResources().getDrawable(R.drawable.camera_btn));
		ActionItem galleryfItem 	= new ActionItem(ID_TWO, "갤러리", getResources().getDrawable(R.drawable.gallery_btn));
        	
		quickAction103.addActionItem(cameraItem);
		quickAction103.addActionItem(galleryfItem);
		
		quickAction103.setPopupView(button,2.8,2.9);          
        
		quickAction103.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
        	@Override
        	public void onItemClick(QuickAction source, int pos, int actionId) {        		
        		switch(actionId) {
        		//카메라로 촬영
        		case ID_ONE:
        			
        			quickAction.dismiss();
        			//SD Card 인식 없으면 촬영안함
        			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//        				//경로 설정
//        				String SD_path = Environment.getExternalStorageDirectory().getAbsolutePath();
//        				SD_path += "/bgImg";        				
//        				File imgFile = new File(SD_path);
//        				if(!imgFile.exists()) {
//        					imgFile.mkdir();
//        				}
//        				
//        				SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//        				String filename = "/bg " + timeFormat.format(new Date()).toString() + ".jpg";
//        				
//        				imgFile = new File(SD_path + filename);
//        				img_uri = Uri.fromFile(imgFile);
//        				
//        				//카메라 app 실행
//        				Intent mIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
//        				mIntent.putExtra(MediaStore.EXTRA_OUTPUT, img_uri);
//        				startActivityForResult(mIntent, REQUEST_CODE_CAMERA);
        				
        				//Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        				
        				Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class); 
                        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA); 
        			}
        			break;
        		case ID_TWO:
        			quickAction.dismiss();
        			
        			//갤러리 app 실행
        			Intent mIntent = new Intent (Intent.ACTION_GET_CONTENT);
        			mIntent.setType("image/*");
        			startActivityForResult(mIntent, REQUEST_CODE_GALLERY);        			
        			break;
        		}
        	}
        });
        		
		
		/*
		 * 선택 메뉴
		 * 변형 - 복사, 이동, 회전, 삭제
		 * 표면 - 투명화, 텍스쳐
		 * 점선택 - 선택된 도형내의 점 선택
		 * 취소 - 실행취소(undo) 
		 */
		ActionItem transformItem 	= new ActionItem(ID_ONE, "변형", getResources().getDrawable(R.drawable.transform_btn));
		ActionItem surfaceItem 	= new ActionItem(ID_TWO, "표면", getResources().getDrawable(R.drawable.surface_btn));
        ActionItem ptSelectItem 	= new ActionItem(ID_THREE, "점선택", getResources().getDrawable(R.drawable.selectpoint_btn));
        ActionItem undoItem 	= new ActionItem(ID_FOUR, "그룹해제", getResources().getDrawable(R.drawable.ungroup_btn));
        
        transformItem.setSticky(true);
        surfaceItem.setSticky(true);    
       
        quickAction2.addActionItem(transformItem);
		quickAction2.addActionItem(surfaceItem);
        quickAction2.addActionItem(ptSelectItem);
        quickAction2.addActionItem(undoItem);
        
        quickAction2.setPopupView(button, 1, 1);
        
        quickAction2.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
        	@Override
        	public void onItemClick(QuickAction source, int pos, int actionId) {	        		
        		switch(actionId) {
        		case ID_ONE:
        			print("선택된 도형이나 그룹을 변형시킬 수 있습니다.");
        			quickAction201.showAddPopupWindow();        			
        			break;
        		case ID_TWO:
        			print("선택된 도형이나 그룹의 표면을 설정하거나 투명하게 변경할 수 있습니다.");
        			quickAction202.showAddPopupWindow();
        			break;
        		case ID_THREE:
        			print("선택된 도형의 점들을 선택후 이동시켜 도형의 크기나 형태를 변형시킬 수 있습니다.");
        			isTransformMode=true;
        			isPtSelectMode=true;
        			tHandler.setMode(6);
        			quickAction2.dismiss();
        			
        			if(isPtSelectMode)
					{					
						
							//c0 = new Cylinder(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, , SquarePrism.getCalculatedVertex()[0].getY(), SquarePrism.getCalculatedVertex()[0].getZ());							
							
						
							Log.d("c0", "c0");
							c0 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getZ());
							Log.d("c1", "c1");
							c1 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[1].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[1].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[1].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[1].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[1].getZ());
							Log.d("c2", "c2");
							c2 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[2].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[2].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[2].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[2].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[2].getZ());
							Log.d("c3", "c3");
							c3 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[3].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[3].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[3].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[3].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[3].getZ());
							
							Log.d("c4", "c4");
							c4 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[4].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[4].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[4].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[4].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[4].getZ());
							Log.d("c5", "c5");
							c5 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[5].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[5].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[5].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[5].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[5].getZ());
							Log.d("c6", "c6");
							c6 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[6].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[6].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[6].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[6].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[6].getZ());
							Log.d("c7", "c7");
							c7 = new SquarePrism(new float[]{glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[7].getX(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[7].getY(),glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[7].getZ()},glRenderer.prismList.get(selectedPrism),glRenderer, glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[0].getX(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[7].getY(), glRenderer.prismList.get(selectedPrism).getCalculatedVertex()[7].getZ());
							
							glRenderer.ptPrismList.add(c0);
							glRenderer.ptPrismList.add(c1);
							glRenderer.ptPrismList.add(c2);
							glRenderer.ptPrismList.add(c3);
							
							glRenderer.ptPrismList.add(c4);
							glRenderer.ptPrismList.add(c5);
							glRenderer.ptPrismList.add(c6);
							glRenderer.ptPrismList.add(c7);						
					}
					setMode();
        			switchVisibility();        			
        			tmpMode = 4;
        			initSeekBar();
					
        			break;
        		case ID_FOUR:
        			print("선택된 그룹을 해제합니다.");
        			tHandler.clearGroup();
        			setMode();
        			break;
        		}
        	}
        }); 
        
        
        /*
         * 선택-변형메뉴
         */
        ActionItem copyItem 	= new ActionItem(ID_ONE, "복사", getResources().getDrawable(R.drawable.copy_btn));
		ActionItem moveItem 	= new ActionItem(ID_TWO, "이동", getResources().getDrawable(R.drawable.move_btn));
        ActionItem rotateItem 	= new ActionItem(ID_THREE, "회전", getResources().getDrawable(R.drawable.rotation_btn));
        ActionItem deleteItem = new ActionItem(ID_FOUR, "삭제", getResources().getDrawable(R.drawable.delete_btn));
        	
		quickAction201.addActionItem(deleteItem);
		quickAction201.addActionItem(rotateItem);
		quickAction201.addActionItem(moveItem);
		quickAction201.addActionItem(copyItem);
		
		quickAction201.setPopupView(button,1,4.2);          
        
        quickAction201.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
        	@Override
        	public void onItemClick(QuickAction source, int pos, int actionId) {	        		
        		switch(actionId) {
        		case ID_ONE:              			
        			//copy selected objects
        			print("선택된 도형(그룹)을 복사하였습니다.");
        			if(tHandler.alreadyGrouped==true)
        				glRenderer.copyPrism(selectIndex);
        			else
        				glRenderer.copyPrism(selectedPrism);
        			break;
        		case ID_TWO:        			
        			//move selected objects
        			print("선택된 도형(그룹)을 이동합니다.\n기준축을 선택후 슬라이드바를 조정해 이동시킬 수 있습니다.");
        			isTransformMode=true;
        			setMode();
        			switchVisibility();        			
        			tmpMode = 1;
        			initSeekBar();
        			quickAction2.dismiss();
        			break;
        		case ID_THREE:             			
        			//rotate selected objects
        			print("선택된 도형(그룹)을 회전합니다.\n기준축을 선택후 슬라이드바를 조정해 회전시킬 수 있습니다.");
        			isTransformMode=true;
        			setMode();
        			switchVisibility();  
        			tmpMode = 2;
        			initSeekBar();        			
        			quickAction2.dismiss();
        			break;
        		case ID_FOUR:
        			//delete selected objects
        			print("선택된 도형(그룹)을 삭제하였습니다.");
        			if(tHandler.alreadyGrouped==true)
        				glRenderer.deletePrism(selectIndex);
        			else
        				glRenderer.deletePrism(selectedPrism);
        			break;       			
        		}
        	}
        });
        
        quickAction201.setOnDismissListener(new QuickAction.OnDismissListener() {			
        	@Override
        	public void onDismiss() {
        		quickAction2.dismiss();
        	}
        });    
        
        /*
         * 선택-표면메뉴
         */        
        ActionItem transparencyItem = new ActionItem(ID_ONE, "투명화", getResources().getDrawable(R.drawable.invisible_btn));
        ActionItem textureItem 	= new ActionItem(ID_TWO, "텍스쳐", getResources().getDrawable(R.drawable.texture_btn));
        	
		quickAction202.addActionItem(textureItem);
		quickAction202.addActionItem(transparencyItem);
		
		quickAction202.setPopupView(button,1.9,2.9);          
        
        quickAction202.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
        	@Override
        	public void onItemClick(QuickAction source, int pos, int actionId) {	        		
        		switch(actionId) {
        		case ID_ONE:        			
        			//transparent selected objects
        			print("선택된 도형(그룹)이 투명화되었습니다.");
        			if(tHandler.alreadyGrouped==true)
        				glRenderer.transparentPrism(selectIndex);
        			else
        				glRenderer.transparentPrism(selectedPrism);
        			break;
        		case ID_TWO:
        			//set texture selected objects
        			Intent myIntent = new Intent(getApplicationContext(), TextureActivity.class);
    				startActivityForResult(myIntent, REQUEST_CODE_TEXTURE);
        			break;        			      			
        		}
        	}
        });
        
        quickAction202.setOnDismissListener(new QuickAction.OnDismissListener() {			
        	@Override
        	public void onDismiss() {
        		quickAction2.dismiss();
        	}
        });
        createObjFromFile();
	}
	
	public void setMode() {
		
		button = (ImageButton) this.findViewById(R.id.button); 
		
		//탐색모드 또는 선택모드 메뉴 생성
		if(isExplorerMode) {
			button.setBackgroundResource(R.drawable.explorer_btn);
			print("탐색모드 입니다.");
		}
		else {
			button.setBackgroundResource(R.drawable.select_btn);
			print("선택모드 입니다.");
		}
		
		if(isTransformMode || isGroupMode || isBackgroundMode)
			button.setBackgroundResource(R.drawable.complete_btn);
		
        button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isTransformMode&&isPtSelectMode) {
//					Log.d("isTransformMode", "isTransformMode : " + isTransformMode);
					isTransformMode = false;
					isPtSelectMode = false;
//					Log.d("isTransformMode", "isTransformMode : " + isTransformMode);
//					Log.d("isPtSelectMode", "isPtSelectMode : " + "isPtSelectMode");
//					
					switchVisibility();
					mode = 0;
					tmpMode = 0;	
					isExplorerMode=true;
					setMode();
					
					
				}
				else if(isTransformMode) {
//					Log.d("isTransformMode", "isTransformMode : " + isTransformMode);
					isTransformMode = false;
//					Log.d("isTransformMode", "isTransformMode : " + isTransformMode);
//					Log.d("isPtSelectMode", "isPtSelectMode : " + "isPtSelectMode");
//					
					switchVisibility();
					mode = 0;
					tmpMode = 0;	
					setMode();
					
					
				}
				else if(isBackgroundMode) {
					isBackgroundMode = false;
					backgroundModeVisibility();
					mode = 0;
					tmpMode = 0;
					setMode();
					glRenderer.offBackgroundMode();
					glSurface.setOnTouchListener(tHandler);
				}
				else if(isGroupMode) {
					isGroupMode = false;
					isExplorerMode=true;
						
					HashSet<Integer> temp = (HashSet<Integer>) tHandler.getNewGroup().clone();
					
					//그룹이 한개면 추가가 안된다.
					if(temp.size()!=1){
						glRenderer.groupManager.groupAdd(temp);
						tHandler.getNewGroup().clear();
					}
					//
					for(int i=0;i<glRenderer.prismList.size();i++)
					{
						glRenderer.prismList.get(i).selectPrism(false);
						glRenderer.prismList.get(i).changeLineColor();
					}
					//tHandler.getNewGroup().clear();
					tHandler.checkSelected = false;
					setMode();
					
				}
				
				else if(isExplorerMode) {
					quickAction.show(v);
					quickAction.setAnimStyle(QuickAction.ANIM_REFLECT);
				}
				else {
					quickAction2.show(v);
					quickAction2.setAnimStyle(QuickAction.ANIM_REFLECT);					
				}
			}
		}); 
		
	}
		
	public void backgroundModeVisibility() {
		if(!isBackgroundMode) 
			seekbar.setVisibility(View.INVISIBLE);			
		else 
			seekbar.setVisibility(View.VISIBLE);			
	}
	
	public void switchVisibility() {
		if(!isTransformMode) {
			xBtn.setVisibility(View.INVISIBLE);
			yBtn.setVisibility(View.INVISIBLE);
			zBtn.setVisibility(View.INVISIBLE);
			seekbar.setVisibility(View.INVISIBLE);
		}
		else {
			xBtn.setVisibility(View.VISIBLE);
			yBtn.setVisibility(View.VISIBLE);
			zBtn.setVisibility(View.VISIBLE);
			seekbar.setVisibility(View.VISIBLE);
		}
	}
	
	public void initSeekBar() {
		if(tmpMode==1) {
			seekbar.setMax(60);
			seekbar.setProgress(30);
			tmpProgress = 30;
		}
		else if(tmpMode ==2) {
			seekbar.setMax(72);
			seekbar.setProgress(36);
			tmpProgress = 36;
		}
		else if(tmpMode==4) {
			seekbar.setMax(60);
			seekbar.setProgress(30);
			tmpProgress = 30;
		}
		mode = tmpMode;
	}
	
	/**
	 * Remember to resume the glSurface
	 */
	@Override
	protected void onResume() {
		super.onResume();		
	}

	/**
	 * Also pause the glSurface
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	//result method for another activity which are running from EditActivity
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == REQUEST_CODE_TEXTURE && data != null) {
			print("선택된 도형(그룹)의 텍스쳐가 변경 되었습니다.");
			int index = data.getExtras().getInt("tex_index");
			
			if(tHandler.alreadyGrouped==true)
				glRenderer.texturePrism(selectIndex, index);
			else
				glRenderer.texturePrism(selectedPrism, index);
		}
		else if(requestCode == REQUEST_CODE_CAMERA) {
			if(resultCode == Activity.RESULT_OK && data != null) {
				
	            String picturePath = data.getExtras().getString("pic_path");
	          
	            Bitmap bg = BitmapFactory.decodeFile(picturePath);
				glRenderer.setBackground(bg);	  
				MoveHandler mHandler = new MoveHandler(glRenderer);
				glSurface.setOnTouchListener(mHandler);
				isBackgroundMode = true;
				setMode();
				selectAxis = 3;
				tmpMode = 2;
				initSeekBar();
				mode = 3;
				backgroundModeVisibility();
				print("배경이 배치되었습니다.");				
			}
			
		}
		else if(requestCode == REQUEST_CODE_GALLERY) {
			if(resultCode == Activity.RESULT_OK && data != null) {
				Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
	            cursor.moveToFirst();
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
	            Bitmap bg = getScaledBitmap(picturePath, 1560, 2104);
				glRenderer.setBackground(bg);	  
				MoveHandler mHandler = new MoveHandler(glRenderer);
				glSurface.setOnTouchListener(mHandler);
				isBackgroundMode = true;
				setMode();
				selectAxis = 3;
				tmpMode = 2;
				initSeekBar();
				mode = 3;
				backgroundModeVisibility();
				print("배경이 배치되었습니다.");
			}
		}
		if(requestCode == REQUEST_CODE_SAVE_IMAGE && data != null) {
			boolean confirm = data.getExtras().getBoolean("confirmCapture", false);
			String strPath = data.getExtras().getString("ImageFilePath", "");
			
			if(confirm) {
				glRenderer.setTextureModeList(); // capture위해 투명화
			}
			glRenderer.setScreenCapture(confirm, strPath);
		}
		else {
			glRenderer.setScreenCapture(false, "");
		}
	}
	
	private Bitmap getScaledBitmap(String picturePath, int width, int height) {
	    BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
	    sizeOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(picturePath, sizeOptions);

	    int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

	    sizeOptions.inJustDecodeBounds = false;
	    sizeOptions.inSampleSize = inSampleSize;

	    return BitmapFactory.decodeFile(picturePath, sizeOptions);
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and
	        // width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will
	        // guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
	
	public static void print(String str) {		
		txtview.setText(str);
	}

	public void setSelectedPrism(int i) {
		this.selectedPrism=i;
	}
	
	public int getSelectedPrism(){
		return this.selectedPrism;
	}

	public HashSet <Integer> getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(HashSet<Integer> selectIndex) {
		this.selectIndex = selectIndex;
	}
	
	public void createObjFromFile() {
		
		Intent intent = getIntent();
		this.existData = intent.getBooleanExtra("FileLoad", false);
		
		// file open activity에서 edit activity로 data 전달 방법을 아직 못찾아서 우선 임시 파일로 처리
		// --> 찾아보니 전달할 object를 직렬화 하여 activity간 전달 가능한데, 우선 나중에 수정하는 걸로 (다른 게 더 급함)
		if(this.existData) {
			FileToPrism fileTo = new FileToPrism(this);
			fileTo.dataProcess();
        }
	}
	
	public void setTextureIndex(int i, int j) {
		glRenderer.texturePrism(i, j%9);
	}
	
	public void setData(int mode, int nType, boolean isExplorer,  Vertex[] vPos, float[] angleData) {
		tHandler.setMode(mode);
		tHandler.setAddNumber(nType);
		isExplorerMode = isExplorer;
		setMode();
		
		glRenderer.onTouch(screenWidth/2.01f, screenHeight/2.01f);
		glRenderer.makePrism(nType, vPos);
		glRenderer.prismList.get(glRenderer.prismList.size() - 1).selectPrism(true);
		glRenderer.prismList.get(glRenderer.prismList.size() - 1).changeLineColor();
		
		for(int i=0; i<3; i++) {
			glRenderer.prismList.get(glRenderer.prismList.size() - 1).setRotationAngle(i+1, angleData[i]);
		}
		
		setSelectedPrism(glRenderer.prismList.size() - 1);
		isGroupMode = false;
		
		tHandler.checkSelected=true;
		tHandler.alreadyGrouped=false;
		tHandler.setMode(tHandler.getModeDefault());
	}
	
	public void saveFile(int nFileType) {		
		Intent myIntent = new Intent(getApplicationContext(), FileSaveActivity.class);
		myIntent.putExtra("SaveType", nFileType);
		
		PrismToFile prismTo = new PrismToFile(this, glRenderer, nFileType);
		prismTo.dataProcess();
		
		if(nFileType == 1) {
			startActivity(myIntent);
		}
		else {	
			startActivityForResult(myIntent, REQUEST_CODE_SAVE_IMAGE);
		}
	}

	public TouchHandler gettHandler() {
		return tHandler;
	}

	public void settHandler(TouchHandler tHandler) {
		this.tHandler = tHandler;
	}
}


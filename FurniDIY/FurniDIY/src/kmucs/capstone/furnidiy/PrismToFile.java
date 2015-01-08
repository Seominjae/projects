package kmucs.capstone.furnidiy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import android.os.Environment;

// yhchung
// save

// prism의 vertex 등을 임시파일에 저장

public class PrismToFile {
	EditActivity editActivity = new EditActivity();
	EditRenderer editRenderer;
	private int nFileType;
	private int nType;
	private float fDivideUnit = 250.0f; // 3ds와 furniDIY간 단위를 맞춰야
	
	public PrismToFile(EditActivity editActivity, EditRenderer editRenderer, int nFileType) {
		this.editActivity = editActivity;
		//this.editRenderer = new EditRenderer(this.editActivity);
		this.editRenderer = editRenderer;
		this.nFileType = nFileType;
	}
	
	void dataProcess() {
		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/_tempData_.txt";
		
		try {
			FileWriter fw = new FileWriter(path);
			BufferedWriter bw = new BufferedWriter(fw);
			Vertex[] readData = new Vertex[48];
			
			for(int i=0; i<this.editRenderer.prismList.size(); i++) {
				
				int n = this.editRenderer.prismList.get(i).getPrismSize();
				String strName = "";
				
				if(n==3) {
					strName = "Trigonal";
				}
				else if(n==4) {
					strName = "Square";
				}
				else if(n==5) {
					strName = "Pentagonal";
				}
				else if(n==6) {
					strName = "Hexagonal";
				}
				else if(n==0) {
					
				}
				
				bw.write("Name: " + strName);		bw.newLine();
				bw.write("Type:");		bw.newLine();
				bw.write(Integer.toString(n));		bw.newLine();
				
				float[] angle = this.editRenderer.prismList.get(i).getRotationAngle();
				bw.write("Angle:");		bw.newLine();
				for(int j=0; j<angle.length; j++) {
					bw.write(Float.toString(angle[j]));	bw.newLine();
				}
				
				float[] movingPoint = this.editRenderer.prismList.get(i).getMovingPoints();
				readData = this.editRenderer.prismList.get(i).getVertex();
				for(int j=0; j<this.editRenderer.prismList.get(i).getPrismSize()*2; j++) {
					bw.write(Float.toString( (readData[j].getX()+movingPoint[0]) * fDivideUnit));		bw.newLine();
	            	bw.write(Float.toString( (readData[j].getY()+movingPoint[1]) * fDivideUnit));		bw.newLine();
	            	bw.write(Float.toString( (readData[j].getZ()+movingPoint[2]) * fDivideUnit));		bw.newLine();
				}
		
				bw.write("OneEnd");		bw.newLine();
			}
			
			bw.write("AllEnd");		bw.newLine();
			bw.close();
		}
		catch(Exception e) {
			System.out.println (e.toString());
		}
	}
}

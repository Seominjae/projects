package kmucs.capstone.furnidiy;

import java.io.File;
import java.io.FileInputStream;
import java.io.*;

// yhchung
// parsing 

public class DataFileLoader implements Logger {
	
	String rootPath;
	String filePath;
    private File file; 

    public void setFilePath(String filePath, String rootPath) {
		this.filePath = filePath;
		file = new File(filePath); //"src/test/resources/3ds_zero.3ds");
		this.rootPath = rootPath;
	}

	public void parseModel() throws Exception {
        FileInputStream fis = new FileInputStream(file);
        MapReader reader = new MapReader(fis.getChannel());
        Parser parser = new Parser(reader);
        parser.setLogger(this);
        Model model = parser.parseFile();
        saveModel(model);
    }
	
	private void saveModel(Model model) {
		
		// file open activity에서 edit activity로 data 전달 방법을 아직 못찾아서 우선 임시 파일로 처리
		// --> activity간 object 전달 가능한데, 우선 나중에 수정하는 걸로 (다른 게 더 급함)
		try	{
			FileWriter fw = new FileWriter(rootPath+"_tempData_.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			Vertex[] readData = new Vertex[48];//144];
			
			for (ModelObject object : model.objects) {
	            if (object.vertices != null) {   
	            	// model의 name 쓰고
	                bw.write("Name: " + object.getName());		bw.newLine();
	                
	                // model의 type 쓰고
	                int cnt = object.vertices.length / 3;
	                int idx = 0;
	                boolean bMatch = false;
	                
	                bw.write("Type:");		bw.newLine();
	                switch(cnt)
	                {
	                case 18:
	                	bw.write("3");		bw.newLine();
	                	readData = new Vertex[6];//18];
	                	break;
	                case 24:
	                	bw.write("4");		bw.newLine();
	                	readData = new Vertex[8];//24];
	                	break;
	                case 30:
	                	bw.write("5");		bw.newLine();
	                	readData = new Vertex[10];//30];
	                	break;
	                case 36:
	                	bw.write("6");		bw.newLine();
	                	readData = new Vertex[12];//36];
	                	break;
	                default:
	                	//bw.write("0");		bw.newLine();
	                	//readData = new Vertex[48];//144];
						bw.write("4");		bw.newLine();
	                	readData = new Vertex[8];//24];
	                	break;
	                }
	                
	                if(object.angle != null) {
		                bw.write("Angle:");		bw.newLine();
		                for(int i=0; i<3; i++) {
		                	bw.write(Float.toString(object.angle[i]));		bw.newLine();
		                }
	                }
	                
	                // 첫번째값은 우선 넣고.
	                readData[idx] = new Vertex();
	                readData[idx].setX(object.vertices[0]);
	                readData[idx].setY(object.vertices[1]);
	                readData[idx++].setZ(object.vertices[2]);
	                
	                for(int i=3; i<object.vertices.length; i=i+3) {
	                	bMatch = false;
	                	for(int j=0; j<idx; j++) { // 기존에 넣은 값인지 비교
	                		if( (readData[j].getX() == object.vertices[i]) &&
	                			(readData[j].getY() == object.vertices[i+1]) &&
	                			(readData[j].getZ() == object.vertices[i+2]) ) {
	                			bMatch = true;
	                			break;
	                		}
	                	}
	                	
	                	if(bMatch)
	                		continue;
	                	
	                	readData[idx] = new Vertex();
	                	readData[idx].setX(object.vertices[i]);
	                	readData[idx].setY(object.vertices[i+1]);
	                	readData[idx++].setZ(object.vertices[i+2]);
	                }
	                
	                // model의 vertecies 쓰고
	                for(int j=0; j<idx; j++) {
	                	bw.write(Float.toString(readData[j].getX()));		bw.newLine();
	                	bw.write(Float.toString(readData[j].getY()));		bw.newLine();
	                	bw.write(Float.toString(readData[j].getZ()));		bw.newLine();
	                }
	                
	                // model의 end 쓰고 - 필요할지도
	                bw.write("OneEnd");		bw.newLine();
	            }
	        }
			// 파일의 끝 쓰고 - 필요할지도
			bw.write("AllEnd");		bw.newLine();
			bw.close();
		}
		catch (IOException e) {
			System.err.println(e);
            System.exit(1);
		}
	}

    public void log(String s) {
        System.out.println(s);
    }
}

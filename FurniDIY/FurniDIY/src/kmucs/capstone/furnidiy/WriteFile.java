package kmucs.capstone.furnidiy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.os.Environment;

// yhchung
// save

// 3ds규격으로 binary 파일 저장 - 아직 100% 호환안됨

public class WriteFile {
	
	String filePath = "";
	int nModel = 0;
	FileOutputStream fOut;
	int countFace = 12;
	
	public void writeTailerDataChunk(int nPrism) { 
		try {
			// 앞에 MainDataChunk에서
			// x4000, x4100, x4110, x4140, x4160, x4120, x4130, x4150이 반복된 이후에
			// obj가 여러 개라면(1개 초과)
			if(nPrism > 1) {
				// 여기서 0x4000
				writeOneChunkStr(0x40, 0x00, 65, "Model"); // 0x4000 : Object Block (2+4+6)
				fOut = new FileOutputStream(this.filePath, true);
				writeOneChunk(0x41, 0x00, 60, true); // 0x4100 : Triangular Mesh (2+4)
						
				float[] fVals = new float[12];
				for(int i=0; i<fVals.length; i++) {
					fVals[i] = 0.0f;
				}
				writeOneChunkFloatValArr(0x41, 0x60, 54, fVals, false); // 0x4160을 먼저 넣어줘야 한다. (2+4+(4*12)) 
			}			
			writeOneChunk(0xb0, 0x00, 419, false); // 그 다음에  b000 (2+4)			
			writeOneChunkName(0xb0, 0x0a, 19, 5, "LIB3DS"); // b00a (2+4+2+7)
			fOut = new FileOutputStream(this.filePath, true);
			writeIntData(100, true); // (4)
			int[] nVals = {0, 0};
			writeOneChunkShortVal(0xb0, 0x08, 14, nVals); // b008 (2+4+(2*2))
			writeIntData(100, false); // (4)
			nVals[1] = 100;
			writeOneChunkShortVal(0xb0, 0x09, 10, nVals); // b009 (2+4+(2*2))			
			writeOneChunk(0xb0, 0x04, 71, false); // b004 (2+4)
			int[] nVal = {1};
			writeOneChunkShortVal(0xb0, 0x30, 8, nVal); // b030 (2+4+2)
			
			writeOneChunkStr(0xb0, 0x10, 19, "Camera"); // b010 (2+4+7)
			fOut = new FileOutputStream(this.filePath, true);
			int[] nValNode = {-1, 0, 0};
			writeShortDataArr(nValNode, true); // b010 (2*3)			
			
			// 현재 값은 대부분 0이라도 아래 다른 chunk에서 값이 바뀔지 모르니 우선 변수는 다 만들고..
			int nFlag = 0, nKey = 1, nUnknown2 = 0;
			int[] nUnknown1 = {0,0,0,0};			
			int[] frame = {0};
			int[] nUnknown3 = {0};
			float[] fPos = {-5854.524414f, 11891.424805f, -12324.120117f};
			
			// b020 : (2+4+2+2*4+2+2) + ( (2+4+(4*3))*key )
			writeOneChunkStrutHeader(0xb0, 0x20, 38, nFlag, nUnknown1, nKey, nUnknown2); 
			writeOneChunkStrutMainDataA(frame, nUnknown3, fPos);			
			writeOneChunk(0xb0, 0x03, 131, false); // b003 (2+4)		
			nVal[0] = 2;
			writeOneChunkShortVal(0xb0, 0x30, 8, nVal); // b030 (2+4+2)
			writeOneChunkStr(0xb0, 0x10, 19, "Camera"); // b010 (2+4+7)
			fOut = new FileOutputStream(this.filePath, true);
			writeShortDataArr(nValNode, true); // b010 (2*3)
			
			// b020 : (2+4+2+2*4+2+2) + ( (2+4+(4*3))*key )
			fPos[0] = 3629.746582f;
			fPos[1] = 3066.780273f;
			fPos[2] = 4660.624512f;
			writeOneChunkStrutHeader(0xb0, 0x20, 38, nFlag, nUnknown1, nKey, nUnknown2);
			writeOneChunkStrutMainDataA(frame, nUnknown3, fPos);
			
			// b023 : (2+4+2+2*4+2+2) + ( (2+4+4)*key ) 
			float fData = 46.666668f;
			writeOneChunkStrutHeader(0xb0, 0x23, 30, nFlag, nUnknown1, nKey, nUnknown2);
			writeOneChunkStrutMainDataC(frame, nUnknown3, fData);
			
			// b024 : (2+4+2+2*4+2+2) + ( (2+4+4)*key )
			fData = 0.000000f;
			writeOneChunkStrutHeader(0xb0, 0x24, 30, nFlag, nUnknown1, nKey, nUnknown2);
			writeOneChunkStrutMainDataC(frame, nUnknown3, fData);
			// 까지 써주고
			
			
			// 여기서부터 크기 다시 확인할것
			for(int i=0; i<nPrism; i++) {
				writeOneChunk(0xb0, 0x02, 168, false); // b002 : OBJECT_NODE_TAG (2+4)
				
				nVal[0] = 0;
				writeOneChunkShortVal(0xb0, 0x30, 8, nVal); // b030 NODE ID : (2+4+2)
				
				writeOneChunkStr(0xb0, 0x10, 18, "Model"); // b010 NODE_HDER : (2+4+6)
				fOut = new FileOutputStream(this.filePath, true);
				nValNode[0] = -1;
				nValNode[2] = nValNode[1] = 0;
				writeShortDataArr(nValNode, true); // b010 NODE_HDER : (2*3)
				
				float[] pivot = {0f, 0f, 0f};
				writeOneChunkFloatValArr(0xb0, 0x13, 18, pivot, false); // b013 PIVOT : (2+4+(4*3)) 
	
				// b020 : POS TRACK TAG (2+4+2+2*4+2+2) + ( (2+4+(4*3))*key )
				for(int j=0; j<fPos.length; j++) {
					fPos[j] = 0f;
				}
				writeOneChunkStrutHeader(0xb0, 0x20, 38, nFlag, nUnknown1, nKey, nUnknown2);
				writeOneChunkStrutMainDataA(frame, nUnknown3, fPos);
				
				// b021 : ROT TRACK TAG (2+4+2+2*4+2+2) + ( (2+4+4+(4*3))*key )
				float[] fRot = {0f};
				fPos[2] = 1f;
				writeOneChunkStrutHeader(0xb0, 0x21, 42, nFlag, nUnknown1, nKey, nUnknown2);
				writeOneChunkStrutMainDataB(frame, nUnknown3, fRot, fPos);
				
				// b022 : SCL TRACK TAG (2+4+2+2*4+2+2) + ( (2+4+(4*3))*key )
				for(int j=0; j<fPos.length; j++) {
					fPos[j] = 1f;
				}
				writeOneChunkStrutHeader(0xb0, 0x22, 38, nFlag, nUnknown1, nKey, nUnknown2);
				writeOneChunkStrutMainDataA(frame, nUnknown3, fPos);
				// 를 obj만큼 반복해서  써준다.				
			}
		} 
		catch(Exception e) {
			System.out.println (e.toString());
		}
	}
	
	public void writeMainDataChunk(Vertex[] readData, int nPrism, int nSize, float[] angleData, String name) {
		try {
			// 여기도 아래 header쓰는 함수와 마찬가지로...
			// 굳이 chunk별로 나눌필요는 없는 데, 문서상의 문제도 있고 해서 개별적으로 함수 만듬.  
			
			float[] fVals = new float[angleData.length];
			for(int i=0; i<fVals.length; i++) {
				fVals[i] = angleData[i];
			}
			writeOneChunkFloatValArr(0x78, 0x90, 12, fVals, false); // 0x7890 (2+4+4+4+4) <-- 3ds format 아님
			
			writeOneChunkStr(0x40, 0x00, 767, "Model"); // 0x4000 : Object Block (2+4+6)
			fOut = new FileOutputStream(filePath, true);			 
			writeOneChunk(0x41, 0x00, 755, true); // 0x4100 : Triangular Mesh (2+4) 
						
			
			writeVerticesListChunk(readData, nSize); // 0x4110 : Vertices List
			writeMappingCoordinatesListChunk(); // 0x4140 : Mapping Coordinates List (2+4+2+(48*4)
			
			float[] fValCoord = new float[12];
			for(int i=0; i<fValCoord.length; i++) {
				fValCoord[i] = 0.0f;
			}
			fValCoord[0] = fValCoord[4] = fValCoord[8] = 1f;
			writeOneChunkFloatValArr(0x41, 0x60, 54, fValCoord, false); // 0x4160 : Local Coordinates List (2+4+(4*12))
			writeFaceDescriptionChunk(); // 0x4120 : Faces Description (2+4+2+(2*12*4))
			writeOneChunkStr(0x41, 0x30, 41, "FrontCol"); // 0x4130 : MSH_MAT_GROUP (2+4+9)
			fOut = new FileOutputStream(filePath, true);
			writeMshMatGroup(); // // 0x4130 : MSH_MAT_GROUP (2+2*12)
			
			int[] nVals = new int[countFace*2];
			for(int i=0; i<nVals.length; i++) {
				nVals[i] = 0;
			}
			writeOneChunkShortVal(0x41, 0x50, 54, nVals); // 0x4150 : SMOOTH_GROUP (2+4+(2*countFace*2))
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
		
	}
			
	public void writeHeaderDataChunk(int nSize) {
		try {		
			writeOneChunk(0x4d, 0x4d, nSize, false); // 0x4d4d : MainChunk (2+4) 
			
			int[] nVal = {3};
			writeOneChunkArr(0x00, 0x02, 10, nVal); // 0x0002 : M3D Version (2+4+4)
			writeOneChunk(0x3d, 0x3d, 1045, false); // 0x3d3d : 3D Editor Chunk (2+4)
			nVal[0] = 3;
			writeOneChunkArr(0x3d, 0x3e, 10, nVal); // 0x3d3e : Mesh Version (2+4+4)
			
			float[] fVal = {0.039370f};
			writeOneChunkFloatValArr(0x01, 0x00, 10, fVal, false); // 0x0100 : Master Scale (2+4+4)			
			writeOneChunk(0xaf, 0xff, 187, false); // 0xafff : Material Block (2+4)
			writeOneChunkStr(0xa0, 0x00, 15, "FrontCol"); // 0xa000 : Material Name (2+4+9)	
			fOut = new FileOutputStream(this.filePath, true);			
			writeOneChunk(0xa0, 0x10, 24, true); // 0xa010 : mat ambient (2+4)
			writeColorChunk(0x00, 0x11, 9, 0); // 0x0011 : color_24 (2+4+3)			
			writeColorChunk(0x00, 0x12, 9, 0); // 0x0012 : lin_color_24 (2+4+3)
			writeOneChunk(0xa0, 0x20, 24, false); // a020 : mat_diffuse (2+4)
			writeColorChunk(0x00, 0x11, 9, -1); // 0x0011 : color_24 (2+4+3)
			writeColorChunk(0x00, 0x12, 9, -1); // 0x0012 : lin_color_24 (2+4+3)
			writeOneChunk(0xa0, 0x30, 24, false); // a030 : mat specular (2+4)
			writeColorChunk(0x00, 0x11, 9, 84); // 0x0011 : color_24 (2+4+3)			
			writeColorChunk(0x00, 0x12, 9, 84); // 0x0012 : lin_color_24 (2+4+3)
			writeOneChunk(0xa0, 0x40, 14, false); // a040 : mat shineiness (2+4)
			nVal[0] = 0;
			writeOneChunkShortVal(0x00, 0x30, 8, nVal); // 0030 : int percentage (2+4+2)
			writeOneChunk(0xa0, 0x41, 14, false); // a041 : mat shin2pct (2+4)
			writeOneChunkShortVal(0x00, 0x30, 8, nVal); // 0030 : int percentage (2+4+2)
			writeOneChunk(0xa0, 0x50, 14, false); // a050 : mat transparancy (2+4)
			writeOneChunkShortVal(0x00, 0x30, 8, nVal); // 0030 : int percentage (2+4+2)
			writeOneChunk(0xa0, 0x52, 14, false); // a052 : mat xpfall (2+4)
			writeOneChunkShortVal(0x00, 0x30, 8, nVal); // 0030 : int percentage (2+4+2)
			nVal[0] = 3;
			writeOneChunkShortVal(0xa1, 0x00, 8, nVal); // a100 : mat shading (2+4+2)
			writeOneChunk(0xa0, 0x53, 14, false); // a053 : mat_refblur (2+4)
			nVal[0] = 0;
			writeOneChunkShortVal(0x00, 0x30, 8, nVal); // 0030 : int percentage (2+4+2)
			writeOneChunk(0xa0, 0x81, 6, false); // a081 : make two side (2+4)
			
			fVal[0] = 1.0f;
			writeOneChunkFloatValArr(0xa0, 0x87, 10, fVal, false); // a087 : mat wiresize (2+4+4)			
			writeOneChunkStr(0x40, 0x00, 65, "Camera"); // 0x4000 : Object Block (2+4+7)
			fOut = new FileOutputStream(this.filePath, true);
			
			float[] fVals = { 2464.800537f, -2181.572021f, 3010.182129f,
					2135.342285f, -1703.585449f, 2520.890137f,
					0.000000f, 51.428570f};
			writeOneChunkFloatValArr(0x47, 0x00, 52, fVals, true); // 0x4700 : N_Camera (2+4+(8*4))
			
			float[] fVal2 = {1.0f, 25400f};
			writeOneChunkFloatValArr(0x47, 0x20, 14, fVal2, false); // 4720 : cam ranges (2+4+4+4)
		} 
		catch(Exception e) {
			System.out.println (e.toString());
		}
	}
		
	public void createFile(String filePath) {
		
		this.filePath = filePath;
		
		try {
			fOut = new FileOutputStream(filePath);
			
			int nHeader = (2+4)+(2+4+4)+(2+4)+(2+4+4)+(2+4+4)+(2+4)+(2+4+9)+(2+4)+(2+4+3)+(2+4+3)+(2+4)+(2+4+3)+(2+4+3)+(2+4)+(2+4+3)+(2+4+3)+(2+4)+
					(2+4+2)+(2+4)+(2+4+2)+(2+4)+(2+4+2)+(2+4)+(2+4+2)+(2+4+2)+(2+4)+(2+4+2)+(2+4)+(2+4+4)+(2+4+7)+(2+4+(8*4))+(2+4+4+4);
			
			int nVertices = getTempDataSize() + ((2+4+2)*nModel);
			
			int nMainSize = ( (2+4+4+4+4)+(2+4+6)+(2+4)+(2+4+2+(48*4))+(2+4+(4*12))+(2+4+2+(2*48))+(2+4+9)+(2+2*12) +(2+4+(2*countFace*2)))*nModel;
			
			int nTailer = (2+4)+(2+4+2+7+4)+(2+4+(2*2)+4)+(2+4+(2*2))+(2+4)+(2+4+2)+(2+4+7)+(2*3)+(2+4+2+2*4+2+2)+((2+4+(4*3))*1)+(2+4)+(2+4+2)+(2+4+7)+(2*3)+(2+4+2+2*4+2+2)+((2+4+(4*3))*1)+(2+4+2+2*4+2+2)+((2+4+4)*1)+(2+4+2+2*4+2+2)+((2+4+4)*1);
			if(nModel > 1) {
				nTailer += (2+4+6)+(2+4)+(2+4+(4*12));
			}
			nTailer += ( (2+4)+(2+4+2)+(2+4+6+2*3)+(2+4+(4*3))  ) * nModel;
			nTailer += ( (2+4+2+2*4+2+2)+((2+4+(4*3))*1) + (2+4+2+2*4+2+2)+((2+4+4+(4*3))*1) + (2+4+2+2*4+2+2)+((2+4+(4*3))*1) ) * nModel;
			
			int nSize = nHeader + nVertices + nMainSize + nTailer;
			
			// header 부분 시작
			writeHeaderDataChunk(nSize);
			// header 부분 끝 
			
			// temp file로부터 main data 처리 시작
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/_tempData_.txt";

			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = null;
			int nIdx, nXYZ, nPrism, nType;
			nIdx = nXYZ = nPrism = nType = 0;
			boolean readType = false;
			Vertex[] readData = new Vertex[48];
			
			boolean readAngle = false;
			int nAngleCount = 0;
			float[] angleData = new float[3];
			
			while((line = br.readLine()) != null) {
				if(line.matches(".*Name:.*")) {
					nPrism++;
					nIdx = nXYZ = 0;
				}
				else if(line.matches(".*Type:.*")) {
					// trigonal, square, pentagonal, hexagonal
					readType = true;
				}
				else if(line.matches(".*Angle:.*")) {
					readAngle = true;
				}
				else if(line.matches(".*OneEnd.*"))	{					
					writeMainDataChunk(readData, nPrism, nIdx, angleData, "Model"+Integer.toString(nPrism));
				}
				else if(line.matches(".*AllEnd.*"))	{
					writeTailerDataChunk(nPrism);
				}
				else {
					if(readType) {
						nType = Integer.parseInt(line);
						readType = false;
						
						switch(nType) {
						case 3:
							readData = new Vertex[6];
							break;
						case 4:
							readData = new Vertex[8];
							break;
						case 5:
							readData = new Vertex[10];
							break;
						case 6:
							readData = new Vertex[12];
							break;
						case 0:
							readData = new Vertex[48];
							break;
						}
						continue;
					}
					
					if(readAngle) {
						angleData[nAngleCount++] = Float.parseFloat(line);
						if(nAngleCount > 2) {
							nAngleCount = 0;
							readAngle = false;
						}
						continue;
					}
						
					if(nXYZ > 2)
						nXYZ = 0;
				
					if(nXYZ == 0)
						readData[nIdx] = new Vertex();
					
					switch(nXYZ) {
					case 0:
						readData[nIdx].setX(Float.parseFloat(line));
						break;
					case 1:
						readData[nIdx].setY(Float.parseFloat(line));
						break;
					case 2:
						readData[nIdx++].setZ(Float.parseFloat(line));
						break;
					}
					nXYZ++;
				}
			}
			br.close();	
			// temp data 처리 긑
			
			
			fOut.close();
		} 
		catch(Exception e) {
			System.out.println (e.toString());
		}
	}
	
	
	
	// 아래에 chunk별로 저장함수들
	// 특별한 것 빼고는 대부분 writeOneChunk...()
	public void writeOneChunkStrutMainDataC(int[] frame, int[] unknown, float data) { // ( (2+4+4)*key )
		try { // b023 FOV, b024 ROLL			
			for(int i=0; i<frame.length; i++) {
				byte[] bytes = intTobyteForShort(frame[i]);
				fOut.write(bytes, 0, 2);
				
				bytes = intTobyte(unknown[i], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes, 0, bytes.length);
				
				bytes = floatTobyte(data, ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes, 0, bytes.length);	
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunkStrutMainDataB(int[] frame, int[] unknown, float[] rot, float[] data) { // ( (2+4+4+(4*3))*key )
		try { // b021 ROT
			for(int i=0; i<frame.length; i++) {
				byte[] bytes = intTobyteForShort(frame[i]);
				fOut.write(bytes, 0, 2);
				
				bytes = intTobyte(unknown[i], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes, 0, bytes.length);
				
				bytes = floatTobyte(rot[i], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes, 0, bytes.length);	
				
				for(int j=0; j<3; j++) {
					bytes = floatTobyte(data[j], ByteOrder.LITTLE_ENDIAN);                
					fOut.write(bytes, 0, bytes.length);	
				}
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}

	public void writeOneChunkStrutMainDataA(int[] frame, int[] unknown, float[] data) { // ( (2+4+(4*3))*key )
		try { // b020 POS, b022 SCL
			for(int i=0; i<frame.length; i++) {
				byte[] bytes = intTobyteForShort(frame[i]);
				fOut.write(bytes, 0, 2);
				
				bytes = intTobyte(unknown[i], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes, 0, bytes.length);
				
				for(int j=0; j<3; j++) {
					bytes = floatTobyte(data[j], ByteOrder.LITTLE_ENDIAN);                
					fOut.write(bytes, 0, bytes.length);	
				}
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunkStrutHeader(int sHigh, int sLow, int nSize, int flag, int[] unknown1, int key, int unknown2) {
		try { // key=1, (2+4+ 2+2*4+2+2)
			// sHigh sLow 
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(sLow);
			byte1[1] = (byte)(sHigh);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
			
			bytes = intTobyteForShort(flag);
			fOut.write(bytes, 0, 2);
			
			for(int i=0; i<unknown1.length; i++) {
				bytes = intTobyteForShort(unknown1[i]);
				fOut.write(bytes, 0, 2);
			}
			
			bytes = intTobyteForShort(key);
			fOut.write(bytes, 0, 2);
			
			bytes = intTobyteForShort(unknown2);
			fOut.write(bytes, 0, 2);
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeMshMatGroup() { // (2+2*12)
		try {
			// 0x4130 : MSH_MAT_GROUP
			byte[] byteC = new byte[1];
			byteC[0] = (byte)(0x00);
			fOut.write(byteC, 0, 1);
			
			byte[] byte2 = intTobyteForShort(countFace);
			fOut.write(byte2, 0, 2);
			
			for(int i=0; i<countFace; i++) {
				byte2 = intTobyteForShort(i);
				fOut.write(byte2, 0, 2);
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeFaceDescriptionChunk() { // (2+4+2+(2*12*4))
		try {
			// 0x4120 : Faces Description (short n, short[3*n])
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(0x20);
			byte1[1] = (byte)(0x41);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes2 = intTobyte(199, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes2, 0, bytes2.length);
			
			byte1 = intTobyteForShort(12);
			fOut.write(byte1, 0, 2);
			
			int[] index = {0, 1, 2, 0, 
					1, 0, 3, 3, 
					4, 5, 6, 3,
					5, 4, 7, 3,
					8, 9, 10, 3,
					9, 8, 11, 3,
					12, 13, 14, 3,
					13, 12, 15, 3,
					16, 17, 18, 3,
					17, 16, 19, 3,
					20, 21, 22, 3,
					21, 20, 23, 3};
			for(int i=0; i<index.length; i++) {
				byte1 = intTobyteForShort(index[i]);
				fOut.write(byte1, 0, 2);
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeMappingCoordinatesListChunk() { // (2+4+2+(48*4)
		try {
			// 0x4140 : Mapping Coordinates List (short n, float[2*n])
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(0x40);
			byte1[1] = (byte)(0x41);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes2 = intTobyte(200, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes2, 0, bytes2.length);
			
			byte1 = intTobyteForShort(24);
			fOut.write(byte1, 0, 2);
			
			float[] tempData = { -31.806980f, 32.848213f,
					23.677273f, -34.163597f,
					23.677273f, 32.848213f,
					-31.806980f, -34.163597f,
					32.848213f, 0.000000f,
					-34.163597f, 25.220472f,
					-34.163597f, 0.000000f,
					32.848213f, 25.220472f,
					-31.806980f, 0.000000f,
					23.677273f, 25.220472f,
					-31.806980f, 25.220472f,
					23.677273f, 0.000000f,
					34.163597f, 0.000000f,
					-32.848213f, 25.220472f,
					-32.848213f, 0.000000f,
					34.163597f, 25.220472f,
					-23.677273f, 0.000000f,
					 31.806980f, 25.220472f,
					 -23.677273f, 25.220472f,
					 31.806980f, 0.000000f,
					 31.806980f, -34.163597f,
					 -23.677273f, 32.848213f,
					 -23.677273f, -34.163597f,
					 31.806980f, 32.848213f
			};
			
			for(int i=0; i<24; i++) {
				byte[] bytes3 = floatTobyte(tempData[i*2], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes3, 0, bytes3.length);						
				
				bytes3 = floatTobyte(tempData[i*2+1], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes3, 0, bytes3.length);
			}
			
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeVerticesListChunk(Vertex[] readData, int nSize) {
		try {
			// 0x4110 : Vertices List (short n, float[3*n])
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(0x10);
			byte1[1] = (byte)(0x41);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes2 = intTobyte(296, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes2, 0, bytes2.length);
			
			byte1 = intTobyteForShort(nSize*3);
			fOut.write(byte1, 0, 2);
			/*
			float[] tempData = {807.897278f, 834.344666f, 0f,
					-601.402710f, -867.755371f, 0f,
					-601.402710f, 834.344666f, 0f,
					807.897278f, -867.755371f, 0f,
					807.897278f, 834.344666f, 0f,
					807.897278f, -867.755371f, 640.599976f,
					807.897278f, -867.755371f, 0f,
					807.897278f, 834.344666f, 640.599976f,
					807.897278f, 834.344666f, 0f,
					-601.402710f, 834.344666f, 640.599976f,
					807.897278f, 834.344666f, 640.599976f,
					-601.402710f, 834.344666f, 0f,
					-601.402710f, -867.755371f, 0f,
					-601.402710f, 834.344666f, 640.599976f,
					-601.402710f, 834.344666f, 0f,
					-601.402710f, -867.755371f, 640.599976f,
					-601.402710f, -867.755371f, 0f,
					807.897278f, -867.755371f, 640.599976f,
					-601.402710f, -867.755371f, 640.599976f,
					807.897278f, -867.755371f, 0f,
					807.897278f, -867.755371f, 640.599976f,
					-601.402710f, 834.344666f, 640.599976f,
					-601.402710f, -867.755371f, 640.599976f,
					807.897278f, 834.344666f, 640.599976f};
			
			for(int i=0; i<nSize*3; i++) {
				byte[] bytes3 = floatTobyte(tempData[i*3], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes3, 0, bytes3.length);						
				
				bytes3 = floatTobyte(tempData[i*3+1], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes3, 0, bytes3.length);	
				
				bytes3 = floatTobyte(tempData[i*3+2], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes3, 0, bytes3.length);
			}
			 */
			
			for(int i=0; i<nSize; i++) {
				for(int j=0; j<3; j++) {
					byte[] bytes3 = floatTobyte(readData[i].getX(), ByteOrder.LITTLE_ENDIAN);                
					fOut.write(bytes3, 0, bytes3.length);						
					
					bytes3 = floatTobyte(readData[i].getY(), ByteOrder.LITTLE_ENDIAN);                
					fOut.write(bytes3, 0, bytes3.length);	
					
					bytes3 = floatTobyte(readData[i].getZ(), ByteOrder.LITTLE_ENDIAN);                
					fOut.write(bytes3, 0, bytes3.length);
				}
			}	
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}

	public void writeColorChunk(int sHigh, int sLow, int nSize, int nVal) { // (2+4+3)
		try {		
			// sHigh sLow 
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(sLow);
			byte1[1] = (byte)(sHigh);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
			
			byte[] byteC = new byte[1];
			byteC[0] = (byte)(nVal);
			fOut.write(byteC, 0, 1);
			byteC[0] = (byte)(nVal);
			fOut.write(byteC, 0, 1);
			byteC[0] = (byte)(nVal);
			fOut.write(byteC, 0, 1);
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunkName(int sHigh, int sLow, int nSize, int nVal, String sData) {
		// (2+4+2+7)
		try {		
			// sHigh sLow 
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(sLow);
			byte1[1] = (byte)(sHigh);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
						
			byte1 = intTobyteForShort(nVal);
			fOut.write(byte1, 0, 2);
			
			fOut.close();
			
			FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sData);
			bw.close();
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunkStr(int sHigh, int sLow, int nSize, String sData) { // (2+4+7?9?)
		try {		
			// sHigh sLow 
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(sLow);
			byte1[1] = (byte)(sHigh);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
			
			fOut.close();
			
			FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sData);
			bw.close();
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunkArr(int sHigh, int sLow, int nSize, int[] nVal) { // (2+4+(4*lengnth))
		try {		
			// sHigh sLow 
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(sLow);
			byte1[1] = (byte)(sHigh);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
			
			for(int i=0; i<nVal.length; i++) {
				bytes = intTobyte(nVal[i], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes, 0, bytes.length);
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunkFloatValArr(int sHigh, int sLow, int nSize, float[] nVal, boolean overWrite) { // (2+4+4)
		try {		
			// sHigh sLow 
			if(overWrite) {
				byte[] byte1 = new byte[3];
				byte1[0] = (byte)(0x00);
				byte1[1] = (byte)(sLow);
				byte1[2] = (byte)(sHigh);
				fOut.write(byte1, 0, 3);
			}
			else {
				byte[] byte1 = new byte[2];
				byte1[0] = (byte)(sLow);
				byte1[1] = (byte)(sHigh);
				fOut.write(byte1, 0, 2);
			}				
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
			
			for(int i=0; i<nVal.length; i++) {
				bytes = floatTobyte(nVal[i], ByteOrder.LITTLE_ENDIAN);                
				fOut.write(bytes, 0, bytes.length);
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunkShortVal(int sHigh, int sLow, int nSize, int[] nVals) { // (2+4+(2*n))
		try {		
			// sHigh sLow 
			byte[] byte1 = new byte[2];
			byte1[0] = (byte)(sLow);
			byte1[1] = (byte)(sHigh);
			fOut.write(byte1, 0, 2);
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
			
			for(int i=0; i<nVals.length; i++) {
				byte1 = intTobyteForShort(nVals[i]);
				fOut.write(byte1, 0, 2);
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeOneChunk(int sHigh, int sLow, int nSize, boolean overWrite) { // (2+4)
		try {		
			// sHigh sLow 
			if(overWrite) {
				byte[] byte1 = new byte[3];
				byte1[0] = (byte)(0x00);
				byte1[1] = (byte)(sLow);
				byte1[2] = (byte)(sHigh);
				fOut.write(byte1, 0, 3);
			}
			else {
				byte[] byte1 = new byte[2];
				byte1[0] = (byte)(sLow);
				byte1[1] = (byte)(sHigh);
				fOut.write(byte1, 0, 2);
			}
			
			byte[] bytes = intTobyte(nSize, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(bytes, 0, bytes.length);
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeIntData(int nVal, boolean overWrite) { // (4)
		try {		
			if(overWrite) {
				byte[] byteC = new byte[1];
				byteC[0] = (byte)(0x00);
				fOut.write(byteC, 0, 1);
			}

			byte[] byte1 = intTobyte(nVal, ByteOrder.LITTLE_ENDIAN);                
			fOut.write(byte1, 0, byte1.length);
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public void writeShortDataArr(int[] nVal, boolean overWrite) { // (2*n)
		try {		
			if(overWrite) {
				byte[] byteC = new byte[1];
				byteC[0] = (byte)(0x00);
				fOut.write(byteC, 0, 1);
			}
				
			for(int i=0; i<nVal.length; i++) {
				byte[] byte1 = intTobyteForShort(nVal[i]);
				byte1[0]=1;
				byte1[1]=1;
				fOut.write(byte1, 0, 2);
			}
		}
		catch (IOException e) {
			System.out.println (e.toString());
        }
	}
	
	public static byte[] floatTobyte(float value, ByteOrder order) {
		
		ByteBuffer buff = ByteBuffer.allocate(Float.SIZE/8);
		buff.order(order);
		
		buff.putFloat(value);
		
		return buff.array();
	}
	
	public static byte[] intTobyte(int integer, ByteOrder order) {
		 
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
		buff.order(order);
 
		buff.putInt(integer);
 
		return buff.array();
	}
	
	public static byte[] intTobyteForShort(int value) {
		byte[] byte1 = new byte[2];
		byte1[0] = (byte)(value);
		byte1[1] = (byte)(0x00);
		
		return byte1;
	}
	
	public int getTempDataSize() {
		int nTotal = 0;
		nModel = 0;
		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/_tempData_.txt";

		try {
			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = null;
			boolean readType = false;
			boolean readAngle = false;
			int nAngleCount = 0;
			
			while((line = br.readLine()) != null) {
				if(line.matches(".*Name:.*")) {
				}
				else if(line.matches(".*Type:.*")) {
					readType = true;
				}
				else if(line.matches(".*OneEnd.*"))	{
					nModel++;
				}
				else if(line.matches(".*AllEnd.*"))	{
	
				}
				else if(line.matches(".*Angle:.*")) {
					readAngle = true;
					nAngleCount = 0;
				}
				else {
					if(readType) {
						readType = false;
						continue;
					}
					if(readAngle) {
						nAngleCount++;
						if(nAngleCount > 2) {
							readAngle = false;
							nAngleCount = 0;
						}
						continue;
					}
					nTotal++;
				}
			}
			br.close();	
		}
		catch(Exception e) {
			System.out.println (e.toString());
		}
		
		return (nTotal*3*4);
	}

}

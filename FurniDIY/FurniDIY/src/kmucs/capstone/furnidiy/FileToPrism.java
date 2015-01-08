package kmucs.capstone.furnidiy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import android.os.Environment;

// yhchung
// open

// 임시파일 열어서 prism의 vertex로 저장

public class FileToPrism {
	EditActivity editActivity = new EditActivity();
	private int nType;
	private float fDivideUnit = 250.0f; // 3ds와 furniDIY간 단위를 맞춰야
	
	public FileToPrism(EditActivity editActivity) {
		this.editActivity = editActivity;
	}
	
	public void procDataTrigonal(Vertex[] readData, float[] minXYZ, float[] maxXYZ, Vertex[] vPos) {
		
		for(int i=0; i<nType*2; i++) {

			// 삼각 기둥 (x,y,z)			
			// 1
			//      0
			// 2			
			
			// 0: half Max min
			// 1: min min min
			// 2: Max min min
			
			// 3: half Max Max
			// 4: min min max
			// 5: Max min Max
			
			if(readData[i].getZ() == minXYZ[2]) { // 0,1,2
				if(readData[i].getY() == maxXYZ[1]) { // 0
					vPos[0].setX(readData[i].getX() / fDivideUnit);
					vPos[0].setY(readData[i].getY() / fDivideUnit);
					vPos[0].setZ(readData[i].getZ() / fDivideUnit);
				}
				else { // 1,2
					if(readData[i].getX() == minXYZ[0]) { // 1
						vPos[1].setX(readData[i].getX() / fDivideUnit);
						vPos[1].setY(readData[i].getY() / fDivideUnit);
						vPos[1].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 2
						vPos[2].setX(readData[i].getX() / fDivideUnit);
						vPos[2].setY(readData[i].getY() / fDivideUnit);
						vPos[2].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
			else { // 3,4,5
				if(readData[i].getY() == maxXYZ[1]) { // 3
					vPos[3].setX(readData[i].getX() / fDivideUnit);
					vPos[3].setY(readData[i].getY() / fDivideUnit);
					vPos[3].setZ(readData[i].getZ() / fDivideUnit);
				}
				else { // 4,5
					if(readData[i].getX() == minXYZ[0]) { // 4
						vPos[4].setX(readData[i].getX() / fDivideUnit);
						vPos[4].setY(readData[i].getY() / fDivideUnit);
						vPos[4].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 5
						vPos[5].setX(readData[i].getX() / fDivideUnit);
						vPos[5].setY(readData[i].getY() / fDivideUnit);
						vPos[5].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
		}		
	}
	
	public void procDataSquare(Vertex[] readData, float[] minXYZ, float[] maxXYZ, Vertex[] vPos) {
			
		for(int i=0; i<nType*2; i++) {

			// 육면체 (x,y,z)
			// 0: min, min, min			
			// 1: Max, min, min
			// 2: Max, Max, min			
			// 3: min, Max, min
			
			// 4: min, min, Max			
			// 5: Max, min, Max
			// 6: Max, Max, Max			
			// 7: min, Max, Max
			
			if( Math.abs(readData[i].getZ() - minXYZ[2]) < Math.abs(readData[i].getZ() - maxXYZ[2]) ) { // 0,1,2,3
				if( Math.abs(readData[i].getY() - minXYZ[1]) < Math.abs(readData[i].getY() - maxXYZ[1]) ) { // 0,1
					if( Math.abs(readData[i].getX() - minXYZ[0]) < Math.abs(readData[i].getX() - maxXYZ[0]) ) { // 0
						vPos[0].setX(readData[i].getX() / fDivideUnit);
						vPos[0].setY(readData[i].getY() / fDivideUnit);
						vPos[0].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 1
						vPos[1].setX(readData[i].getX() / fDivideUnit);
						vPos[1].setY(readData[i].getY() / fDivideUnit);
						vPos[1].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else { // 2,3
					if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - minXYZ[0]) ) { // 2
						vPos[2].setX(readData[i].getX() / fDivideUnit);
						vPos[2].setY(readData[i].getY() / fDivideUnit);
						vPos[2].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 3
						vPos[3].setX(readData[i].getX() / fDivideUnit);
						vPos[3].setY(readData[i].getY() / fDivideUnit);
						vPos[3].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
			else { // 4,5,6,7
				if( Math.abs(readData[i].getY() - minXYZ[1]) < Math.abs(readData[i].getY() - maxXYZ[1]) ) { // 4,5
					if( Math.abs(readData[i].getX() - minXYZ[0]) < Math.abs(readData[i].getX() - maxXYZ[0]) ) { // 4
						vPos[4].setX(readData[i].getX() / fDivideUnit);
						vPos[4].setY(readData[i].getY() / fDivideUnit);
						vPos[4].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 5
						vPos[5].setX(readData[i].getX() / fDivideUnit);
						vPos[5].setY(readData[i].getY() / fDivideUnit);
						vPos[5].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else { // 6,7
					if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - minXYZ[0]) ) { // 6
						vPos[6].setX(readData[i].getX() / fDivideUnit);
						vPos[6].setY(readData[i].getY() / fDivideUnit);
						vPos[6].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 7
						vPos[7].setX(readData[i].getX() / fDivideUnit);
						vPos[7].setY(readData[i].getY() / fDivideUnit);
						vPos[7].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
		}		
	}
	
	public void procDataPentagonal(Vertex[] readData, float[] minXYZ, float[] maxXYZ, Vertex[] vPos) {
		
		float halfX = (minXYZ[0] + maxXYZ[0]) / 2.0f;
		float halfY = (minXYZ[1] + maxXYZ[1]) / 2.0f;
		
		for(int i=0; i<nType*2; i++) {

			// 오각 기둥 (x,y,z)
			//     3
			// 4
			//         2
			// 0
			//     1
			
			// 0: half+, min, min
			// 1: Max, half, min
			// 2: half, Max, min
			// 3: Max, half, min
			// 4: half-, min, min
			
			// 5: half+, min, Max
			// 6: Max, half, max
			// 7: half, Max, Max
			// 8: Max, half, Max
			// 9: half-, min, Max
			
			if( Math.abs(readData[i].getZ() - minXYZ[2]) < Math.abs(readData[i].getZ() - maxXYZ[2]) ) { // 0,1,2,3,4
				if( Math.abs(readData[i].getY() - minXYZ[1]) < Math.abs(readData[i].getY() - halfY) ) { // 0, 4
					if( readData[i].getX() >= halfX  ) { // 0
						vPos[0].setX(readData[i].getX() / fDivideUnit);
						vPos[0].setY(readData[i].getY() / fDivideUnit);
						vPos[0].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 4
						vPos[4].setX(readData[i].getX() / fDivideUnit);
						vPos[4].setY(readData[i].getY() / fDivideUnit);
						vPos[4].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else if( Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - minXYZ[0]) &&
						Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - maxXYZ[0]) ) { // 2
					vPos[2].setX(readData[i].getX() / fDivideUnit);
					vPos[2].setY(readData[i].getY() / fDivideUnit);
					vPos[2].setZ(readData[i].getZ() / fDivideUnit);
				}
				else {
					if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - halfX) ) { // 1
						vPos[1].setX(readData[i].getX() / fDivideUnit);
						vPos[1].setY(readData[i].getY() / fDivideUnit);
						vPos[1].setZ(readData[i].getZ() / fDivideUnit);
					}
					else if( Math.abs(readData[i].getX() - minXYZ[0]) < Math.abs(readData[i].getX() - halfX) ) { // 3
						vPos[3].setX(readData[i].getX() / fDivideUnit);
						vPos[3].setY(readData[i].getY() / fDivideUnit);
						vPos[3].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
			else { // 5,6,7,8,9
				if( Math.abs(readData[i].getY() - minXYZ[1]) < Math.abs(readData[i].getY() - halfY) ) { // 5, 9
					if( readData[i].getX() >= halfX  ) { // 5
						vPos[5].setX(readData[i].getX() / fDivideUnit);
						vPos[5].setY(readData[i].getY() / fDivideUnit);
						vPos[5].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 9
						vPos[9].setX(readData[i].getX() / fDivideUnit);
						vPos[9].setY(readData[i].getY() / fDivideUnit);
						vPos[9].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else if( Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - minXYZ[0]) &&
						Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - maxXYZ[0]) ) { // 7
					vPos[7].setX(readData[i].getX() / fDivideUnit);
					vPos[7].setY(readData[i].getY() / fDivideUnit);
					vPos[7].setZ(readData[i].getZ() / fDivideUnit);
				}
				else {
					if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - halfX) ) { // 6
						vPos[6].setX(readData[i].getX() / fDivideUnit);
						vPos[6].setY(readData[i].getY() / fDivideUnit);
						vPos[6].setZ(readData[i].getZ() / fDivideUnit);
					}
					else if( Math.abs(readData[i].getX() - minXYZ[0]) < Math.abs(readData[i].getX() - halfX) ) { // 8
						vPos[8].setX(readData[i].getX() / fDivideUnit);
						vPos[8].setY(readData[i].getY() / fDivideUnit);
						vPos[8].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
		}
	}
	
	public void procDataHexagonalVertical(Vertex[] readData, float[] minXYZ, float[] maxXYZ, Vertex[] vPos) {
		float halfX = (minXYZ[0] + maxXYZ[0]) / 2.0f;
		float halfY = (minXYZ[1] + maxXYZ[1]) / 2.0f;
		
		for(int i=0; i<nType*2; i++) {

			// 육각 기둥 (x,y,z)
			//      4
			// 3         5
			//         
			// 2         0
			//      1
			
			// 0: half+, Max, min
			// 1: Max, half, min
			// 2: half+, min, min
			// 3: half-, min, min
			// 4: min, half, min
			// 5: half-, Max, min
			
			// 6: half+, Max, Max
			// 7: Max, half, Max
			// 8: half+, min, Max
			// 9: half-, min, Max
			// 10: min, half, Max
			// 11: half-, Max, Max
			
			if( Math.abs(readData[i].getZ() - minXYZ[2]) < Math.abs(readData[i].getZ() - maxXYZ[2]) ) { // 0,1,2,3,4,5
				if( Math.abs(readData[i].getY() - maxXYZ[1]) < Math.abs(readData[i].getY() - halfY) ) { // 0, 5
					if( readData[i].getX() >= halfX) { // 0
						vPos[0].setX(readData[i].getX() / fDivideUnit);
						vPos[0].setY(readData[i].getY() / fDivideUnit);
						vPos[0].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 5
						vPos[5].setX(readData[i].getX() / fDivideUnit);
						vPos[5].setY(readData[i].getY() / fDivideUnit);
						vPos[5].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else if( readData[i].getX() == minXYZ[0] || readData[i].getX() == maxXYZ[0] ||
						(Math.abs(readData[i].getY() - halfY) < Math.abs(readData[i].getY() - minXYZ[1]) &&	Math.abs(readData[i].getY() - halfY) < Math.abs(readData[i].getY() - maxXYZ[1])) 
						) { // 1, 4				
					if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - minXYZ[0]) ) { // 1
						vPos[1].setX(readData[i].getX() / fDivideUnit);
						vPos[1].setY(readData[i].getY() / fDivideUnit);
						vPos[1].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 4
						vPos[4].setX(readData[i].getX() / fDivideUnit);
						vPos[4].setY(readData[i].getY() / fDivideUnit);
						vPos[4].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else { // 2, 3
					if(readData[i].getX() >= halfX) { // 2
						vPos[2].setX(readData[i].getX() / fDivideUnit);
						vPos[2].setY(readData[i].getY() / fDivideUnit);
						vPos[2].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 3
						vPos[3].setX(readData[i].getX() / fDivideUnit);
						vPos[3].setY(readData[i].getY() / fDivideUnit);
						vPos[3].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				
			}
			else { // 6,7,8,9,10,11
				if( Math.abs(readData[i].getY() - maxXYZ[1]) < Math.abs(readData[i].getY() - halfY) ) { // 6, 11
					if(readData[i].getX() >= halfX) { // 6
						vPos[6].setX(readData[i].getX() / fDivideUnit);
						vPos[6].setY(readData[i].getY() / fDivideUnit);
						vPos[6].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 11
						vPos[11].setX(readData[i].getX() / fDivideUnit);
						vPos[11].setY(readData[i].getY() / fDivideUnit);
						vPos[11].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else if( readData[i].getX() == minXYZ[0] || readData[i].getX() == maxXYZ[0] ||
						(Math.abs(readData[i].getY() - halfY) < Math.abs(readData[i].getY() - minXYZ[1]) &&	Math.abs(readData[i].getY() - halfY) < Math.abs(readData[i].getY() - maxXYZ[1]))
						) { // 7, 10		
					if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - minXYZ[0]) ) { // 7
						vPos[7].setX(readData[i].getX() / fDivideUnit);
						vPos[7].setY(readData[i].getY() / fDivideUnit);
						vPos[7].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 10
						vPos[10].setX(readData[i].getX() / fDivideUnit);
						vPos[10].setY(readData[i].getY() / fDivideUnit);
						vPos[10].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else { // 8, 9
					if(readData[i].getX() >= halfX) { // 8
						vPos[8].setX(readData[i].getX() / fDivideUnit);
						vPos[8].setY(readData[i].getY() / fDivideUnit);
						vPos[8].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 9
						vPos[9].setX(readData[i].getX() / fDivideUnit);
						vPos[9].setY(readData[i].getY() / fDivideUnit);
						vPos[9].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
		}
	}
	
	public void procDataHexagonalHorizontal(Vertex[] readData, float[] minXYZ, float[] maxXYZ, Vertex[] vPos) {
		float halfX = (minXYZ[0] + maxXYZ[0]) / 2.0f;
		float halfY = (minXYZ[1] + maxXYZ[1]) / 2.0f;
		
		for(int i=0; i<nType*2; i++) {

			// 육각 기둥 (x,y,z)
			//    3    4
			// 
			// 2          5
			//
			//    1    0
			
			// 0: Max, half+, min
			// 1: Max, half-, min
			// 2: half, min, min
			// 3: min, half-, min
			// 4: min, half+, min
			// 5: half, Max, min
			
			// 6: Max, half+, Max
			// 7: Max, half-, Max
			// 8: half, min, Max
			// 9: min, half-, Max
			// 10: min, half+, Max
			// 11: half, Max, Max
			
			if( Math.abs(readData[i].getZ() - minXYZ[2]) < Math.abs(readData[i].getZ() - maxXYZ[2]) ) { // 0,1,2,3,4,5
				if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - halfX) ) { // 0, 1
					if( readData[i].getY() >= halfY) { // 0
						vPos[0].setX(readData[i].getX() / fDivideUnit);
						vPos[0].setY(readData[i].getY() / fDivideUnit);
						vPos[0].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 1
						vPos[1].setX(readData[i].getX() / fDivideUnit);
						vPos[1].setY(readData[i].getY() / fDivideUnit);
						vPos[1].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else if( readData[i].getY() == minXYZ[1] || readData[i].getY() == maxXYZ[1] ||
						(Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - minXYZ[0]) &&	Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - maxXYZ[0])) 
						) { // 2, 5		
					if( Math.abs(readData[i].getY() - minXYZ[1]) < Math.abs(readData[i].getY() - maxXYZ[1]) ) { // 2
						vPos[2].setX(readData[i].getX() / fDivideUnit);
						vPos[2].setY(readData[i].getY() / fDivideUnit);
						vPos[2].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 5
						vPos[5].setX(readData[i].getX() / fDivideUnit);
						vPos[5].setY(readData[i].getY() / fDivideUnit);
						vPos[5].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else { // 3, 4
					if(readData[i].getY() < halfY) { // 3
						vPos[3].setX(readData[i].getX() / fDivideUnit);
						vPos[3].setY(readData[i].getY() / fDivideUnit);
						vPos[3].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 4
						vPos[4].setX(readData[i].getX() / fDivideUnit);
						vPos[4].setY(readData[i].getY() / fDivideUnit);
						vPos[4].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				
			}
			else { // 6,7,8,9,10,11
				if( Math.abs(readData[i].getX() - maxXYZ[0]) < Math.abs(readData[i].getX() - halfX) ) { // 6, 7
					if( readData[i].getY() >= halfY) { // 6
						vPos[6].setX(readData[i].getX() / fDivideUnit);
						vPos[6].setY(readData[i].getY() / fDivideUnit);
						vPos[6].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 7
						vPos[7].setX(readData[i].getX() / fDivideUnit);
						vPos[7].setY(readData[i].getY() / fDivideUnit);
						vPos[7].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else if( readData[i].getY() == minXYZ[1] || readData[i].getY() == maxXYZ[1] ||
						(Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - minXYZ[0]) &&	Math.abs(readData[i].getX() - halfX) < Math.abs(readData[i].getX() - maxXYZ[0])) 
						) { // 8, 11	
					if( Math.abs(readData[i].getY() - minXYZ[1]) < Math.abs(readData[i].getY() - maxXYZ[1]) ) { // 8
						vPos[8].setX(readData[i].getX() / fDivideUnit);
						vPos[8].setY(readData[i].getY() / fDivideUnit);
						vPos[8].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 11
						vPos[11].setX(readData[i].getX() / fDivideUnit);
						vPos[11].setY(readData[i].getY() / fDivideUnit);
						vPos[11].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
				else { // 9, 10
					if(readData[i].getY() < halfY) { // 9
						vPos[9].setX(readData[i].getX() / fDivideUnit);
						vPos[9].setY(readData[i].getY() / fDivideUnit);
						vPos[9].setZ(readData[i].getZ() / fDivideUnit);
					}
					else { // 10
						vPos[10].setX(readData[i].getX() / fDivideUnit);
						vPos[10].setY(readData[i].getY() / fDivideUnit);
						vPos[10].setZ(readData[i].getZ() / fDivideUnit);
					}
				}
			}
		}
	}
	
	public void procDataHexagonal(Vertex[] readData, float[] minXYZ, float[] maxXYZ, Vertex[] vPos) {
		int nMaxX, nMinX, nMaxY, nMinY;
		nMaxX = nMinX = nMaxY = nMinY = 0;
		
		for(int i=0; i<readData.length; i++)
		{
			if(readData[i].getX() == minXYZ[0]) nMinX++;
			else if(readData[i].getX() == maxXYZ[0]) nMaxX++;
			
			if(readData[i].getY() == minXYZ[1]) nMinY++;
			else if(readData[i].getY() == maxXYZ[1]) nMaxY++;
		}
		
		if(nMinX == 4 && nMaxX == 4 && nMinY == 2 && nMaxY == 2) {
			/*
			     3  4
			    2    5
			     1  0
			 */
			procDataHexagonalHorizontal(readData, minXYZ, maxXYZ, vPos);
		}
		else {
			/*
		       		4
		    	3       5
		    	2       0
		       		1
			 */
			procDataHexagonalVertical(readData, minXYZ, maxXYZ, vPos);
		}
		
	}
	
	public void procDataCircular(Vertex[] readData, float[] minXYZ, float[] maxXYZ, Vertex[] vPos) {
		
	}
	
	public void procData(Vertex[] readData, float[] angleData) {
		
		/*
		 * [axis]
		 *        z
		 *        z
		 *        z
		 *        z
		 *        z
		 *        y y y y y
		 *       x
		 *     x
		 *   x 
		 */
		
		Vertex[] vPos = new Vertex[nType*2];
		for(int i=0; i<vPos.length; i++) {
			vPos[i] = new Vertex();
		}
		
		float[] minXYZ, maxXYZ;
		minXYZ = new float[3]; // x,y,z
		maxXYZ = new float[3]; // x,y,z
		//minXYZ[0] = minXYZ[1] = minXYZ[2] = Float.MAX_VALUE;
		//maxXYZ[0] = maxXYZ[1] = maxXYZ[2] = Float.MIN_VALUE;
		minXYZ[0] = maxXYZ[0] = readData[0].getX();
		minXYZ[1] = maxXYZ[1] = readData[0].getY();
		minXYZ[2] = maxXYZ[2] = readData[0].getZ();
		
		for(int i=0; i<nType*2; i++) {
			if(readData[i].getX() < minXYZ[0]) minXYZ[0] = readData[i].getX();
			if(readData[i].getX() > maxXYZ[0]) maxXYZ[0] = readData[i].getX();
			
			if(readData[i].getY() < minXYZ[1]) minXYZ[1] = readData[i].getY();
			if(readData[i].getY() > maxXYZ[1]) maxXYZ[1] = readData[i].getY();
			
			if(readData[i].getZ() < minXYZ[2]) minXYZ[2] = readData[i].getZ();
			if(readData[i].getZ() > maxXYZ[2]) maxXYZ[2] = readData[i].getZ();
		}
		
		switch(nType) {
		case 3:
			procDataTrigonal(readData, minXYZ, maxXYZ, vPos);
			break;
		case 4:
			procDataSquare(readData, minXYZ, maxXYZ, vPos);
			break;
		case 5:
			procDataPentagonal(readData, minXYZ, maxXYZ, vPos);
			break;
		case 6:
			procDataHexagonal(readData, minXYZ, maxXYZ, vPos);
			break;
		case 0:
			procDataCircular(readData, minXYZ, maxXYZ, vPos);
			break;
		}
		
		editActivity.setData(3, nType, true, vPos, angleData);
	}
	
	public void dataProcess() {
		
		// file open activity에서 edit activity로 data 전달 방법을 아직 못찾아서 우선 임시 파일로 처리
		// --> 찾아보니 전달할 object를 직렬화 하여 activity간 전달 가능한데, 우선 나중에 수정하는 걸로 (다른 게 더 급함)
		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FurniDIY/_tempData_.txt";
		try {
			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = null;
			int nIdx = 0;
			int nXYZ = 0;
			boolean readType = false;
			Vertex[] readData = new Vertex[48];
			int nPrism = 0;
			
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
					nAngleCount = 0;
				}
				else if(line.matches(".*OneEnd.*") || line.matches(".*,.*"))   {
					procData(readData, angleData);
				}
				else if(line.matches(".*AllEnd.*"))	{
					int temp = (int)(Math.random() * 1000.0);
					for(int i=0; i<nPrism; i++) {
						editActivity.setTextureIndex(i, temp%9);
					}
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
							readAngle = false;
							nAngleCount = 0;
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
		}
		catch (IOException e) {
			System.err.println(e);
            System.exit(1);
		}
	}
}

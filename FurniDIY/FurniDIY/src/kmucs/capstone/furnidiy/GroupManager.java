package kmucs.capstone.furnidiy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.util.Log;

public class GroupManager {

	private ArrayList<HashSet<Integer>> groupList;
	private HashSet<Integer> newGroup = new HashSet<Integer>();
	private EditRenderer renderer;
	private EditActivity activity;

	public GroupManager(EditRenderer editRenderer, EditActivity activity) {
		// TODO Auto-generated constructor stub
		groupList = new ArrayList<HashSet<Integer>>();
		this.renderer = editRenderer;
		this.activity = activity;
	}

	public void groupAdd(HashSet<Integer> newGroup) {

		this.newGroup = newGroup;
		groupList.add(newGroup);
	}

	public void groupDelete(int groupID) {

		// 그룹을 삭제할떄는 선택되었던 도형들을 모두 검은색으로 바꾸고, 제거한다.
		Iterator<Integer> iterator = groupList.get(groupID).iterator();
		while (iterator.hasNext()) {
			int temp = iterator.next();
			Log.d("temp", "temp is " + temp);
			renderer.prismList.get(temp).selectPrism(false);
			renderer.prismList.get(temp).changeLineColor();
		}

		groupList.remove(groupID); //groupID를 삭제하고 groupID를 추가하는데 빈것을 추가
		HashSet<Integer> temp = new HashSet<Integer>();
		groupList.add(groupID,temp);
		
		activity.isExplorerMode = true;
		activity.setMode();

	}

	public ArrayList<HashSet<Integer>> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<HashSet<Integer>> groupList) {
		this.groupList = groupList;
	}

	public void showGroup() {
		for (int i = 0; i < groupList.size(); i++) {
			Log.d("grouplist", "grouplist " + i + "번쨰 ");

			Iterator<Integer> iterator = groupList.get(i).iterator();
			while (iterator.hasNext()) {
				int number = iterator.next();
				Log.d("hashSet", "hashSet is " + number);
			}
		}
	}

	public boolean contains(int prismID) {
		for (int i = 0; i < groupList.size(); i++) {
			if (groupList.get(i).contains((Integer) prismID)) {
				return true;
			}
		}
		return false;
	}

	public int getGroupListID(int prismID) {
		if (this.contains(prismID)) {
			for (int i = 0; i < groupList.size(); i++) {
				if (groupList.get(i).contains((Integer) prismID)) {
					return i;
				}
			}
		}
		
		return -1;//-1이 반환되면 포함되지 않은것.

	}
	public void clear(int groupID)
	{
		
	}
}

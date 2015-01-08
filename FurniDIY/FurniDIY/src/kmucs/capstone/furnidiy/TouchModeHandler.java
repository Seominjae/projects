package kmucs.capstone.furnidiy;

import java.util.Iterator;

public class TouchModeHandler {
	
	private EditRenderer renderer;
	private EditActivity activity;
	private TouchHandler tHandler;
	
	public TouchModeHandler(EditRenderer renderer, EditActivity activity, TouchHandler tHandler) {
		// TODO Auto-generated constructor stub
		this.renderer=renderer;
		this.activity=activity;
		this.tHandler=tHandler;
	}
	public void addMode(float x, float y, int prismSize)
	{
		renderer.onTouch(x, y);
		renderer.makePrism(prismSize,x,y);
		renderer.prismList.get(renderer.prismList.size() - 1).selectPrism(true);
		renderer.prismList.get(renderer.prismList.size() - 1).changeLineColor();
		activity.setSelectedPrism(renderer.prismList.size() - 1);
		activity.isGroupMode = false;
		tHandler.checkSelected=true;
		tHandler.alreadyGrouped=false;
		tHandler.setMode(tHandler.getModeDefault());
		
		switch(prismSize) {
		case 3:
			EditActivity.print("삼각기둥이 추가되었습니다.");
			break;
		case 4:
			EditActivity.print("사각기둥이 추가되었습니다.");
			break;
		case 5:
			EditActivity.print("오각기둥이 추가되었습니다.");
			break;
		case 6:
			EditActivity.print("육각기둥이 추가되었습니다.");
			break;
		case 20:
			EditActivity.print("원기둥이 추가되었습니다.");
			break;
		}
	}
	public void viewMove(float x, float y)
	{
		// 탐색모드일때
		if (renderer.prismList.size() != 0) {
			float[] result = new float[3];
			result = renderer.convert2dTo3d(x, y);

			if (renderer.prismList.get(renderer.prismList.size() - 1).getSelectPrism() == true) {
				// 최종적으로 만들어진게 빨간색인 상태에서
				if (tHandler.pickedPrismID(result) < 0)// 바깥을 선택했다면
				{
					renderer.prismList.get(renderer.prismList.size() - 1).selectPrism(false);
					renderer.prismList.get(renderer.prismList.size() - 1).changeLineColor();
				}
			}
			if (activity.isGroupMode == false)// 그룹모드가 아닐때
				groupModeFalse(result);
			else// 그룹화모드이면
				groupModeTrue(result);
		}
		// flush event
		tHandler.start_pos = null;
		tHandler.prev_pos = null;
		tHandler.curr_pos = null;

	}
	public void groupModeTrue(float[] result)
	{

		// 도형을 선택했을때 선택한 도형의 ID를 계속 넘겨주다가 완료를 누르는 순간, 넘겨쪗던 도형
		// ID들을 저장한다.
		// arrayList와 hashset을 이용한다.
		if (tHandler.groupPickedPrismID(result) >= 0) { // 도형이 선택되었다면, 그룹에
			int selectedPrism = tHandler.groupPickedPrismID(result);
			boolean check = false;
			//그룹화모드에서 다시 자기자신을 클릭하면 해제가능하게 만든다.
			if(tHandler.newGroup.contains(selectedPrism))
			{
				activity.isExplorerMode = false;
				activity.setMode();
				renderer.prismList.get(selectedPrism).selectPrism(false);
				renderer.prismList.get(selectedPrism).changeLineColor();
			}
			//만약에 이미 그룹화된 도형이라면, 다른 그룹으로 만들어지지 않게 만들기 위해
			if(renderer.groupManager.contains(selectedPrism))
				check = true;
			
			//그룹화가 이루어지지 않은 도형이면 여기서 추가한다.
			if (!check){
				selectedPrism=tHandler.pickedPrismID(result);
				tHandler.newGroup.add(selectedPrism);
			}
		}
	}
	public void groupModeFalse(float[] result)
	{
		if (tHandler.checkSelected == false)// 도형이 선택되지 않은 상태에서
		{
			prismNotSelected(result);
		} else {// 도형이 선택되었다면, 2가지로 나눠볼 수 있다
			prismSelected(result);
		}
	}
	public void prismNotSelected(float[] result)
	{

		// 3)경우, 도형을 선택한다면 도형의 id를 넘기면 된다.
		int pickID = tHandler.pickedPrismID(result);
		if (pickID < 0)// 도형이 선택 되지 않으면 -1을 반납한다. 0보다 작으면선택이 되지 않은것이다.
			outSideClicked();
		else // 선택을 한다면
			inSideClicked(pickID);
	}
	public void prismSelected(float[] result) {
		boolean selected = false; // 일단 임시로 다음에 선택한것이 도형인지 외부를 선택했는지 확인하기 위한 임시 변수 selected
		int selectedPrism = -1;
		for (int i = 0; i < renderer.prismList.size(); i++) {
			if (renderer.prismList.get(i).checkPick(result))
			{
				selected = true;
				selectedPrism = i;
				break;
			}
		}
		// 여기서 selected가 true면 다시 도형을 선택했다는 이야기, false면 바깥을 선택했다는 이야기
		if (selected) // 1)경우, 이전의 선택했던 도형이 아닌 다른 도형을 선택했다면, 이전의 선택한 도형의 빨간선을 없애고, 지금 선택한 도형을 빨갛게 변경시키며 지금 선택한 도형의 id값을 넘겨준다
			prismToprism(selectedPrism);
		else// 2)경우, 다른 도형이 아니라 바깥 어딘가를 선택했다면 원래 선택한 도형을검은색으로 바꾼다.
			prismToOutside();
	}
	public void outSideClicked()
	{
		activity.isExplorerMode = true;
		activity.setMode();
		tHandler.checkSelected = false;
		for (int i = 0; i < renderer.prismList.size(); i++) {
			renderer.prismList.get(i).selectPrism(false);
			renderer.prismList.get(i).changeLineColor();
		}
	}
	public void inSideClicked(int pickID)
	{
		// 근데 이 도형의 아이디가 groupmanager에서 포함하고 있는 ID이면 그
		// 그룹 전체를 빨간색으로 바꾼다.
		// renderer.groupManager.showGroup();
		tHandler.alreadyGrouped = false;
		tHandler.groupListID = -1;
		// 선택한 도형이 이미 그룹으로 지정된 도형인지 확인한 후에
		if(renderer.groupManager.getGroupListID(pickID)>=0)
		{
			tHandler.alreadyGrouped = true;
			tHandler.groupListID = renderer.groupManager.getGroupListID(pickID);
		}
		// 이미 그룹화된 도형으로 확인이 된다면, 같은 그룹의 모든 도형의 Id를 설정한다.
		if (tHandler.alreadyGrouped) {
			Iterator<Integer> iterator = renderer.groupManager.getGroupList().get(tHandler.groupListID).iterator();
			while (iterator.hasNext()) {
				int id = iterator.next();
				renderer.prismList.get(id).selectPrism(true);
				renderer.prismList.get(id).changeLineColor();
				activity.setSelectIndex(renderer.groupManager.getGroupList().get(tHandler.groupListID));
			}
		}
		// ///////////////////////////////////////////////////////////////////////////

	}
	private void prismToprism(int selectedPrism) {
		// TODO Auto-generated method stub
		for (int i = 0; i < renderer.prismList.size(); i++) {
			renderer.prismList.get(i).selectPrism(false);
			renderer.prismList.get(i).changeLineColor();
		}
		// 이전 도형을 검은색으로 바꾸고
		activity.isExplorerMode = false;
		activity.setMode();
		tHandler.checkSelected = true;
		boolean selectedPrismAlreadyGrouped = false;
		// 선택된 도형이 그룹으로 지정된 arrayList에 있으면 hashSet안에 있는
		// 모든 도형들을 빨갛게 변경시키고, 그 도형들을 한꺼번에 이동,회전 등이 가능하게
		// 해야함.
		for (int i = 0; i < renderer.groupManager.getGroupList().size(); i++) {
			if (renderer.groupManager.getGroupList().get(i).contains(selectedPrism)) {
				activity.setSelectIndex(renderer.groupManager.getGroupList().get(i));
				selectedPrismAlreadyGrouped = true;
				tHandler.groupListID = i;
				break;
			}
		}
		if (selectedPrismAlreadyGrouped)// 도형을 선택했을떄 이미그룹된 도형을 선택했으면
			groupedPrismSelected();
		else// 도형을 선택했을때 그룹안된 도형을 선택했었다면, 한개의 도형만 바꾼다.
			notGroupedPrismSelected(selectedPrism);
	}
	private void notGroupedPrismSelected(int selectedPrism) {
		// TODO Auto-generated method stub
		tHandler.alreadyGrouped = false;
		tHandler.groupListID = -1;
		renderer.prismList.get(selectedPrism).selectPrism(true);
		renderer.prismList.get(selectedPrism).changeLineColor();
		// 지금선택한 도형을 빨갛게 변경시키며
		activity.setSelectedPrism(selectedPrism);
		// 지금 선택한 도형의 id값을 넘겨준다.
		
	}
	private void groupedPrismSelected() {
		// TODO Auto-generated method stub

		Iterator<Integer> iterator = activity.getSelectIndex()
				.iterator();
		// activity.getSelectIndex().clear();
		while (iterator.hasNext()) {
			int temp = iterator.next();
			renderer.prismList.get(temp).selectPrism(true);
			renderer.prismList.get(temp).changeLineColor();
			activity.setSelectIndex(activity.getSelectIndex());
			tHandler.alreadyGrouped = true;
		}
		
	}
	private void prismToOutside() {
		// TODO Auto-generated method stub
		if (!activity.isExplorerMode) {
			for (int i = 0; i < renderer.prismList.size(); i++) {
				renderer.prismList.get(i).selectPrism(false);
				renderer.prismList.get(i).changeLineColor();
			}
			// 원래 도형을 검은색으로 바꾸고
			// 선택된 도형 hashset초기화
			// activity.getSelectIndex().clear();
			// activity.setSelectedPrism(-1);
			activity.isExplorerMode = true;
			activity.setMode();
			tHandler.checkSelected = false;
		}
	}
}

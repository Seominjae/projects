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
			EditActivity.print("�ﰢ����� �߰��Ǿ����ϴ�.");
			break;
		case 4:
			EditActivity.print("�簢����� �߰��Ǿ����ϴ�.");
			break;
		case 5:
			EditActivity.print("��������� �߰��Ǿ����ϴ�.");
			break;
		case 6:
			EditActivity.print("��������� �߰��Ǿ����ϴ�.");
			break;
		case 20:
			EditActivity.print("������� �߰��Ǿ����ϴ�.");
			break;
		}
	}
	public void viewMove(float x, float y)
	{
		// Ž������϶�
		if (renderer.prismList.size() != 0) {
			float[] result = new float[3];
			result = renderer.convert2dTo3d(x, y);

			if (renderer.prismList.get(renderer.prismList.size() - 1).getSelectPrism() == true) {
				// ���������� ��������� �������� ���¿���
				if (tHandler.pickedPrismID(result) < 0)// �ٱ��� �����ߴٸ�
				{
					renderer.prismList.get(renderer.prismList.size() - 1).selectPrism(false);
					renderer.prismList.get(renderer.prismList.size() - 1).changeLineColor();
				}
			}
			if (activity.isGroupMode == false)// �׷��尡 �ƴҶ�
				groupModeFalse(result);
			else// �׷�ȭ����̸�
				groupModeTrue(result);
		}
		// flush event
		tHandler.start_pos = null;
		tHandler.prev_pos = null;
		tHandler.curr_pos = null;

	}
	public void groupModeTrue(float[] result)
	{

		// ������ ���������� ������ ������ ID�� ��� �Ѱ��ִٰ� �ϷḦ ������ ����, �Ѱܥ��� ����
		// ID���� �����Ѵ�.
		// arrayList�� hashset�� �̿��Ѵ�.
		if (tHandler.groupPickedPrismID(result) >= 0) { // ������ ���õǾ��ٸ�, �׷쿡
			int selectedPrism = tHandler.groupPickedPrismID(result);
			boolean check = false;
			//�׷�ȭ��忡�� �ٽ� �ڱ��ڽ��� Ŭ���ϸ� ���������ϰ� �����.
			if(tHandler.newGroup.contains(selectedPrism))
			{
				activity.isExplorerMode = false;
				activity.setMode();
				renderer.prismList.get(selectedPrism).selectPrism(false);
				renderer.prismList.get(selectedPrism).changeLineColor();
			}
			//���࿡ �̹� �׷�ȭ�� �����̶��, �ٸ� �׷����� ��������� �ʰ� ����� ����
			if(renderer.groupManager.contains(selectedPrism))
				check = true;
			
			//�׷�ȭ�� �̷������ ���� �����̸� ���⼭ �߰��Ѵ�.
			if (!check){
				selectedPrism=tHandler.pickedPrismID(result);
				tHandler.newGroup.add(selectedPrism);
			}
		}
	}
	public void groupModeFalse(float[] result)
	{
		if (tHandler.checkSelected == false)// ������ ���õ��� ���� ���¿���
		{
			prismNotSelected(result);
		} else {// ������ ���õǾ��ٸ�, 2������ ������ �� �ִ�
			prismSelected(result);
		}
	}
	public void prismNotSelected(float[] result)
	{

		// 3)���, ������ �����Ѵٸ� ������ id�� �ѱ�� �ȴ�.
		int pickID = tHandler.pickedPrismID(result);
		if (pickID < 0)// ������ ���� ���� ������ -1�� �ݳ��Ѵ�. 0���� �����鼱���� ���� �������̴�.
			outSideClicked();
		else // ������ �Ѵٸ�
			inSideClicked(pickID);
	}
	public void prismSelected(float[] result) {
		boolean selected = false; // �ϴ� �ӽ÷� ������ �����Ѱ��� �������� �ܺθ� �����ߴ��� Ȯ���ϱ� ���� �ӽ� ���� selected
		int selectedPrism = -1;
		for (int i = 0; i < renderer.prismList.size(); i++) {
			if (renderer.prismList.get(i).checkPick(result))
			{
				selected = true;
				selectedPrism = i;
				break;
			}
		}
		// ���⼭ selected�� true�� �ٽ� ������ �����ߴٴ� �̾߱�, false�� �ٱ��� �����ߴٴ� �̾߱�
		if (selected) // 1)���, ������ �����ߴ� ������ �ƴ� �ٸ� ������ �����ߴٸ�, ������ ������ ������ �������� ���ְ�, ���� ������ ������ ������ �����Ű�� ���� ������ ������ id���� �Ѱ��ش�
			prismToprism(selectedPrism);
		else// 2)���, �ٸ� ������ �ƴ϶� �ٱ� ��򰡸� �����ߴٸ� ���� ������ ���������������� �ٲ۴�.
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
		// �ٵ� �� ������ ���̵� groupmanager���� �����ϰ� �ִ� ID�̸� ��
		// �׷� ��ü�� ���������� �ٲ۴�.
		// renderer.groupManager.showGroup();
		tHandler.alreadyGrouped = false;
		tHandler.groupListID = -1;
		// ������ ������ �̹� �׷����� ������ �������� Ȯ���� �Ŀ�
		if(renderer.groupManager.getGroupListID(pickID)>=0)
		{
			tHandler.alreadyGrouped = true;
			tHandler.groupListID = renderer.groupManager.getGroupListID(pickID);
		}
		// �̹� �׷�ȭ�� �������� Ȯ���� �ȴٸ�, ���� �׷��� ��� ������ Id�� �����Ѵ�.
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
		// ���� ������ ���������� �ٲٰ�
		activity.isExplorerMode = false;
		activity.setMode();
		tHandler.checkSelected = true;
		boolean selectedPrismAlreadyGrouped = false;
		// ���õ� ������ �׷����� ������ arrayList�� ������ hashSet�ȿ� �ִ�
		// ��� �������� ������ �����Ű��, �� �������� �Ѳ����� �̵�,ȸ�� ���� �����ϰ�
		// �ؾ���.
		for (int i = 0; i < renderer.groupManager.getGroupList().size(); i++) {
			if (renderer.groupManager.getGroupList().get(i).contains(selectedPrism)) {
				activity.setSelectIndex(renderer.groupManager.getGroupList().get(i));
				selectedPrismAlreadyGrouped = true;
				tHandler.groupListID = i;
				break;
			}
		}
		if (selectedPrismAlreadyGrouped)// ������ ���������� �̹̱׷�� ������ ����������
			groupedPrismSelected();
		else// ������ ���������� �׷�ȵ� ������ �����߾��ٸ�, �Ѱ��� ������ �ٲ۴�.
			notGroupedPrismSelected(selectedPrism);
	}
	private void notGroupedPrismSelected(int selectedPrism) {
		// TODO Auto-generated method stub
		tHandler.alreadyGrouped = false;
		tHandler.groupListID = -1;
		renderer.prismList.get(selectedPrism).selectPrism(true);
		renderer.prismList.get(selectedPrism).changeLineColor();
		// ���ݼ����� ������ ������ �����Ű��
		activity.setSelectedPrism(selectedPrism);
		// ���� ������ ������ id���� �Ѱ��ش�.
		
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
			// ���� ������ ���������� �ٲٰ�
			// ���õ� ���� hashset�ʱ�ȭ
			// activity.getSelectIndex().clear();
			// activity.setSelectedPrism(-1);
			activity.isExplorerMode = true;
			activity.setMode();
			tHandler.checkSelected = false;
		}
	}
}

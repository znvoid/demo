package com.znvoid.demo.imf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

public class ContactsItemTouchHelperCallback extends Callback {
	private ItemTouchMoveListener moveListener;

	public ContactsItemTouchHelperCallback(ItemTouchMoveListener moveListener) {
		this.moveListener = moveListener;
	}

	//Callback�ص�����ʱ�ȵ��õģ������жϵ�ǰ��ʲô�����������жϷ�����˼������Ҫ�����ĸ�������϶���
	@Override
	public int getMovementFlags(RecyclerView recyclerView, ViewHolder holder) {

		//��Ҫ��������ק����������������
		int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
		//��Ҫ������swipe�໬�������ĸ�����
//		int swipeFlags = 0;
		int swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
		
		
		int flags = makeMovementFlags(dragFlags, swipeFlags);
		return flags;
	}
	
	@Override
	public boolean isLongPressDragEnabled() {
		// �Ƿ���������קЧ��
		return true;
	}

	//���ƶ���ʱ��ص��ķ���--��ק
	@Override
	public boolean onMove(RecyclerView recyclerView, ViewHolder srcHolder, ViewHolder targetHolder) {
		if(srcHolder.getItemViewType()!=targetHolder.getItemViewType()){
			return false;
		}
		// ����ק�Ĺ��̵��в��ϵص���adapter.notifyItemMoved(from,to);
		boolean result = moveListener.onItemMove(srcHolder.getAdapterPosition(), targetHolder.getAdapterPosition());
		return result;
	}

	//�໬��ʱ��ص���
	@Override
	public void onSwiped(ViewHolder holder, int arg1) {
		// �����໬��1.ɾ�����ݣ�2.����adapter.notifyItemRemove(position)
		moveListener.onItemRemove(holder.getAdapterPosition());
	}
	
	
	@Override
	public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
		//�ж�ѡ��״̬
		if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE){
			viewHolder.itemView.setBackgroundColor(Color.parseColor("#F06292"));
		}
		super.onSelectedChanged(viewHolder, actionState);
	}
	
	@Override
	public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
		viewHolder.itemView.setBackgroundColor(Color.WHITE);
		super.clearView(recyclerView, viewHolder);
	}
	
	@Override
	public void onChildDraw(Canvas c, RecyclerView recyclerView,
			ViewHolder viewHolder, float dX, float dY, int actionState,
			boolean isCurrentlyActive) {

		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
				isCurrentlyActive);
	}
	

}

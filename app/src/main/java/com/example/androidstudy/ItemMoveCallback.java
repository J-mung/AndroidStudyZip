package com.example.androidstudy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudy.activitys.MainActivity;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    // return true here to enable long press on the RecyclerView rows for drag and drop.
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    // This is used to enable or disable swipes.
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    // Here we pass the flags for the directions of drag and swipe. Since swipe is disable we pass 0 for it.
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    // Here we set the code for the drag and drop.
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAbsoluteAdapterPosition(), target.getAbsoluteAdapterPosition());
        return true;
    }

    // Here we implement the code for swiping.
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /*
    * Based on the current state of the RecyclerView and whether it’s pressed or swiped,
    * this method gets triggered. Here we can customize the RecyclerView row.
    * For example, changing the background color.
    * */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if(viewHolder instanceof MainAdapter.CustomViewHolder) {
                MainAdapter.CustomViewHolder myViewHolder = (MainAdapter.CustomViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    // This method gets triggered when the user interaction stops with the RecyclerView row.
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if(viewHolder instanceof MainAdapter.CustomViewHolder) {
            MainAdapter.CustomViewHolder myViewHolder = (MainAdapter.CustomViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContract {
        void onRowMoved(int fromPosition, int otPosition);
        void onRowSelected(MainAdapter.CustomViewHolder myViewHolder);
        void onRowClear(MainAdapter.CustomViewHolder myViewHolder);
    }
}

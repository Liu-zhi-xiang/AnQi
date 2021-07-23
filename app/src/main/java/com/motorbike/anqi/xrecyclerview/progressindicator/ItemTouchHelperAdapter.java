package com.motorbike.anqi.xrecyclerview.progressindicator;



/**
 * Created by jianghejie on 16/6/20.
 */

public interface ItemTouchHelperAdapter {

    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and <strong>not</strong> at the end of a "drop" event.<br/>
     * <br/>
     * Implementations should call {@link androidx.recyclerview.widget.RecyclerView.Adapter#notifyItemMoved(int, int)} after
     * adjusting the underlying data to reflect this move.
     *
     * @param fromPosition The start position of the moved item.
     * @param toPosition   Then resolved position of the moved item.
     *
     * @see androidx.recyclerview.widget.RecyclerView#getAdapterPositionFor(androidx.recyclerview.widget.RecyclerView.ViewHolder)
     * @see androidx.recyclerview.widget.RecyclerView.ViewHolder#getAdapterPosition()
     */
    void onItemMove(int fromPosition, int toPosition);


    /**
     * Called when an item has been dismissed by a swipe.<br/>
     * <br/>
     * Implementations should call {@link androidx.recyclerview.widget.RecyclerView.Adapter#notifyItemRemoved(int)} after
     * adjusting the underlying data to reflect this removal.
     *
     * @param position The position of the item dismissed.
     *
     * @see androidx.recyclerview.widget.RecyclerView#getAdapterPositionFor(androidx.recyclerview.widget.RecyclerView.ViewHolder)
     * @see androidx.recyclerview.widget.RecyclerView.ViewHolder#getAdapterPosition()
     */
    void onItemDismiss(int position);
}

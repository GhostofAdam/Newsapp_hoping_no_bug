package com.example.myapplication.Utilities;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapter.DeletableNewsListAdapter;
import com.example.myapplication.Adapter.NewsListAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private NewsListAdapter mAdapter;
    public SwipeToDeleteCallback(NewsListAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
}

package com.example.myapplication.Adapter;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Utilities.DownloadImageTask;
import com.example.myapplication.Utilities.News;


import java.util.ArrayList;
import java.util.Vector;

public class NewsListAdapter extends RecyclerView.Adapter{
    public Vector<News> Dataset = new Vector<News>();
    public NewsListAdapter(int size){
        for(int i=0;i<size;i++){
            Dataset.add(new News());
        }
    }
    public NewsListAdapter(Vector<News> d){
        Dataset = d;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
    class Myholder extends RecyclerView.ViewHolder {
        LinearLayout t;
        TextView item_news_tv_title;
        ImageView item_news_tv_img;

        public Myholder(View view) {
            super(view);
            t = (LinearLayout) view;
            item_news_tv_title = view.findViewById(R.id.item_news_tv_title);
            item_news_tv_img = view.findViewById(R.id.item_news_tv_img);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_label, parent, false);
        Myholder vh = new Myholder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {

        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
        Myholder mm = (Myholder) holder;
        mm.item_news_tv_img.setImageBitmap(new DownloadImageTask().download(Dataset.get(position).getImage()));
        mm.item_news_tv_title.setText(Dataset.get(position).getTitle());
    }


    @Override
    public int getItemCount() {
        return Dataset.size();
    }
}

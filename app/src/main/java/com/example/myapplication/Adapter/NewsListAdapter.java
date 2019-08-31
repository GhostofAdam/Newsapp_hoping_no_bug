package com.example.myapplication.Adapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.R;

import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class NewsListAdapter extends RecyclerView.Adapter{
    public Vector<News> Dataset = new Vector<News>();
    private Activity activity;
    private Fragment fragment;
    private boolean no_image=false;
    public NewsListAdapter(int size){
        for(int i=0;i<size;i++){
            Dataset.add(new News());
        }
    }
    public NewsListAdapter(Vector<News> d, Activity c,Fragment b){
        Dataset = d;
        activity = c;
        fragment = b;
    }
    public void notifyAdapter(Vector<News> myLiveList, boolean isAdd){
        if (!isAdd){
            Dataset=myLiveList;
        }else {
            Dataset.addAll(myLiveList);
        }
        notifyDataSetChanged();
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
    class OneImageHolder extends RecyclerView.ViewHolder {
        LinearLayout t;
        TextView item_news_tv_title;
        ImageView item_news_tv_img;

        public OneImageHolder(View view) {
            super(view);
            t = (LinearLayout) view;
            item_news_tv_title = view.findViewById(R.id.item_news_tv_title);
            item_news_tv_img = view.findViewById(R.id.item_news_tv_img);
        }
    }
    class ThreeImageHolder extends RecyclerView.ViewHolder{
        ImageView imageView_1;
        ImageView imageView_2;
        ImageView imageView_3;
        TextView textView;
        LinearLayout layout;
        public ThreeImageHolder(View view){
            super(view);
            layout = (LinearLayout) view;
            imageView_1 = view.findViewById(R.id.item_three_image_news_1);
            imageView_2 = view.findViewById(R.id.item_three_image_news_2);
            imageView_3 = view.findViewById(R.id.item_three_image_news_3);
            textView = view.findViewById(R.id.item_three_image_news_title);
        }
    }
    class NoImageHolder extends RecyclerView.ViewHolder{
        TextView titleView;
        TextView subtitleView;
        public NoImageHolder(View view){
            super(view);
            view = (LinearLayout) view;
            titleView = view.findViewById(R.id.no_image_news_label_title);
            subtitleView = view.findViewById(R.id.no_image_news_label_subtitle);
        }
    }
    class VideoHolder extends RecyclerView.ViewHolder{
        JCVideoPlayerStandard jcVideoPlayer;
        public VideoHolder(View view){
            super(view);
            view = (LinearLayout)view;
            jcVideoPlayer = view.findViewById(R.id.videoplayer);
        }
    }
    public void  setNoImage(){
        no_image=true;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(no_image)
            viewType = 2;
        switch (viewType) {
            case 0: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_label, parent, false);
                OneImageHolder vh = new OneImageHolder(v);
                return vh;
            }
            case 1: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.three_image_label, parent, false);
                ThreeImageHolder vh = new ThreeImageHolder(v);
                return vh;
            }
            case 2:{
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.no_image_news_label, parent, false);
                NoImageHolder vh = new NoImageHolder(v);
                return vh;
            }
            case 3:{
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_news_label, parent, false);
                VideoHolder vh = new VideoHolder(v);
                return vh;
            }
                default:
                return null;
        }
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
        if(holder instanceof OneImageHolder){
            OneImageHolder mm = (OneImageHolder) holder;
            if(activity==null)
                Glide.with(fragment).load(Dataset.get(position).getImageUrl().get(0)).into(mm.item_news_tv_img);
            else
                Glide.with(activity).load(Dataset.get(position).getImageUrl().get(0)).into(mm.item_news_tv_img);
            mm.item_news_tv_title.setText(Dataset.get(position).getTitle());
        }
        else if(holder instanceof ThreeImageHolder){
            ThreeImageHolder vh = (ThreeImageHolder)holder;
            ArrayList<String>urls=Dataset.get(position).getImageUrl();
            if(activity==null) {
                Glide.with(fragment).load(urls.get(0)).skipMemoryCache(true).into(vh.imageView_1);
                Glide.with(fragment).load(urls.get(1)).skipMemoryCache(true).into(vh.imageView_2);
                Glide.with(fragment).load(urls.get(2)).skipMemoryCache(true).into(vh.imageView_3);
            }
            else{
                Glide.with(activity).load(urls.get(0)).skipMemoryCache(true).into(vh.imageView_1);
                Glide.with(activity).load(urls.get(1)).skipMemoryCache(true).into(vh.imageView_2);
                Glide.with(activity).load(urls.get(2)).skipMemoryCache(true).into(vh.imageView_3);
            }
            vh.textView.setText(Dataset.get(position).getTitle());
        }
        else if(holder instanceof NoImageHolder) {
            NoImageHolder vh = (NoImageHolder) holder;
            vh.titleView.setText(Dataset.get(position).getTitle());
            vh.subtitleView.setText(Dataset.get(position).getPublisher()+" "+Dataset.get(position).getPublishTime());
        }
        else if(holder instanceof VideoHolder){
            VideoHolder vh = (VideoHolder)holder;
            //vh.videoView.setVideoURI(Uri.parse("https://www.w3schools.com/html/movie.mp4"));
            vh.jcVideoPlayer.setUp("https://www.w3schools.com/html/movie.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,Dataset.get(position).getTitle());
//            Picasso.with(fragment)
//                    .load(VideoConstant.videoThumbs[pager][position])
//                    .into(viewHolder.jcVideoPlayer.thumbImageView);
        }
        else{

        }

    }
    @Override
    public int getItemViewType(int position) {
        News news = Dataset.get(position);
        if(news.getVideoUrl()!=null){
            return 3;
        }
        else if(news.getImageUrl()!=null&&news.getImageUrl().size()>=3){
            return 1;
        }
        else if(news.getImageUrl()!=null&&news.getImageUrl().size()>=1){
            return 0;
        }
        else
            return  2;


    }

    @Override
    public int getItemCount() {
        return Dataset.size();
    }

    public void deleteItem(int pos){
        Dataset.remove(pos);
        notifyDataSetChanged();
    }
}

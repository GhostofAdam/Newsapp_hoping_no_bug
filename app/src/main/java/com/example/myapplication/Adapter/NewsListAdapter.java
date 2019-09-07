package com.example.myapplication.Adapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DirectAction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.bumptech.glide.load.engine.Resource;
import com.example.myapplication.Activity.MainActivity;
import com.example.myapplication.R;

import com.example.myapplication.SQLite.OperateOnSQLite;
import com.example.myapplication.SQLite.SQLiteDbHelper;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Vector;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class NewsListAdapter extends RecyclerView.Adapter implements Serializable {
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
        TextView subtitle;

        public OneImageHolder(View view) {
            super(view);
            t = (LinearLayout) view;
            item_news_tv_title = view.findViewById(R.id.item_news_tv_title);
            item_news_tv_img = view.findViewById(R.id.item_news_tv_img);
            subtitle = view.findViewById(R.id.item_news_tv_subtitle);
        }
    }
    class ThreeImageHolder extends RecyclerView.ViewHolder{
        LinearLayout t;
        ImageView imageView_1;
        ImageView imageView_2;
        ImageView imageView_3;
        TextView textView;
        TextView subtitle;
        LinearLayout layout;
        public ThreeImageHolder(View view){
            super(view);
            layout = (LinearLayout) view;
            imageView_1 = view.findViewById(R.id.item_three_image_news_1);
            imageView_2 = view.findViewById(R.id.item_three_image_news_2);
            imageView_3 = view.findViewById(R.id.item_three_image_news_3);
            textView = view.findViewById(R.id.item_three_image_news_title);
            subtitle = view.findViewById(R.id.item_three_image_news_subtitle);
        }
    }
    class NoImageHolder extends RecyclerView.ViewHolder{
        LinearLayout t;
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
        LinearLayout t;
        JCVideoPlayerStandard jcVideoPlayer;
        TextView subtitle;
        TextView title;
        public VideoHolder(View view){
            super(view);
            view = (LinearLayout)view;
            jcVideoPlayer = view.findViewById(R.id.videoplayer);
            subtitle = view.findViewById(R.id.item_video_news_subtitle);
            title = view.findViewById(R.id.item_video_news_title);
        }
    }
    public void  setNoImage(){
        no_image=true;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        boolean seen = false;
        android.content.res.Resources res;
        if(activity==null){
            res = fragment.getResources();
        }
        else{
          res = activity.getResources();
        }
        if(no_image) {
            viewType = 2;
        }
        if(viewType>=10){
            viewType-=10;
            seen = true;
        }

        switch (viewType) {
            case 0: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_label, parent, false);

                if(seen){
                    LinearLayout t = (LinearLayout)v;
                    t.setBackground(res.getDrawable(R.drawable.ic_seen,null));
                }
                OneImageHolder vh = new OneImageHolder(v);
                return vh;
            }
            case 1: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.three_image_label, parent, false);
                if(seen){
                    LinearLayout t = (LinearLayout)v;
                    t.setBackground(res.getDrawable(R.drawable.ic_seen,null));
                }
                ThreeImageHolder vh = new ThreeImageHolder(v);
                return vh;
            }
            case 2:{
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.no_image_news_label, parent, false);
                if(seen){
                    LinearLayout t = (LinearLayout)v;

                    t.setBackground(res.getDrawable(R.drawable.ic_seen,null));
                }
                NoImageHolder vh = new NoImageHolder(v);
                return vh;
            }
            case 3:{
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.video_news_label, parent, false);
                if(seen){
                    LinearLayout t = (LinearLayout)v;
                    t.setBackground(res.getDrawable(R.drawable.ic_seen,null));
                }
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
        News news = Dataset.get(position);
        if(holder instanceof OneImageHolder){
            OneImageHolder mm = (OneImageHolder) holder;
            if(activity==null)
                Glide.with(fragment).load(news.getImageUrl().get(0)).into(mm.item_news_tv_img);
            else
                Glide.with(activity).load(news.getImageUrl().get(0)).into(mm.item_news_tv_img);
            mm.item_news_tv_title.setText(news.getTitle());
            mm.subtitle.setText(news.getPublisher()+" "+news.getPublishTime());
        }
        else if(holder instanceof ThreeImageHolder){
            ThreeImageHolder vh = (ThreeImageHolder)holder;
            ArrayList<String>urls=news.getImageUrl();
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
            vh.textView.setText(news.getTitle());
            vh.subtitle.setText(news.getPublisher()+" "+news.getPublishTime());
        }
        else if(holder instanceof NoImageHolder) {
            NoImageHolder vh = (NoImageHolder) holder;
            vh.titleView.setText(news.getTitle());
            vh.subtitleView.setText(news.getPublisher()+" "+news.getPublishTime());
        }
        else if(holder instanceof VideoHolder){
            VideoHolder vh = (VideoHolder)holder;
            String url = Dataset.get(position).getVideoUrl();
            //vh.videoView.setVideoURI(Uri.parse("https://www.w3schools.com/html/movie.mp4"));
            //vh.jcVideoPlayer.setUp("https://www.w3schools.com/html/movie.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,Dataset.get(position).getTitle());
            vh.jcVideoPlayer.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST,Dataset.get(position).getTitle());
            vh.title.setText(news.getTitle());
            vh.subtitle.setText(news.getPublisher()+" "+news.getPublishTime());
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
        int type = 0;
        if(news.getVideoUrl()!=null){
            type=3;
        }
        else if(news.getImageUrl()!=null&&news.getImageUrl().size()>=3){
            type=1;
        }
        else if(news.getImageUrl()!=null&&news.getImageUrl().size()>=1){
            type=0;
        }
        else
            type=2;

        User user;
        if(activity==null)
            user = (User)fragment.getActivity().getApplication();
        else
            user = (User)activity.getApplication();
        if(user.getHistory().contains(news))
            type+=10;
        return type;

    }

    @Override
    public int getItemCount() {
        return Dataset.size();
    }
    public void deleteItem(int pos){
        Dataset.remove(pos);
        notifyDataSetChanged();
    }
    public void setfragment(Fragment f){
        fragment=f;
    }
    public void updateFliter(Vector<String> strings){
        java.util.Iterator<News> iterable = Dataset.iterator();
        while (iterable.hasNext()){
            News news = iterable.next();
            for(String s:strings) {
                if (news.filter(s)){
                    iterable.remove();
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }
}

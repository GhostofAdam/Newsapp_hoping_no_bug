package com.example.myapplication.Adapter;

import java.util.ArrayList;
import java.util.Vector;

import com.example.myapplication.Utilities.News;

public class DeletableNewsListAdapter extends NewsListAdapter{
    public void notifyAdapter(Vector<News> myLiveList, boolean isAdd){
        if (!isAdd){
            super.Dataset=myLiveList;
        }else {
            super.Dataset.addAll(myLiveList);
        }
        notifyDataSetChanged();
    }
    public DeletableNewsListAdapter(int size){
        super(size);
    }
    public void deleteItem(int pos){
        super.Dataset.remove(pos);
        notifyDataSetChanged();
    }
}

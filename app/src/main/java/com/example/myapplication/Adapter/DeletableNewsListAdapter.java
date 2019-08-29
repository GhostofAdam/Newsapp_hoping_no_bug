package com.example.myapplication.Adapter;

import java.util.ArrayList;
import java.util.Vector;

import com.example.myapplication.Utilities.News;

public class DeletableNewsListAdapter extends NewsListAdapter{
    public DeletableNewsListAdapter(int size){
        super(size);
    }
    public void deleteItem(int pos){
        super.Dataset.remove(pos);
        notifyDataSetChanged();
    }
}

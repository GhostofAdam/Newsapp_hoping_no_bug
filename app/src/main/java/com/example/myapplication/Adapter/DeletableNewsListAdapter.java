package com.example.myapplication.Adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Vector;

import com.example.myapplication.Utilities.News;

public class DeletableNewsListAdapter extends NewsListAdapter{
    public DeletableNewsListAdapter(int size){
        super(size);
    }
    public DeletableNewsListAdapter(Vector<News> d, Activity c, Fragment b){
        super(d,c,b);
    }
    public void deleteItem(int pos){
        super.Dataset.remove(pos);
        notifyDataSetChanged();
    }
}

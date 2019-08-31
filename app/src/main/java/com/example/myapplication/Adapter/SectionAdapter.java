package com.example.myapplication.Adapter;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.myapplication.Fragment.Pageholder;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Vector;

public class SectionAdapter extends FragmentStatePagerAdapter{
    private static Vector<String> tab_tiles = new Vector<String>();
    private static Vector<Pageholder>  pages = new Vector<Pageholder>();
    private final Context mContext;
    public SectionAdapter(Context context, FragmentManager fm,String[] chanles) {
        super(fm);
        pages.add(Pageholder.newInstance(0,"推荐"));
        for(int i=0;i<chanles.length;i++){
            pages.add(Pageholder.newInstance(i+1,chanles[i]));
            tab_tiles.add(chanles[i]);
        }
        mContext = context;
    }
    @Override
    public Fragment getItem(int position){
        return pages.get(position);
    }
    @Override
    public CharSequence getPageTitle(int position){
        return pages.get(position).Label;
    }
    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }
    public Vector<String> getTabTitles(){
        return tab_tiles;
    }
    public void removeTabPage(int position) {
        if (!pages.isEmpty() && position<pages.size()) {
            pages.remove(position);
            notifyDataSetChanged();
        }
    }
    public void addTabPage(String title) {
        pages.add(Pageholder.newInstance(pages.size(),""));
        notifyDataSetChanged();
    }
    public void refreshTabPage(ArrayList<String>titles){
        tab_tiles.clear();
        tab_tiles.addAll(titles);
        pages.clear();
        for(int i=0;i<tab_tiles.size();i++){
            pages.add(Pageholder.newInstance(i,tab_tiles.get(i)));
        }
        notifyDataSetChanged();
    }
    public int getCount() {
        return pages.size();
    }
}

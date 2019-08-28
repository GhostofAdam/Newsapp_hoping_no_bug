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
import java.util.Vector;

public class SectionAdapter extends FragmentStatePagerAdapter{
    private static Vector<Integer> TAB_TITLES = new Vector<Integer>();
    private static Vector<Pageholder>  pages = new Vector<Pageholder>();
    private final Context mContext;
    public SectionAdapter(Context context, FragmentManager fm) {
        super(fm);
        pages.add(Pageholder.newInstance(0,"科技"));
        pages.add(Pageholder.newInstance(1,"文艺"));
        pages.add(Pageholder.newInstance(2,"国际"));
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
    public int getCount() {
        return pages.size();
    }
}

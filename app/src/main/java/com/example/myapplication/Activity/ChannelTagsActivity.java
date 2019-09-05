package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.Utilities.User;
import com.zhl.channeltagview.bean.ChannelItem;
import com.zhl.channeltagview.bean.GroupItem;
import com.zhl.channeltagview.listener.OnChannelItemClicklistener;
import com.zhl.channeltagview.listener.UserActionListener;
import com.zhl.channeltagview.view.ChannelTagView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Vector;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

public class ChannelTagsActivity extends AppCompatActivity {
    private ChannelTagView channelTagView;
    private ArrayList<ChannelItem> addedChannels = new ArrayList<>();
    private ArrayList<ChannelItem> unAddedChannels = new ArrayList<>();
    private ArrayList<GroupItem> unAddedItems = new ArrayList<>();
    private Button confirm;
    private ImageButton back;
    private int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = (User)getApplication();
        switch (user.gettheme()){
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.DayTheme);
                break;
            case 2:
                setTheme(R.style.NightTheme);
                break;
            default:
                break;

        }
        setContentView(R.layout.activity_channel_tags);
        channelTagView = (ChannelTagView) findViewById(R.id.channel_tag_view);
        confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    ArrayList<String>strings = new ArrayList<>();
                    for(ChannelItem c:addedChannels){
                        strings.add(c.title);
                    }
                    intent.putExtra("result",strings);
                    setResult(1,intent);
                    finish();
            }});
        back = findViewById(R.id.channel_tag_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        ArrayList<String> added = (ArrayList<String>) intent.getSerializableExtra("data");
        initData(added);
        channelTagView.showPahtAnim(false);
        channelTagView.setCategaryAddedBannerTX("已添加(滑动删除)");
        channelTagView.setCategoryAddedBannerBg(R.attr.colorPrimary);
        channelTagView.setCategrayUnAddedBannerTX("未添加(点击添加)");
        channelTagView.setCategoryUnAddedBannerBg(R.color.colorPrimary);
        //channelTagView.setCategoryBannerTXsize(10);
        //channelTagView.setChannelItemTxSizeSP(10);
        channelTagView.initChannels(addedChannels, unAddedItems, false, new ChannelTagView.RedDotRemainderListener() {
            @Override
            public boolean showAddedChannelBadge(BGABadgeTextView itemView, int position) {
               return false;
            }
            @Override
            public boolean showUnAddedChannelBadge(BGABadgeTextView itemView, int position) {
                return false;
            }
            @Override
            public void handleAddedChannelReddot(BGABadgeTextView itemView, int position) {
                itemView.showCirclePointBadge();
            }
            @Override
            public void handleUnAddedChannelReddot(BGABadgeTextView itemView, int position) {
            }
            @Override
            public void OnDragDismiss(BGABadgeTextView itemView, int position) {
                itemView.hiddenBadge();
            }
        });
        channelTagView.setOnChannelItemClicklistener(new OnChannelItemClicklistener() {

            @Override
            public void onAddedChannelItemClick(View itemView, int position) {
                ChannelItem item = addedChannels.remove(position);
                unAddedChannels.add(item);
            }

            @Override
            public void onUnAddedChannelItemClick(View itemView, int position) {
                ChannelItem item = unAddedChannels.remove(position);
                addedChannels.add(item);
        }

            @Override
            public void onItemDrawableClickListener(View itemView, int position) {
                unAddedChannels.add(addedChannels.remove(position));
            }
        });
        channelTagView.setUserActionListener(new UserActionListener() {
            @Override
            public void onMoved(int fromPos, int toPos, ArrayList<ChannelItem> checkedChannels) {
                addedChannels.clear();
                addedChannels.addAll(checkedChannels);
            }

            @Override
            public void onSwiped(int position, View itemView, ArrayList<ChannelItem> checkedChannels, ArrayList<ChannelItem> uncheckedChannels) {
                unAddedChannels.clear();
                unAddedChannels.addAll(uncheckedChannels);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();

        setResult(-1,intent);
        finish();
    }

    private void initData(ArrayList<String> added) {
        String[] chanles = getResources().getStringArray(R.array.chanles);
        Vector<String> all = new Vector<String>();
        for (int i=0;i<chanles.length;i++){
            all.add(chanles[i]);
        }
        int i=0;
        for(String a:added){
            ChannelItem channelItem = new ChannelItem();
            channelItem.title = a;
            channelItem.id = i++;
            channelItem.category="";
            addedChannels.add(channelItem);
            all.remove(a);
        }
        GroupItem groupItem = new GroupItem();
        groupItem.category = "";
        for (String a:all){
            ChannelItem channelItem = new ChannelItem();
            channelItem.title = a;
            channelItem.id = i++;
            channelItem.category="";
            unAddedChannels.add(channelItem);
            groupItem.addChanelItem(channelItem);
        }
        unAddedItems.add(groupItem);
    }
}

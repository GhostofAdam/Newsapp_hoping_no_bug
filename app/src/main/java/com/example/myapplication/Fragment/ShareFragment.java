package com.example.myapplication.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.Utilities.News;
import com.example.myapplication.Utilities.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;


public class ShareFragment extends BottomSheetDialogFragment {
    public static ShareFragment newInstance(News news) {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT,news);
        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private ImageButton QQ;
    private ImageButton WeChat;
    private ImageButton Moment;
    private ImageButton Zone;

    public static final String ARGUMENT = "argument";
    private News news;
    // show的时候调用
    @Override
    public void show(FragmentManager manager, String tag) {
        Log.e("xiaxl: ", "show");
        super.show(manager, tag);
    }
    // create dialog

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            news = (News) bundle.getSerializable(ARGUMENT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.e("xiaxl: ", "onCreateDialog");
        return super.onCreateDialog(savedInstanceState);
    }
    // 创建View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("xiaxl: ", "onCreateView");
        View inflate = inflater.inflate(R.layout.fragment_share, container);
        QQ = inflate.findViewById(R.id.qq);
        WeChat = inflate.findViewById(R.id.wechat);
        Moment = inflate.findViewById(R.id.moment);
        Zone = inflate.findViewById(R.id.zone);

        QQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        WeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXTextObject textObj = new WXTextObject();
                textObj.text = news.getTitle();

//用 WXTextObject 对象初始化一个 WXMediaMessage 对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                msg.description = news.getPublisher();
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = WXSceneSession;
//调用api接口，发送数据到微信
                User user = (User)getActivity().getApplication();
                user.getAPI().sendReq(req);
            }
        });
        Moment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return inflate;
    }
    // 加载数据

}

package com.example.myapplication.Activity;

import com.example.myapplication.R;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class myWelcomeActivity extends WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.background)
                .page(new TitlePage(R.drawable.aaa,
                        "Avain News").background(R.color.md_grey_800)
                )
                .page(new BasicPage(R.drawable.dalao,
                        "By",
                        "蒋王一 桂尚彤")
                        .background(R.color.firebrick)
                )
                .page(new BasicPage(R.drawable.timg,
                        "Fly freely",
                        "Some birds aren't meant to be caged, that's all. Their feathers are just too bright.") .background(R.color.olive)
                )
                .swipeToDismiss(true)
                .build();
    }
}

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
                .page(new TitlePage(R.drawable.bg_people,
                        "Avain News").background(R.color.black)
                )
                .page(new BasicPage(R.drawable.bg_people,
                        "By",
                        "蒋王一 桂尚彤")
                        .background(R.color.red)
                )
                .page(new BasicPage(R.drawable.bg_people,
                        "Let's start",
                        "See the world with a bird") .background(R.color.orange)
                )
                .swipeToDismiss(true)
                .build();
    }
}

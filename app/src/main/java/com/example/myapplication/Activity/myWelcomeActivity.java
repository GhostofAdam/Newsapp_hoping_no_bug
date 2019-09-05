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
                        "Title")
                )
                .page(new BasicPage(R.drawable.bg_people,
                        "Header",
                        "More text.")
                        .background(R.color.red)
                )
                .page(new BasicPage(R.drawable.bg_people,
                        "Lorem ipsum",
                        "dolor sit amet.")
                )
                .swipeToDismiss(true)
                .build();
    }
}

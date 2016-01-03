package com.jaynewstrom.screenswitchersample.second;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.screenswitchersample.R;
import com.jaynewstrom.screenswitchersample.ScreenManager;
import com.jaynewstrom.screenswitchersample.third.ThirdScreen;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

final class SecondView extends LinearLayout {

    @Inject ScreenManager screenManager;

    SecondView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.holo_orange_light);
        Concrete.inject(context, this);
        LayoutInflater.from(context).inflate(R.layout.second_view, this, true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_third) void onThirdScreenButtonPressed() {
        screenManager.push(new ThirdScreen());
    }
}

package com.jaynewstrom.screenswitchersample.third;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jaynewstrom.screenswitchersample.R;
import com.jaynewstrom.screenswitchersample.ScreenManager;
import com.jaynewstrom.screenswitchersample.second.SecondScreen;
import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

final class ThirdView extends LinearLayout {

    @Inject ScreenManager screenManager;
    @Inject DialogHub dialogHub;

    ThirdView(Context context, ThirdComponent component) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.holo_green_light);
        component.inject(this);
        LayoutInflater.from(context).inflate(R.layout.third_view, this, true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_pop) void onPopButtonClicked() {
        screenManager.pop();
    }

    @OnClick(R.id.btn_pop_two) void onPopTwoButtonClicked() {
        screenManager.pop(2);
    }

    @OnClick(R.id.btn_pop_to_second_screen) void onPopToSecondScreenButtonClicked() {
        screenManager.popTo(new SecondScreen());
    }

    @OnClick(R.id.btn_show_third_dialog) void onShowThirdDialogClicked() {
        dialogHub.show(new ThirdScreenDialogFactory(getContext()));
    }
}

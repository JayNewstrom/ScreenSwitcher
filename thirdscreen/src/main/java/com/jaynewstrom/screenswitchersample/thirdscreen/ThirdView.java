package com.jaynewstrom.screenswitchersample.thirdscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jaynewstrom.screenswitchersample.base.ScreenManager;
import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

final class ThirdView extends LinearLayout {

    @Inject ScreenManager screenManager;
    @Inject DialogHub dialogHub;
    @Inject ThirdScreenNavigator navigator;

    ThirdView(Context context, ThirdComponent component) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.holo_green_light);
        component.inject(this);
        LayoutInflater.from(context).inflate(R.layout.third_view, this, true);
        ButterKnife.bind(this);
    }

    @OnClick(R2.id.btn_pop) void onPopButtonClicked() {
        screenManager.pop();
    }

    @OnClick(R2.id.btn_pop_two) void onPopTwoButtonClicked() {
        screenManager.pop(2);
    }

    @OnClick(R2.id.btn_pop_to_second_screen) void onPopToSecondScreenButtonClicked() {
        navigator.popToSecondScreen();
    }

    @OnClick(R2.id.btn_show_third_dialog) void onShowThirdDialogClicked() {
        dialogHub.show(new ThirdScreenDialogFactory(getContext()));
    }
}

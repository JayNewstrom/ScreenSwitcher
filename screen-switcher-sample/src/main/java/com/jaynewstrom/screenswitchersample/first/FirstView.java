package com.jaynewstrom.screenswitchersample.first;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jaynewstrom.screenswitchersample.R;
import com.jaynewstrom.screenswitchersample.ScreenManager;
import com.jaynewstrom.screenswitchersample.second.SecondScreen;
import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.ButterKnife;
import butterknife.OnClick;

final class FirstView extends LinearLayout {

    @Inject ScreenManager screenManager;
    @Inject DialogHub dialogHub;
    @Inject Provider<FirstDialogFactory> dialogFactoryProvider;

    FirstView(Context context, FirstComponent component) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.white);
        component.inject(this);
        LayoutInflater.from(context).inflate(R.layout.first_view, this, true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_second) void onSecondScreenButtonClicked() {
        screenManager.push(new SecondScreen());
    }

    @OnClick(R.id.btn_replace_with_second) void onReplaceWithSecondScreenButtonClicked() {
        screenManager.replaceScreenWith(new SecondScreen());
    }

    @OnClick(R.id.btn_show_first_dialog) void onShowFirstDialogClicked() {
        dialogHub.show(dialogFactoryProvider.get());
    }
}

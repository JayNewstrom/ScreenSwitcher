package com.jaynewstrom.screenswitchersample.second;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class SecondScreen extends ConcreteScreen {

    @Override public View createViewWithConcreteContext(@NonNull Context context) {
        return new SecondView(context);
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public Object daggerModule() {
        return new SecondScreenModule(this);
    }

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }
}

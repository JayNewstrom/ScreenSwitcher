package com.jaynewstrom.screenswitchersample.first;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class FirstScreen extends ConcreteScreen {

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }

    @Override public View createViewWithConcreteContext(Context context) {
        return new FirstView(context);
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public Object daggerModule() {
        return new FirstScreenModule();
    }
}

package com.jaynewstrom.screenswitchersample.first;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenAnimationConfiguration;
import com.jaynewstrom.screenswitchersample.DefaultAnimationConfiguration;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class FirstScreen extends ConcreteScreen {

    @Override public ScreenAnimationConfiguration animationConfiguration() {
        return new DefaultAnimationConfiguration();
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

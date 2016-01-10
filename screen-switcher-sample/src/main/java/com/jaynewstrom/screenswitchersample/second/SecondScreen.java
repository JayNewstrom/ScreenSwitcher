package com.jaynewstrom.screenswitchersample.second;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenAnimationConfiguration;
import com.jaynewstrom.screenswitchersample.DefaultAnimationConfiguration;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class SecondScreen extends ConcreteScreen {

    @Override public View createViewWithConcreteContext(Context context) {
        return new SecondView(context);
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public Object daggerModule() {
        return new SecondScreenModule(this);
    }

    @Override public ScreenAnimationConfiguration animationConfiguration() {
        return new DefaultAnimationConfiguration();
    }
}

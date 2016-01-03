package com.jaynewstrom.screenswitchersample.third;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenAnimationConfiguration;
import com.jaynewstrom.screenswitchersample.DefaultAnimationConfiguration;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class ThirdScreen extends ConcreteScreen {

    @Override public View createViewWithConcreteContext(Context context) {
        return new ThirdView(context);
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public Object daggerModule() {
        return new ThirdScreenModule();
    }

    @Override public ScreenAnimationConfiguration animationConfiguration() {
        return new DefaultAnimationConfiguration();
    }
}

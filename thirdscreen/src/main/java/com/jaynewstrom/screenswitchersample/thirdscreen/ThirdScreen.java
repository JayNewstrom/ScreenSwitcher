package com.jaynewstrom.screenswitchersample.thirdscreen;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.base.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.base.ParentComponent;
import com.jaynewstrom.screenswitchersample.base.concrete.ConcreteScreen;

public final class ThirdScreen extends ConcreteScreen<ThirdComponent> {

    private final ThirdScreenNavigator navigator;

    public ThirdScreen(ThirdScreenNavigator navigator) {
        this.navigator = navigator;
    }

    @Override protected ConcreteBlock<ThirdComponent> block(ParentComponent parentComponent) {
        return new ThirdScreenBlock(parentComponent, navigator);
    }

    @Override public View createView(Context context, ThirdComponent component) {
        return new ThirdView(context, component);
    }

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }
}

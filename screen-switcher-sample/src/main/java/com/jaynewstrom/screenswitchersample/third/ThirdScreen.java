package com.jaynewstrom.screenswitchersample.third;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class ThirdScreen extends ConcreteScreen<ThirdComponent> {

    @Override protected ConcreteBlock<ThirdComponent> block(MainActivityComponent theParentComponent) {
        return new ThirdScreenBlock(theParentComponent);
    }

    @Override public View createView(Context context, ThirdComponent component) {
        return new ThirdView(context, component);
    }

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }
}

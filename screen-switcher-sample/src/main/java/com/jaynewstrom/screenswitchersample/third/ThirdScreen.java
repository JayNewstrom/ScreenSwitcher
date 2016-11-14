package com.jaynewstrom.screenswitchersample.third;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class ThirdScreen extends ConcreteScreen<ThirdComponent> {

    @Override protected ConcreteBlock<ThirdComponent> block(@NonNull MainActivityComponent theParentComponent) {
        return new ThirdScreenBlock(theParentComponent);
    }

    @Override public View createView(@NonNull Context context, @NonNull ThirdComponent component) {
        return new ThirdView(context, component);
    }

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }
}

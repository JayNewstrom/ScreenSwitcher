package com.jaynewstrom.screenswitchersample.first;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.base.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.base.ParentComponent;
import com.jaynewstrom.screenswitchersample.base.concrete.ConcreteScreen;

public final class FirstScreen extends ConcreteScreen<FirstComponent> {

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }

    @Override protected ConcreteBlock<FirstComponent> block(ParentComponent parentComponent) {
        return new FirstScreenBlock(parentComponent);
    }

    @Override public View createView(Context context, FirstComponent component) {
        return new FirstView(context, component);
    }
}

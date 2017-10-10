package com.jaynewstrom.screenswitchersample.second;

import android.content.Context;
import android.view.View;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.base.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.base.ParentComponent;
import com.jaynewstrom.screenswitchersample.base.concrete.ConcreteScreen;

public final class SecondScreen extends ConcreteScreen<SecondComponent> {

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }

    @Override protected ConcreteBlock<SecondComponent> block(ParentComponent parentComponent) {
        return new SecondScreenBlock(parentComponent, this);
    }

    @Override protected View createView(Context context, SecondComponent component) {
        return new SecondView(context, component);
    }

    @Override public boolean equals(Object o) {
        return o instanceof SecondScreen || super.equals(o);
    }

    @Override public int hashCode() {
        return getClass().getName().hashCode();
    }
}

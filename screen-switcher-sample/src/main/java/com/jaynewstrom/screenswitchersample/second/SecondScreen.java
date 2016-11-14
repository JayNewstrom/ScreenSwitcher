package com.jaynewstrom.screenswitchersample.second;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class SecondScreen extends ConcreteScreen<SecondComponent> {

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }

    @Override protected ConcreteBlock<SecondComponent> block(@NonNull MainActivityComponent theParentComponent) {
        return new SecondScreenBlock(theParentComponent, this);
    }

    @Override protected View createView(@NonNull Context context, @NonNull SecondComponent component) {
        return new SecondView(context, component);
    }

    @Override public boolean equals(Object o) {
        return o instanceof SecondScreen || super.equals(o);
    }

    @Override public int hashCode() {
        return getClass().getName().hashCode();
    }
}

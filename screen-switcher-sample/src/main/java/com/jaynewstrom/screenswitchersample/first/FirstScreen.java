package com.jaynewstrom.screenswitchersample.first;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen;

public final class FirstScreen extends ConcreteScreen<FirstComponent> {

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }

    @Override protected ConcreteBlock<FirstComponent> block(@NonNull MainActivityComponent theParentComponent) {
        return new FirstScreenBlock(theParentComponent);
    }

    @Override public View createView(@NonNull Context context, @NonNull FirstComponent component) {
        return new FirstView(context, component);
    }
}

package com.jaynewstrom.screenswitchersample.first;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.dagger2.Dagger2Screen;

public final class FirstScreen extends Dagger2Screen<FirstComponent> {

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }

    @Override
    protected FirstComponent createComponent(@NonNull MainActivityComponent theParentComponent) {
        return DaggerFirstComponent.builder().mainActivityComponent(theParentComponent).build();
    }

    @Override public View createView(@NonNull Context context, @NonNull FirstComponent component) {
        return new FirstView(context, component);
    }
}

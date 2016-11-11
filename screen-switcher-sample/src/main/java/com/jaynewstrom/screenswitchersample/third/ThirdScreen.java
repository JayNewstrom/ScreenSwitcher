package com.jaynewstrom.screenswitchersample.third;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.dagger2.Dagger2Screen;

public final class ThirdScreen extends Dagger2Screen<ThirdComponent> {

    @Override protected ThirdComponent createComponent(@NonNull MainActivityComponent theParentComponent) {
        return DaggerThirdComponent.builder().mainActivityComponent(theParentComponent).build();
    }

    @Override public View createView(@NonNull Context context, @NonNull ThirdComponent component) {
        return new ThirdView(context, component);
    }

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }
}

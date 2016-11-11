package com.jaynewstrom.screenswitchersample.second;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenTransition;
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.dagger2.Dagger2Screen;

public final class SecondScreen extends Dagger2Screen<SecondComponent> {

    @Override public ScreenTransition transition() {
        return DefaultScreenTransition.INSTANCE;
    }

    @Override protected SecondComponent createComponent(@NonNull MainActivityComponent theParentComponent) {
        return DaggerSecondComponent.builder()
                .mainActivityComponent(theParentComponent)
                .secondScreenModule(new SecondScreenModule(this))
                .build();
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

package com.jaynewstrom.screenswitchersample.dagger2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.ScreenSwitcherApplication;

public abstract class Dagger2Screen<C> implements Screen {

    private C component;

    @Override public final View createView(@NonNull Context context) {
        if (component == null) {
            //noinspection WrongConstant
            MainActivityComponent activityComponent = (MainActivityComponent) context.getSystemService(MainActivityComponent
                    .SCOPE_NAME);
            component = createComponent(activityComponent);
        }
        return createView(context, component);
    }

    protected abstract C createComponent(@NonNull MainActivityComponent theParentComponent);

    protected abstract View createView(@NonNull Context context, @NonNull C component);

    @Override public final void destroyScreen(@NonNull View viewToDestroy) {
        ScreenSwitcherApplication.watchObject(viewToDestroy.getContext(), this);
        ScreenSwitcherApplication.watchObject(viewToDestroy.getContext(), viewToDestroy);
        // TODO: remove the component from the parent component.
    }
}

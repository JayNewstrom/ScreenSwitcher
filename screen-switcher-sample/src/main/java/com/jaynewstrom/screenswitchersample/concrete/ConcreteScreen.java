package com.jaynewstrom.screenswitchersample.concrete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitchersample.ScreenSwitcherApplication;

public abstract class ConcreteScreen implements Screen, ConcreteBlock {

    @Override public final View createView(@NonNull Context context) {
        Context childContext = Concrete.findWall(context).stack(this).createContext(context);
        return createViewWithConcreteContext(childContext);
    }

    public abstract View createViewWithConcreteContext(@NonNull Context context);

    @Override public final void destroyScreen(@NonNull View viewToDestroy) {
        ScreenSwitcherApplication.watchObject(viewToDestroy.getContext(), this);
        ScreenSwitcherApplication.watchObject(viewToDestroy.getContext(), viewToDestroy);
        Concrete.findWall(viewToDestroy.getContext()).destroy();
    }
}

package com.jaynewstrom.screenswitchersample.concrete;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;
import com.jaynewstrom.screenswitchersample.ScreenSwitcherApplication;

public abstract class ConcreteScreen<C> implements Screen {

    private ConcreteWall<C> screenWall;

    @Override public final View createView(Context context, ViewGroup hostView) {
        ConcreteWall<MainActivityComponent> activityWall = Concrete.findWall(context);
        screenWall = activityWall.stack(block(activityWall.getComponent()));
        return createView(screenWall.createContext(context), screenWall.getComponent());
    }

    protected abstract ConcreteBlock<C> block(MainActivityComponent theParentComponent);

    protected abstract View createView(Context context, C component);

    @Override public final void destroyScreen(View viewToDestroy) {
        ScreenSwitcherApplication.watchObject(viewToDestroy.getContext(), this);
        ScreenSwitcherApplication.watchObject(viewToDestroy.getContext(), viewToDestroy.getContext());
        ScreenSwitcherApplication.watchObject(viewToDestroy.getContext(), screenWall.getComponent());
        screenWall.destroy();
    }
}

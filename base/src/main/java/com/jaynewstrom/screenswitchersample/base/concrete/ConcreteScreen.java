package com.jaynewstrom.screenswitchersample.base.concrete;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitchersample.base.LeakWatcher;
import com.jaynewstrom.screenswitchersample.base.ParentComponent;

public abstract class ConcreteScreen<C> implements Screen {

    private LeakWatcher leakWatcher;
    private ConcreteWall<C> screenWall;

    @Override public final View createView(Context context, ViewGroup hostView) {
        ConcreteWall<ParentComponent> activityWall = Concrete.findWall(context);
        leakWatcher = activityWall.getComponent().getLeakWatcher();
        screenWall = activityWall.stack(block(activityWall.getComponent()));
        return createView(screenWall.createContext(context), screenWall.getComponent());
    }

    protected abstract ConcreteBlock<C> block(ParentComponent parentComponent);

    protected abstract View createView(Context context, C component);

    @Override public final void destroyScreen(View viewToDestroy) {
        leakWatcher.watch(this);
        leakWatcher.watch(viewToDestroy.getContext());
        leakWatcher.watch(screenWall.getComponent());
        screenWall.destroy();
    }
}

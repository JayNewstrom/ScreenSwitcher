package com.jaynewstrom.screenswitchersample;

import android.app.Application;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public final class ScreenSwitcherApplication extends Application {

    private ConcreteWall<ApplicationComponent> foundation;
    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        refWatcher = LeakCanary.install(this);
        foundation = Concrete.pourFoundation(applicationComponent());
    }

    private ApplicationComponent applicationComponent() {
        return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this, refWatcher)).build();
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isService(name)) {
            return foundation;
        }
        return super.getSystemService(name);
    }
}

package com.jaynewstrom.screenswitchersample;

import android.app.Application;
import android.content.Context;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public final class ScreenSwitcherApplication extends Application {

    private ConcreteWall foundation;
    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        foundation = Concrete.pourFoundation(new ApplicationModule(), BuildConfig.DEBUG);
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isService(name)) {
            return foundation;
        }
        return super.getSystemService(name);
    }

    public static void watchObject(Context context, Object watchedReference) {
        ScreenSwitcherApplication application = (ScreenSwitcherApplication) context.getApplicationContext();
        application.refWatcher.watch(watchedReference);
    }
}

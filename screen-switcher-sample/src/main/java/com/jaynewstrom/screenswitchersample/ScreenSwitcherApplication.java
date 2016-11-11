package com.jaynewstrom.screenswitchersample;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public final class ScreenSwitcherApplication extends Application {

    private static ApplicationComponent component;
    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        component = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    @Override public Object getSystemService(String name) {
        if (ApplicationComponent.SCOPE_NAME.equals(name)) {
            return component;
        }
        return super.getSystemService(name);
    }

    public static void watchObject(Context context, Object watchedReference) {
        ScreenSwitcherApplication application = (ScreenSwitcherApplication) context.getApplicationContext();
        application.refWatcher.watch(watchedReference);
    }
}

package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.screenswitchersample.base.LeakWatcher;
import com.squareup.leakcanary.RefWatcher;

import dagger.Module;
import dagger.Provides;

@Module
final class ApplicationModule {
    private final ScreenSwitcherApplication application;
    private final RefWatcher refWatcher;

    ApplicationModule(ScreenSwitcherApplication application, RefWatcher refWatcher) {
        this.application = application;
        this.refWatcher = refWatcher;
    }

    @Provides LeakWatcher provideLeakWatcher() {
        return new LeakWatcher() {
            @Override public void watch(Object object) {
                refWatcher.watch(object);
            }
        };
    }
}

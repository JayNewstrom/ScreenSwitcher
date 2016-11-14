package com.jaynewstrom.screenswitchersample;

import dagger.Module;

@Module
final class ApplicationModule {

    private final ScreenSwitcherApplication application;

    ApplicationModule(ScreenSwitcherApplication application) {
        this.application = application;
    }
}

package com.jaynewstrom.screenswitchersample.second;

import dagger.Module;
import dagger.Provides;

@Module
final class SecondScreenModule {

    private final SecondScreen screen;

    SecondScreenModule(SecondScreen screen) {
        this.screen = screen;
    }

    @Provides SecondScreen provideSecondScreen() {
        return screen;
    }
}

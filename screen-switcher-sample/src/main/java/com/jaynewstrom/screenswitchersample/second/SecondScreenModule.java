package com.jaynewstrom.screenswitchersample.second;

import com.jaynewstrom.screenswitchersample.MainActivityModule;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                SecondView.class,
        },
        addsTo = MainActivityModule.class
)
final class SecondScreenModule {

    private final SecondScreen screen;

    SecondScreenModule(SecondScreen screen) {
        this.screen = screen;
    }

    @Provides SecondScreen provideSecondScreen() {
        return screen;
    }
}

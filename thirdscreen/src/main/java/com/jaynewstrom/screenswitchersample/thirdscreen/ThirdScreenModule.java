package com.jaynewstrom.screenswitchersample.thirdscreen;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
final class ThirdScreenModule {
    private final ThirdScreenNavigator navigator;

    ThirdScreenModule(ThirdScreenNavigator navigator) {
        this.navigator = navigator;
    }

    @Provides ThirdScreenNavigator provideNavigator() {
        return navigator;
    }

    @Provides @Named("dialogMessage") static String provideDialogMessage() {
        return "Rotate the device to see if it works!";
    }
}

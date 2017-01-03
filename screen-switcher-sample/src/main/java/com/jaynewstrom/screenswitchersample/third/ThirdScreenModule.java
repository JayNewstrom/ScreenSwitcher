package com.jaynewstrom.screenswitchersample.third;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
final class ThirdScreenModule {
    @Provides @Named("dialogMessage") static String provideDialogMessage() {
        return "Rotate the device to see if it works!";
    }
}

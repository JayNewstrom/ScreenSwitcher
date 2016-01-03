package com.jaynewstrom.screenswitchersample.third;

import com.jaynewstrom.screenswitchersample.MainActivityModule;

import dagger.Module;

@Module(
        injects = {
                ThirdView.class,
        },
        addsTo = MainActivityModule.class
)
final class ThirdScreenModule {

}

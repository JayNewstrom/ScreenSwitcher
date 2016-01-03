package com.jaynewstrom.screenswitchersample.second;

import com.jaynewstrom.screenswitchersample.MainActivityModule;

import dagger.Module;

@Module(
        injects = {
                SecondView.class,
        },
        addsTo = MainActivityModule.class
)
final class SecondScreenModule {

}

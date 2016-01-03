package com.jaynewstrom.screenswitchersample.first;

import com.jaynewstrom.screenswitchersample.MainActivityModule;

import dagger.Module;

@Module(
        injects = {
                FirstView.class,
        },
        addsTo = MainActivityModule.class
)
final class FirstScreenModule {

}

package com.jaynewstrom.screenswitchersample.second;

import com.jaynewstrom.screenswitchersample.MainActivityComponent;

import dagger.Component;

@ForSecondScreen
@Component(
        dependencies = {
                MainActivityComponent.class
        },
        modules = {
                SecondScreenModule.class
        }
)
interface SecondComponent {
    void inject(SecondView view);
}

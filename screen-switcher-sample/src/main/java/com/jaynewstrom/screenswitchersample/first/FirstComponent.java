package com.jaynewstrom.screenswitchersample.first;

import com.jaynewstrom.screenswitchersample.MainActivityComponent;

import dagger.Component;

@ForFirstScreen
@Component(
        dependencies = {
                MainActivityComponent.class
        },
        modules = {
                FirstScreenModule.class
        }
)
interface FirstComponent {
    void inject(FirstView view);
}

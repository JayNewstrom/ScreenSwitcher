package com.jaynewstrom.screenswitchersample.second;

import com.jaynewstrom.screenswitchersample.base.ParentComponent;

import dagger.Component;

@ForSecondScreen
@Component(
        dependencies = {
                ParentComponent.class
        },
        modules = {
                SecondScreenModule.class
        }
)
interface SecondComponent {
    void inject(SecondView view);
}

package com.jaynewstrom.screenswitchersample.first;

import com.jaynewstrom.screenswitchersample.base.ParentComponent;

import dagger.Component;

@ForFirstScreen
@Component(
        dependencies = {
                ParentComponent.class
        },
        modules = {
                FirstScreenModule.class
        }
)
interface FirstComponent {
    void inject(FirstView view);
}

package com.jaynewstrom.screenswitchersample.thirdscreen;

import com.jaynewstrom.screenswitchersample.base.ParentComponent;

import dagger.Component;

@ForThirdScreen
@Component(
        dependencies = {
                ParentComponent.class
        },
        modules = {
                ThirdScreenModule.class
        }
)
interface ThirdComponent {
    void inject(ThirdView view);

    void inject(ThirdScreenDialog thirdScreenDialog);
}

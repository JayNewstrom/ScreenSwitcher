package com.jaynewstrom.screenswitchersample.third;

import com.jaynewstrom.screenswitchersample.MainActivityComponent;

import dagger.Component;

@ForThirdScreen
@Component(
        dependencies = {
                MainActivityComponent.class
        },
        modules = {
                ThirdScreenModule.class
        }
)
interface ThirdComponent {
    void inject(ThirdView view);

    void inject(ThirdScreenDialog thirdScreenDialog);
}

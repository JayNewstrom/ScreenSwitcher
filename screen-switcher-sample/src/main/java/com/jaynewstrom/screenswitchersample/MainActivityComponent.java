package com.jaynewstrom.screenswitchersample;

import dagger.Component;

@ForMainActivity
@Component(
        dependencies = {
                ApplicationComponent.class
        },
        modules = {
                MainActivityModule.class
        }
)
public interface MainActivityComponent {
    String SCOPE_NAME = "MainActivityComponent";

    ScreenManager getScreenManager();

    void inject(MainActivity mainActivity);
}

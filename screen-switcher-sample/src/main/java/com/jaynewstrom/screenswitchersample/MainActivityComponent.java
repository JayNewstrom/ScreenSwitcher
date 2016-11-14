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
    ScreenManager getScreenManager();

    void inject(MainActivity mainActivity);
}

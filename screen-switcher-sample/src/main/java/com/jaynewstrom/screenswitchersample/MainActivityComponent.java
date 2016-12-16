package com.jaynewstrom.screenswitchersample;

import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

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

    DialogHub getDialogHub();

    void inject(MainActivity mainActivity);
}

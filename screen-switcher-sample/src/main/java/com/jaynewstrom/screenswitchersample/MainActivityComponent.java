package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.screenswitchersample.base.ParentComponent;

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
public interface MainActivityComponent extends ParentComponent {
    void inject(MainActivity mainActivity);
}

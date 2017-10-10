package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.screenswitchersample.base.LeakWatcher;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ApplicationModule.class
        }
)
public interface ApplicationComponent {
    LeakWatcher getLeakWatcher();
}

package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitcher.ScreenSwitcherState;
import com.jaynewstrom.screenswitchersample.first.FirstScreen;

import java.util.Collections;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {
                MainActivity.class,
        },
        addsTo = ApplicationModule.class
)
public final class MainActivityModule {

    @Provides @Singleton ScreenManager provideScreenManager(ScreenSwitcherState screenSwitcherState) {
        return new ScreenManager(screenSwitcherState);
    }

    @Provides @Singleton ScreenSwitcherState provideScreenSwitcherState() {
        return new ScreenSwitcherState(Collections.<Screen>singletonList(new FirstScreen()));
    }
}

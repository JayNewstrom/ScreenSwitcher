package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitcher.ScreenSwitcherState;
import com.jaynewstrom.screenswitchersample.first.FirstScreen;

import java.util.Collections;

import dagger.Module;
import dagger.Provides;

@ForMainActivity
@Module
public final class MainActivityModule {

    @Provides @ForMainActivity ScreenManager provideScreenManager(ScreenSwitcherState screenSwitcherState) {
        return new ScreenManager(screenSwitcherState);
    }

    @Provides @ForMainActivity ScreenSwitcherState provideScreenSwitcherState() {
        return new ScreenSwitcherState(Collections.<Screen>singletonList(new FirstScreen()));
    }
}

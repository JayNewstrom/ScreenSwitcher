package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitcher.ScreenLifecycleListener;
import com.jaynewstrom.screenswitcher.ScreenSwitcherState;
import com.jaynewstrom.screenswitchersample.base.ScreenManager;
import com.jaynewstrom.screenswitchersample.first.FirstScreen;
import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

import java.util.Collections;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@ForMainActivity
@Module
public final class MainActivityModule {

    @Provides @ForMainActivity ScreenManager provideScreenManager(ScreenSwitcherState screenSwitcherState) {
        return new ScreenManager(screenSwitcherState);
    }

    @Provides @ForMainActivity ScreenSwitcherState provideScreenSwitcherState(ScreenLifecycleListener lifecycleListener) {
        return new ScreenSwitcherState(lifecycleListener, Collections.<Screen>singletonList(new FirstScreen()));
    }

    @Provides @ForMainActivity ScreenLifecycleListener provideScreenLifecycleListener() {
        return new ScreenLifecycleListener() {
            @Override public void onScreenAdded(Screen screen) {
                Timber.d("Screen added: " + screen);
            }

            @Override public void onScreenRemoved(Screen screen) {
                Timber.d("Screen removed: " + screen);
            }

            @Override public void onScreenBecameActive(Screen screen) {
                Timber.d("Screen became active: " + screen);
            }

            @Override public void onScreenBecameInactive(Screen screen) {
                Timber.d("Screen became inactive: " + screen);
            }
        };
    }

    @Provides @ForMainActivity DialogHub provideDialogHub() {
        return new DialogHub();
    }
}

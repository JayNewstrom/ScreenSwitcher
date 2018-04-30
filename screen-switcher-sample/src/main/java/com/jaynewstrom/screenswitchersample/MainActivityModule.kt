package com.jaynewstrom.screenswitchersample

import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenLifecycleListener
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitchersample.first.FirstScreenFactory
import com.jnewstrom.screenswitcher.dialoghub.DialogHub
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
object MainActivityModule {
    @JvmStatic @Provides @ForMainActivity
    internal fun provideScreenManager(screenSwitcherState: ScreenSwitcherState): ScreenManager {
        return ScreenManager(screenSwitcherState)
    }

    @JvmStatic @Provides @ForMainActivity
    fun provideScreenSwitcherState(lifecycleListener: ScreenLifecycleListener): ScreenSwitcherState {
        return ScreenSwitcherState(lifecycleListener, listOf(FirstScreenFactory.create()))
    }

    @JvmStatic @Provides @ForMainActivity
    fun provideScreenLifecycleListener(): ScreenLifecycleListener {
        return object : ScreenLifecycleListener {
            override fun onScreenAdded(screen: Screen) {
                Timber.d("Screen added: $screen")
            }

            override fun onScreenRemoved(screen: Screen) {
                Timber.d("Screen removed: $screen")
            }

            override fun onScreenBecameActive(screen: Screen) {
                Timber.d("Screen became active: $screen")
            }

            override fun onScreenBecameInactive(screen: Screen) {
                Timber.d("Screen became inactive: $screen")
            }
        }
    }

    @JvmStatic @Provides @ForMainActivity
    fun provideDialogHub(): DialogHub {
        return DialogHub()
    }
}

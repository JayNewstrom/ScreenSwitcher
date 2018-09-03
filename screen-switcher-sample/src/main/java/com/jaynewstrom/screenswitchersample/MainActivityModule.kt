package com.jaynewstrom.screenswitchersample

import com.jaynewstrom.screenswitcher.ScreenLifecycleListener
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.dialogmanager.DialogManager
import com.jaynewstrom.screenswitcher.screenmanager.CompositeScreenLifecycleListener
import com.jaynewstrom.screenswitcher.screenmanager.ScreenManager
import com.jaynewstrom.screenswitchersample.first.FirstScreenFactory
import dagger.Module
import dagger.Provides

@Module
object MainActivityModule {
    @JvmStatic @Provides @ForMainActivity
    internal fun provideScreenManager() = ScreenManager()

    @JvmStatic @Provides @ForMainActivity
    fun provideScreenSwitcherState(lifecycleListener: ScreenLifecycleListener): ScreenSwitcherState {
        return ScreenSwitcherState(lifecycleListener, listOf(FirstScreenFactory.create()))
    }

    @JvmStatic @Provides @ForMainActivity
    fun provideScreenLifecycleListener(
        screenLifecycleListener: CompositeScreenLifecycleListener
    ): ScreenLifecycleListener = screenLifecycleListener

    @JvmStatic @Provides @ForMainActivity
    fun provideCompositeScreenLifecycleListener() = CompositeScreenLifecycleListener()

    @JvmStatic @Provides @ForMainActivity
    internal fun provideDialogManager(
        screenManager: ScreenManager,
        screenLifecycleListener: CompositeScreenLifecycleListener
    ) = DialogManager(screenManager, screenLifecycleListener)
}

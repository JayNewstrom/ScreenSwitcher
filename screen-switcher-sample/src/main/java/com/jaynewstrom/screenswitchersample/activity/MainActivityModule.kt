package com.jaynewstrom.screenswitchersample.activity

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jaynewstrom.screenswitcher.ScreenLifecycleListener
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.dialogmanager.DialogManager
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.CompositeScreenLifecycleListener
import com.jaynewstrom.screenswitcher.tabbar.TabBarItem
import com.jaynewstrom.screenswitcher.tabbar.TabBarScreenFactory
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.badge.BadgeScreenFactory
import com.jaynewstrom.screenswitchersample.color.ColorScreenFactory
import com.jaynewstrom.screenswitchersample.first.FirstScreenFactory
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
internal object MainActivityModule {
    @JvmStatic @Provides @ForMainActivity
    fun provideScreenSwitcherState(
        lifecycleListener: ScreenLifecycleListener,
        navigator: Navigator
    ): ScreenSwitcherState {
        val tabBarItems = mutableListOf<TabBarItem>()
        tabBarItems += TabBarItem(FirstScreenFactory.create(navigator), "Demo", R.drawable.ic_nav_demo)
        tabBarItems += TabBarItem(ColorScreenFactory.create("#FF0000"), "Color", R.drawable.ic_nav_color)
        val badgeCountRelay = BehaviorRelay.createDefault(5)
        tabBarItems += TabBarItem(
            screen = BadgeScreenFactory.create(badgeCountRelay),
            title = "Badge",
            icon = R.drawable.ic_nav_badge,
            badgeCountObservable = badgeCountRelay
        )
        val tabBarScreen = TabBarScreenFactory.create(
            tabBarItems,
            {
                Timber.d("Dashboard Initialized")
            },
            { popListenerView ->
                popListenerView.dialogDisplayer()?.show(ConfirmExitDialogFactory())
                true
            }
        )
        return ScreenSwitcherState(lifecycleListener, listOf(tabBarScreen))
    }

    @JvmStatic @Provides @ForMainActivity
    fun provideScreenLifecycleListener(
        screenLifecycleListener: CompositeScreenLifecycleListener
    ): ScreenLifecycleListener = screenLifecycleListener

    @JvmStatic @Provides @ForMainActivity
    fun provideCompositeScreenLifecycleListener() = CompositeScreenLifecycleListener()

    @JvmStatic @Provides @ForMainActivity
    internal fun provideDialogManager(
        screenLifecycleListener: CompositeScreenLifecycleListener
    ) = DialogManager(screenLifecycleListener)
}

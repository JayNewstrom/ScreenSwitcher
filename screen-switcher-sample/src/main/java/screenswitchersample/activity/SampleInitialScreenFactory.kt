package screenswitchersample.activity

import android.content.Intent
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.tabbar.TabBarItem
import com.jaynewstrom.screenswitcher.tabbar.TabBarScreenFactory
import screenswitchersample.R
import screenswitchersample.badge.BadgeScreenFactory
import screenswitchersample.color.ColorScreenFactory
import screenswitchersample.core.activity.InitialScreenFactory
import screenswitchersample.first.FirstScreenFactory
import timber.log.Timber
import javax.inject.Inject

internal class SampleInitialScreenFactory @Inject constructor(
    private val navigator: Navigator
) : InitialScreenFactory {
    override fun create(intent: Intent?): List<Screen> {
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

        return listOf(tabBarScreen)
    }
}

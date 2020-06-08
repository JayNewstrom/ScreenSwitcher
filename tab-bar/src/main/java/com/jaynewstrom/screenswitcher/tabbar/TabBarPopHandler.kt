package com.jaynewstrom.screenswitcher.tabbar

import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenPopListener
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import javax.inject.Inject

internal class TabBarPopHandler @Inject constructor(
    private val screenSwitcherState: ScreenSwitcherState,
    private val currentTabBarItemStateHolder: CurrentTabBarItemStateHolder,
    private val tabBarItems: List<TabBarItem>,
    @PopListener private val popListener: (view: @JvmSuppressWildcards View) -> Boolean
) : ScreenPopListener {
    override fun onScreenPop(view: View, screen: Screen): Boolean {
        val currentItem = currentTabBarItemStateHolder.state

        val viewGroup = view.findViewById<ViewGroup>(R.id.tab_bar_content)
        var screenView: View = viewGroup
        for (i in 0 until viewGroup.childCount) {
            val childView = viewGroup.getChildAt(i)
            val tabBarItem = childView.getTag(R.id.tab_bar_item) as TabBarItem
            if (tabBarItem.screen == currentItem.screen) {
                screenView = childView
                break
            }
        }

        var handlesPop = screenSwitcherState.handlesPop(screenView, currentItem.screen)
        if (!handlesPop && tabBarItems.indexOf(currentItem) != 0) {
            currentTabBarItemStateHolder.state = tabBarItems.first()
            handlesPop = true
        }
        if (!handlesPop) {
            handlesPop = popListener(view)
        }
        return handlesPop
    }
}

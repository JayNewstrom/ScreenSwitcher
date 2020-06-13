package com.jaynewstrom.screenswitcher.tabbar

import android.view.View
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenPopListener
import javax.inject.Inject

internal class TabBarPopHandler @Inject constructor(
    @PopListener private val popListener: (view: @JvmSuppressWildcards View) -> Boolean
) : ScreenPopListener {
    override fun onScreenPop(view: View, screen: Screen): Boolean {
        val presenter = view.getTag(R.id.tab_bar_presenter) as TabBarPresenter
        if (presenter.handlesScreenPop()) {
            return true
        }
        return popListener(view)
    }
}

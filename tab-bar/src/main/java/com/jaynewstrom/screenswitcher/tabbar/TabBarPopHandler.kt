package com.jaynewstrom.screenswitcher.tabbar

import android.view.View
import com.jaynewstrom.screenswitcher.ScreenPopListener
import javax.inject.Inject

internal class TabBarPopHandler @Inject constructor() : ScreenPopListener {
    override fun onScreenPop(view: View, popContext: Any?): Boolean {
        val presenter = view.getTag(R.id.tab_bar_presenter) as TabBarPresenter
        presenter.handleScreenPop(popContext)
        return true
    }
}

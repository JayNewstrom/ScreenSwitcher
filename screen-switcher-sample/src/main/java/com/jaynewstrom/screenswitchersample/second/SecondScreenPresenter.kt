package com.jaynewstrom.screenswitchersample.second

import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenPopListener
import com.jaynewstrom.screenswitcher.associatedScreen
import com.jaynewstrom.screenswitcher.screenSwitcherState
import javax.inject.Inject

@ForSecondScreen
internal class SecondScreenPresenter @Inject constructor() : ScreenPopListener {
    private var hasConfirmedPop: Boolean = false
    private var view: SecondView? = null

    fun takeView(view: SecondView) {
        this.view = view
        view.screenSwitcherState().registerPopListener(view.associatedScreen(), this)
    }

    fun dropView(view: SecondView) {
        if (this.view === view) {
            this.view = null
        }
    }

    fun popConfirmed() {
        hasConfirmedPop = true
    }

    override fun onScreenPop(screen: Screen): Boolean {
        val view = view ?: return false
        if (!hasConfirmedPop) {
            view.showConfirmPop()
            return true
        }
        return false
    }
}

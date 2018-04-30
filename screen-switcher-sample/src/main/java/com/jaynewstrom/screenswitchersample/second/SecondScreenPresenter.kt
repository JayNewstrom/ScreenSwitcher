package com.jaynewstrom.screenswitchersample.second

import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenPopListener
import com.jaynewstrom.screenswitchersample.ScreenManager

import javax.inject.Inject

@ForSecondScreen
internal class SecondScreenPresenter @Inject constructor(
    screen: Screen,
    screenManager: ScreenManager
) : ScreenPopListener {
    private var hasConfirmedPop: Boolean = false
    private var view: SecondView? = null

    init {
        screenManager.registerPopListener(screen, this)
    }

    fun takeView(view: SecondView) {
        this.view = view
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

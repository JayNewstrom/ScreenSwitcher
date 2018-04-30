package com.jaynewstrom.screenswitchersample

import android.support.annotation.IntRange
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenPopListener
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherState

internal class ScreenManager constructor(private val screenSwitcherState: ScreenSwitcherState) {
    private var screenSwitcher: ScreenSwitcher? = null

    private fun isSameImplementation(screenSwitcher: ScreenSwitcher): Boolean {
        return this.screenSwitcher === screenSwitcher
    }

    fun take(screenSwitcher: ScreenSwitcher) {
        this.screenSwitcher = screenSwitcher
    }

    fun drop(screenSwitcher: ScreenSwitcher) {
        if (isSameImplementation(screenSwitcher)) {
            this.screenSwitcher = null
        }
    }

    fun registerPopListener(screen: Screen, popListener: ScreenPopListener) {
        screenSwitcherState.registerPopListener(screen, popListener)
    }

    fun pop(@IntRange(from = 1) numberToPop: Int = 1) {
        screenSwitcher?.pop(numberToPop)
    }

    fun push(screen: Screen) {
        screenSwitcher?.push(screen)
    }

    fun popTo(screen: Screen) {
        pop(screenSwitcherState.screenCount() - screenSwitcherState.indexOf(screen) - 1)
    }

    fun replaceScreenWith(screen: Screen) {
        screenSwitcher?.replaceScreensWith(1, listOf(screen))
    }

    fun transitioning() = screenSwitcher?.isTransitioning ?: false
}

package com.jaynewstrom.screenswitcher.screenmanager

import android.support.annotation.IntRange
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenPopListener
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherState

class ScreenManager constructor(private val screenSwitcherState: ScreenSwitcherState) {
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

    fun isActiveScreen(screen: Screen): Boolean {
        return screenSwitcherState.screenCount() > 0 &&
            screenSwitcherState.indexOf(screen) == screenSwitcherState.screenCount() - 1
    }
}

/**
 * Returns the screenManager if the view is associated with the current screen. Null otherwise.
 */
tailrec fun View.screenManager(): ScreenManager? {
    val parent = parent as? ViewGroup ?: return null
    val screen = getTag(R.id.screen_switcher_screen) as? Screen?
    if (screen != null) {
        val screenManager = parent.getTag(R.id.screen_manager) as ScreenManager
        return if (screenManager.isActiveScreen(screen)) {
            screenManager
        } else {
            null
        }
    }
    return parent.screenManager()
}

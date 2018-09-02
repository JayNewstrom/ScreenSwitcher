package com.jaynewstrom.screenswitcher.screenmanager

import android.support.annotation.IntRange
import android.view.View
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.screenSwitcherDataIfActive

class ScreenManager {
    var screenSwitcher: ScreenSwitcher? = null

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
}

fun View.screenTransitioner(): ScreenTransitioner? {
    val associatedData = screenSwitcherDataIfActive() ?: return null
    return ScreenTransitioner(associatedData.screenSwitcher, associatedData.screenSwitcherState)
}

class ScreenTransitioner internal constructor(
    private val screenSwitcher: ScreenSwitcher,
    private val screenSwitcherState: ScreenSwitcherState
) {
    fun pop(@IntRange(from = 1) numberToPop: Int = 1) {
        screenSwitcher.pop(numberToPop)
    }

    fun push(screen: Screen) {
        screenSwitcher.push(screen)
    }

    fun popTo(screen: Screen) {
        pop(screenSwitcherState.screenCount() - screenSwitcherState.indexOf(screen) - 1)
    }

    fun replaceScreenWith(screen: Screen) {
        screenSwitcher.replaceScreensWith(1, listOf(screen))
    }
}

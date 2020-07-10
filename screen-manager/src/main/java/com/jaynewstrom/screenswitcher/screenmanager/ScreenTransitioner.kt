package com.jaynewstrom.screenswitcher.screenmanager

import android.view.View
import androidx.annotation.IntRange
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.screenSwitcherDataIfActive

fun View.screenTransitioner(): ScreenTransitioner? {
    val associatedData = screenSwitcherDataIfActive() ?: return null

    // Prevent a transition if another one is already occurring.
    // This can occur if the user clicked the back button, and an animation is running.
    if (associatedData.screenSwitcher.isTransitioning) return null

    return ScreenTransitioner(associatedData.screenSwitcher, associatedData.screenSwitcherState)
}

class ScreenTransitioner internal constructor(
    private val screenSwitcher: ScreenSwitcher,
    private val screenSwitcherState: ScreenSwitcherState
) {
    fun pop(@IntRange(from = 1) numberToPop: Int = 1, popContext: Any? = null) {
        screenSwitcher.pop(numberToPop, popContext)
    }

    fun push(screen: Screen) {
        screenSwitcher.push(screen)
    }

    fun popTo(screen: Screen, popContext: Any? = null) {
        pop(screenSwitcherState.screenCount() - screenSwitcherState.indexOf(screen) - 1, popContext)
    }

    fun replaceScreenWith(screen: Screen, popContext: Any? = null) {
        screenSwitcher.replaceScreensWith(1, listOf(screen), popContext)
    }
}

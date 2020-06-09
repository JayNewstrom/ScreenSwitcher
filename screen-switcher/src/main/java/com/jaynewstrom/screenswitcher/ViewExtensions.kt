package com.jaynewstrom.screenswitcher

import android.view.View
import android.view.ViewGroup

/**
 * Setup required for the view extensions to work outside of the [ScreenSwitcher] hostView.
 */
fun View.setupForViewExtensions(screenSwitcher: ScreenSwitcher, screenSwitcherState: ScreenSwitcherState) {
    setTag(R.id.screen_switcher, screenSwitcher)
    setTag(R.id.screen_switcher_state, screenSwitcherState)
}

/**
 * Returns the screenSwitcherState associated with this view.
 */
fun View.screenSwitcherState(): ScreenSwitcherState {
    return screenSwitcherData().screenSwitcherState
}

/**
 * Returns the screen associated with the view.
 */
tailrec fun View.associatedScreen(): Screen {
    val parent = parent as? ViewGroup ?: throw IllegalStateException("View is not associated with a Screen.")
    val screen = getTag(R.id.screen_switcher_screen) as? Screen?
    if (screen != null) {
        return screen
    }

    // When we detect we found the screen switcher, but no screen, return a dummy screen, rather than crashing on the
    // next recursion.
    if (ScreenSwitcherConfig.failSilentlyWhenPossible && getTag(R.id.screen_switcher) != null) {
        ScreenSwitcherConfig.logger("Returning NoScreenFoundScreen from #associatedScreen - $this")
        return NoScreenFoundScreen
    }

    return parent.associatedScreen()
}

/**
 * Returns the screenSwitcher if the view is associated with the active screen. Null otherwise.
 */
fun View.screenSwitcher(): ScreenSwitcher? {
    val associatedData = screenSwitcherDataIfActive() ?: return null
    return associatedData.screenSwitcher
}

/**
 * Enqueues the transition.
 * Be careful about the lambda passed into this, as it'll potentially outlive the view in terms of lifecycle.
 *
 * 🛳 below refers to the screen associated with this view.
 *
 * If 🛳 is the active screen, and there is no transition occurring, it'll be executed immediately.
 * If 🛳 is the active screen, and there is a transition occurring, it'll be executed when the transition is complete.
 * If 🛳 is in the backstack, and not the active screen, it'll be executed when the screen becomes the active screen.
 * If 🛳 is not in the backstack, it'll be completely ignored.
 */
fun View.enqueueTransition(performTransitionFunction: (ScreenSwitcher) -> Unit) {
    val (screenSwitcher, state, screen) = screenSwitcherData()
    if (state.isActiveScreen(screen) && !screenSwitcher.isTransitioning) {
        performTransitionFunction(screenSwitcher)
    } else {
        state.enqueueTransition(screen, performTransitionFunction)
    }
}

/**
 * Returns [ScreenSwitcherViewExtensionData] if the view is associated with the active [Screen].
 */
fun View.screenSwitcherDataIfActive(): ScreenSwitcherViewExtensionData? {
    val associatedData = screenSwitcherData()
    return if (associatedData.screenSwitcherState.isActiveScreen(associatedData.screen)) associatedData else null
}

/**
 * Returns [ScreenSwitcherViewExtensionData] for the associated [Screen].
 */
tailrec fun View.screenSwitcherData(): ScreenSwitcherViewExtensionData {
    val parent = parent as? ViewGroup ?: throw IllegalStateException("View is not associated with a Screen.")
    val screen = getTag(R.id.screen_switcher_screen) as? Screen?
    if (screen != null) {
        val screenSwitcher = parent.getTag(R.id.screen_switcher) as ScreenSwitcher
        val screenSwitcherState = parent.getTag(R.id.screen_switcher_state) as ScreenSwitcherState
        return ScreenSwitcherViewExtensionData(screenSwitcher, screenSwitcherState, screen, parent)
    }

    // When we detect we found the screen switcher, but no screen, return a dummy screen, rather than crashing on the
    // next recursion.
    if (ScreenSwitcherConfig.failSilentlyWhenPossible && getTag(R.id.screen_switcher) != null) {
        ScreenSwitcherConfig.logger(
            "Returning ScreenSwitcherViewExtensionData with NoScreenFoundScreen from #screenSwitcherData - $this"
        )
        val screenSwitcher = getTag(R.id.screen_switcher) as ScreenSwitcher
        val screenSwitcherState = getTag(R.id.screen_switcher_state) as ScreenSwitcherState
        return ScreenSwitcherViewExtensionData(screenSwitcher, screenSwitcherState, NoScreenFoundScreen, parent)
    }

    return parent.screenSwitcherData()
}

private fun ScreenSwitcherState.isActiveScreen(screen: Screen): Boolean {
    return screenCount() > 0 && indexOf(screen) == screenCount() - 1
}

/**
 * The [ScreenSwitcher] data for an associated view.
 * This object is not safe to hold outside the view lifecycle.
 */
data class ScreenSwitcherViewExtensionData(
    val screenSwitcher: ScreenSwitcher,
    val screenSwitcherState: ScreenSwitcherState,
    val screen: Screen,
    val hostView: ViewGroup
)

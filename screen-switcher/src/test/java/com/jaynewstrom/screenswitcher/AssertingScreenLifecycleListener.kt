package com.jaynewstrom.screenswitcher

internal class AssertingScreenLifecycleListener : ScreenLifecycleListener {
    private var activeScreen: Screen? = null
    private val screens = ArrayList<Screen>()

    override fun onScreenAdded(screen: Screen) {
        if (!screens.add(screen)) {
            throw AssertionError(screen.toString() + " screen already existed in: " + screens)
        }
    }

    override fun onScreenRemoved(screen: Screen) {
        if (!screens.remove(screen)) {
            throw AssertionError(screen.toString() + " screen did not exist in: " + screens)
        }
    }

    override fun onScreenBecameActive(screen: Screen) {
        if (activeScreen != null) {
            throw AssertionError(activeScreen!!.toString() + " was already active.")
        }
        if (!screens.contains(screen)) {
            throw AssertionError(screen.toString() + " does not exist in screens: " + screens)
        }
        activeScreen = screen
    }

    override fun onScreenBecameInactive(screen: Screen) {
        if (activeScreen !== screen) {
            throw AssertionError("Active screen: $activeScreen did not align with screen becoming inactive: $screen")
        }
        if (!screens.contains(screen)) {
            throw AssertionError(screen.toString() + " does not exist in screens: " + screens)
        }
        activeScreen = null
    }
}

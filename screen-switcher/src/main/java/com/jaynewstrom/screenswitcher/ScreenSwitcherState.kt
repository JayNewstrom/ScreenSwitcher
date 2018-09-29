package com.jaynewstrom.screenswitcher

import android.view.View

/**
 * This object is designed to persist in memory when an [android.app.Activity] configuration change occurs.
 */
class ScreenSwitcherState
/**
 * @param lifecycleListener The [ScreenLifecycleListener] to be notified of what's happening on the internals.
 * @param screens The initial screens that the [ScreenSwitcher] should show.
 * @throws IllegalArgumentException if screens is empty
 * @throws IllegalArgumentException if the same screen is passed
 */
(internal val lifecycleListener: ScreenLifecycleListener, screens: List<Screen>) {
    internal val screens: MutableList<Screen>
    private val transitionMap: MutableMap<Screen, (ScreenSwitcher) -> Unit> = mutableMapOf()
    private val popListenerMap: MutableMap<Screen, ScreenPopListener> = mutableMapOf()

    init {
        checkArgument(screens.isNotEmpty()) { "screens must contain at least one screen" }
        this.screens = ArrayList(screens.size)
        for (screen in screens) {
            addScreen(screen)
        }
    }

    /**
     * The pop listener will be automatically unregistered when the [Screen] is popped.
     * See documentation for [ScreenPopListener] for what objects should be registered.
     *
     * @param screen The [Screen] to be notified of before it gets popped.
     * @param popListener The [ScreenPopListener] to call when the [Screen] is trying to be popped.
     */
    fun registerPopListener(screen: Screen, popListener: ScreenPopListener) {
        popListenerMap[screen] = popListener
    }

    /**
     * @return the index of the screen if it exists, or -1 if it doesn't.
     */
    fun indexOf(screen: Screen): Int {
        return screens.indexOf(screen)
    }

    /**
     * @return number of screens that will be used in any associated [ScreenSwitcher].
     */
    fun screenCount(): Int {
        return screens.size
    }

    /**
     * Enqueues a transition to be executed when [Screen] is the active screen and a transition is not occurring.
     * Enqueued transitions are only executed when another transition is complete.
     * If there are no transitions occurring, this will not be executed immediately.
     */
    fun enqueueTransition(fromScreen: Screen, transitionFunction: (screenSwitcher: ScreenSwitcher) -> Unit) {
        if (screens.contains(fromScreen)) {
            transitionMap[fromScreen] = transitionFunction
        }
    }

    internal fun removeActiveScreenTransition(): ((ScreenSwitcher) -> Unit)? {
        if (screens.isEmpty()) return null
        return transitionMap.remove(screens.last())
    }

    internal fun addScreen(screen: Screen) {
        checkArgument(!screens.contains(screen)) { "screen already exists" }
        screens.add(screen)
        lifecycleListener.onScreenAdded(screen)
    }

    internal fun removeScreen(screen: Screen) {
        screens.remove(screen)
        transitionMap.remove(screen)
        popListenerMap.remove(screen)
        lifecycleListener.onScreenRemoved(screen)
    }

    /**
     * Checks to see if this [screen] consumes the pop.
     */
    fun handlesPop(view: View, screen: Screen): Boolean {
        val popListener = popListenerMap[screen]
        return popListener != null && popListener.onScreenPop(view, screen)
    }
}

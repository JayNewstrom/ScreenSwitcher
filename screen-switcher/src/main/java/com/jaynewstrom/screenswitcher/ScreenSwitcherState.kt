package com.jaynewstrom.screenswitcher

import android.os.Parcelable
import android.util.SparseArray
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
    private val screenViewStateMap: MutableMap<Screen, SparseArray<Parcelable>> = mutableMapOf()
    private val screenSwitcherCreatedListenerMap: MutableMap<Screen, MutableList<ScreenSwitcherCreatedListener>> =
        mutableMapOf()

    init {
        checkArgument(screens.isNotEmpty()) { "screens must contain at least one screen" }
        this.screens = ArrayList(15)
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
     *
     * @return true if the pop listener was added.
     */
    fun setPopListener(screen: Screen, popListener: ScreenPopListener): Boolean {
        if (screens.contains(screen)) {
            popListenerMap[screen] = popListener
            return true
        }
        return false
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

    /**
     * Create a nested [ScreenSwitcherState] object, reusing the [ScreenLifecycleListener], for use with a new [ScreenSwitcher].
     */
    fun createNestedState(screens: List<Screen>): ScreenSwitcherState {
        return ScreenSwitcherState(lifecycleListener, screens)
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
        screenViewStateMap.remove(screen)
        lifecycleListener.onScreenRemoved(screen)
        screenSwitcherCreatedListenerMap.remove(screen)
    }

    internal fun handlesPop(view: View, screen: Screen): Boolean {
        val popListener = popListenerMap[screen]
        return popListener != null && popListener.onScreenPop(view, screen)
    }

    internal fun saveViewHierarchyState(screen: Screen, viewState: SparseArray<Parcelable>) {
        screenViewStateMap[screen] = viewState
    }

    internal fun removeViewHierarchyState(screen: Screen): SparseArray<Parcelable>? {
        return screenViewStateMap.remove(screen)
    }

    /**
     * Listen to all subsequent screen switchers that get created within the context of the attached [ScreenSwitcherState].
     */
    interface ScreenSwitcherCreatedListener {
        fun screenSwitcherCreated(screenSwitcher: ScreenSwitcher)
    }

    /**
     * Add a [ScreenSwitcherCreatedListener] for any new screen switcher that's created with this [ScreenSwitcherState].
     * The [Screen] is passed as context, so the listener can be automatically removed when the screen is removed.
     *
     * See [ScreenSwitcherState#unregisterScreenSwitcherCreatedListener].
     *
     * @return true if the listener was added.
     */
    fun registerScreenSwitcherCreatedListener(screen: Screen, listener: ScreenSwitcherCreatedListener): Boolean {
        if (screens.contains(screen)) {
            return screenSwitcherCreatedListenerMap.getOrPut(screen) { mutableListOf() }.add(listener)
        }
        return false
    }

    /**
     * Remove the [ScreenSwitcherCreatedListener] for the given [Screen].
     * The listener will only be removed for the given screen. If the listener is reused for other screens, they will remain.
     *
     * See [ScreenSwitcherState#registerScreenSwitcherCreatedListener].
     *
     * @return true if the listener was removed.
     */
    fun unregisterScreenSwitcherCreatedListener(screen: Screen, listener: ScreenSwitcherCreatedListener): Boolean {
        return screenSwitcherCreatedListenerMap[screen]?.remove(listener) == true
    }

    internal fun screenSwitcherCreated(screenSwitcher: ScreenSwitcher) {
        for (listenerList in screenSwitcherCreatedListenerMap.values) {
            for (listener in listenerList) {
                listener.screenSwitcherCreated(screenSwitcher)
            }
        }
    }
}

package com.jaynewstrom.screenswitcher

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
    private val popListenerMap: MutableMap<Screen, ScreenPopListener> = LinkedHashMap()

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

    internal fun addScreen(screen: Screen) {
        checkArgument(!screens.contains(screen)) { "screen already exists" }
        screens.add(screen)
        lifecycleListener.onScreenAdded(screen)
    }

    internal fun removeScreen(screen: Screen) {
        screens.remove(screen)
        lifecycleListener.onScreenRemoved(screen)
    }

    internal fun handlesPop(screen: Screen): Boolean {
        val popListener = popListenerMap[screen]
        val handlesPop = popListener != null && popListener.onScreenPop(screen)
        if (!handlesPop) {
            popListenerMap.remove(screen)
        }
        return handlesPop
    }
}

package com.jaynewstrom.screenswitcher.screenmanager

import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenLifecycleListener
import timber.log.Timber

class CompositeScreenLifecycleListener : ScreenLifecycleListener {
    private val listeners = mutableSetOf<ScreenLifecycleListener>()

    fun register(listener: ScreenLifecycleListener) {
        listeners.add(listener)
    }

    fun unregister(listener: ScreenLifecycleListener) {
        listeners.remove(listener)
    }

    override fun onScreenAdded(screen: Screen) {
        Timber.d("Screen added: $screen")
        for (listener in listeners) {
            listener.onScreenAdded(screen)
        }
    }

    override fun onScreenRemoved(screen: Screen) {
        Timber.d("Screen removed: $screen")
        for (listener in listeners) {
            listener.onScreenRemoved(screen)
        }
    }

    override fun onScreenBecameActive(screen: Screen) {
        Timber.d("Screen became active: $screen")
        for (listener in listeners) {
            listener.onScreenBecameActive(screen)
        }
    }

    override fun onScreenBecameInactive(screen: Screen) {
        Timber.d("Screen became inactive: $screen")
        for (listener in listeners) {
            listener.onScreenBecameInactive(screen)
        }
    }
}

package com.jaynewstrom.screenswitcher

/**
 * Contains global config for the library.
 */
object ScreenSwitcherConfig {
    /**
     * Defines error handling behavior for failures.
     *
     * Often times APIs that reference this property will return a [NoScreenFoundScreen] instead of throwing an
     * exception.
     */
    var failSilentlyWhenPossible: Boolean = false

    /**
     * Logs warnings associated with the screen switcher.
     */
    var logger: ((message: String) -> Unit) = {}
}

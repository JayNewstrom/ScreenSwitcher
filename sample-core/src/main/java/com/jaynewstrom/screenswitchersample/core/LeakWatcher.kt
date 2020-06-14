package com.jaynewstrom.screenswitchersample.core

interface LeakWatcher {
    fun <T> watch(t: T, description: String)
}

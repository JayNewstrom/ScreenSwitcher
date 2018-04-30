package com.jaynewstrom.screenswitcher

internal inline fun checkArgument(expected: Boolean, crossinline messageProvider: () -> String) {
    if (!expected) {
        throw IllegalArgumentException(messageProvider())
    }
}

internal inline fun checkState(expected: Boolean, crossinline messageProvider: () -> String) {
    if (!expected) {
        throw IllegalStateException(messageProvider())
    }
}

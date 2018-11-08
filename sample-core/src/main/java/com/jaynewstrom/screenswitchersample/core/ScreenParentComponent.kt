package com.jaynewstrom.screenswitchersample.core

import com.jaynewstrom.screenswitcher.ScreenSwitcherState

interface ScreenParentComponent {
    val leakWatcher: LeakWatcher
    val screenSwitcherState: ScreenSwitcherState
}

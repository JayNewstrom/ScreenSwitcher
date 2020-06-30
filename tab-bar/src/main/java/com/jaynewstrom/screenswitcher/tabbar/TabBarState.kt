package com.jaynewstrom.screenswitcher.tabbar

import android.os.Parcelable
import android.util.SparseArray
import screenswitchersample.core.screen.ScreenScope
import screenswitchersample.core.state.StateHolder
import javax.inject.Inject

@ScreenScope
internal class TabBarState @Inject constructor() {
    val savedContentViewState: MutableMap<TabBarItem, SparseArray<Parcelable>> = mutableMapOf()
}

internal typealias CurrentTabBarItemStateHolder = StateHolder<TabBarItem>

package com.jaynewstrom.screenswitcher.tabbar

import android.os.Parcelable
import android.util.SparseArray
import com.jaynewstrom.screenswitchersample.core.ScreenScope
import com.jaynewstrom.screenswitchersample.core.StateHolder
import javax.inject.Inject

@ScreenScope
internal class TabBarState @Inject constructor() {
    val savedContentViewState: MutableMap<TabBarItem, SparseArray<Parcelable>> = mutableMapOf()
}

internal typealias CurrentTabBarItemStateHolder = StateHolder<TabBarItem>

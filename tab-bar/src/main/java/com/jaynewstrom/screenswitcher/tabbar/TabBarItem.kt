package com.jaynewstrom.screenswitcher.tabbar

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.jaynewstrom.screenswitcher.Screen
import io.reactivex.Observable

class TabBarItem(
    internal val screen: Screen,
    private val title: String,
    @DrawableRes private val icon: Int,
    internal val badgeCountObservable: Observable<Int>? = null
) {
    internal fun createIndicatorView(hostView: ViewGroup): BottomNavItemLayout {
        return BottomNavItemLayout(hostView.context, icon, title)
    }

    internal fun createContentView(hostView: ViewGroup): View {
        val view = screen.createView(hostView)
        screen.bindView(view)
        return view
    }
}

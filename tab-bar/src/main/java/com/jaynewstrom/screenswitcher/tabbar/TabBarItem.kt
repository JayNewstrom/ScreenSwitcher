package com.jaynewstrom.screenswitcher.tabbar

import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory
import com.jaynewstrom.screenswitcher.ScreenSwitcherPopHandler
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.screenSwitcherState
import com.jaynewstrom.screenswitchersample.core.inflate
import io.reactivex.Observable

class TabBarItem(
    screen: Screen,
    private val title: String,
    @DrawableRes private val icon: Int,
    internal val badgeCountObservable: Observable<Int>? = null
) {
    private var screen: Screen? = screen
    private var screenSwitcherState: ScreenSwitcherState? = null

    internal fun createIndicatorView(hostView: ViewGroup): BottomNavItemLayout {
        return BottomNavItemLayout(hostView.context, icon, title)
    }

    internal fun createContentView(hostView: ViewGroup): View {
        val state: ScreenSwitcherState = screenSwitcherState
            ?: hostView.screenSwitcherState().createNestedState(listOf(screen!!)).also {
                screenSwitcherState = it // Assign the state to the field so we don't have to create it.
                screen = null // Clear the screen so it doesn't leak.
            }

        val screenHost = hostView.inflate(R.layout.tab_root) as StateAwareFrameLayout
        screenHost.isSaveFromParentEnabled = false

        val message = "Nested Screen Switchers cannot have zero screens. Handle this in the parent screen switcher."
        val popHandler = IllegalStateExceptionScreenSwitcherPopHandler(message)
        val screenSwitcher = ScreenSwitcherFactory.viewScreenSwitcher(screenHost, state, popHandler)

        screenHost.saveStateDelegate = object : StateAwareFrameLayout.SaveStateDelegate {
            override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
                screenSwitcher.saveViewHierarchyStateToScreenSwitcherState()
            }
        }

        return screenHost
    }

    internal fun contentViewHandlesPop(contentView: View): Boolean {
        val screenSwitcherState = contentView.getTag(R.id.screen_switcher_state) as ScreenSwitcherState
        if (screenSwitcherState.screenCount() <= 1) {
            return false
        }
        val screenSwitcher = contentView.getTag(R.id.screen_switcher) as ScreenSwitcher
        screenSwitcher.pop(1)
        return true
    }

    internal fun popContentToRoot(contentView: View) {
        val screenSwitcherState = contentView.getTag(R.id.screen_switcher_state) as ScreenSwitcherState
        if (screenSwitcherState.screenCount() <= 1) {
            return
        }
        val screenSwitcher = contentView.getTag(R.id.screen_switcher) as ScreenSwitcher
        screenSwitcher.pop(screenSwitcherState.screenCount() - 1)
    }
}

private class IllegalStateExceptionScreenSwitcherPopHandler(private val message: String) : ScreenSwitcherPopHandler {
    override fun onLastScreenPopped(popCompleteHandler: ScreenSwitcherPopHandler.PopCompleteHandler) {
        throw IllegalStateException(message)
    }
}

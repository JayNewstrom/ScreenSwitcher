package com.jaynewstrom.screenswitcher.tabbar

import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory
import com.jaynewstrom.screenswitcher.ScreenSwitcherFinishHandler
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.screenSwitcherState
import io.reactivex.Observable
import screenswitchersample.core.screen.TabPopToRoot
import screenswitchersample.core.view.inflate

class TabBarItem(
    private val initialScreenFactory: () -> Screen,
    private val title: String,
    @DrawableRes private val icon: Int,
    internal val badgeCountObservable: Observable<Int>? = null
) {
    private var screenSwitcherState: ScreenSwitcherState? = null

    internal fun createIndicatorView(hostView: ViewGroup): BottomNavItemLayout {
        return BottomNavItemLayout(hostView.context, icon, title)
    }

    internal fun createContentView(hostView: ViewGroup, presenter: TabBarPresenter): View {
        val state: ScreenSwitcherState = screenSwitcherState
            ?: hostView.screenSwitcherState().createNestedState(listOf(initialScreenFactory())).also {
                screenSwitcherState = it // Assign the state to the field so we don't have to create it.
            }

        val screenHost = hostView.inflate(R.layout.tab_root) as StateAwareFrameLayout
        screenHost.isSaveFromParentEnabled = false

        val finishHandler = TabScreenSwitcherFinishHandler(presenter)
        val screenSwitcher = ScreenSwitcherFactory.viewScreenSwitcher(screenHost, state, finishHandler)

        screenHost.saveStateDelegate = object : StateAwareFrameLayout.SaveStateDelegate {
            override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
                screenSwitcher.saveViewHierarchyStateToScreenSwitcherState()
            }
        }

        return screenHost
    }

    internal fun handleScreenPop(contentView: View, popContext: Any?) {
        val screenSwitcher = contentView.getTag(R.id.screen_switcher) as ScreenSwitcher
        if (screenSwitcher.isTransitioning) {
            return
        }
        screenSwitcher.pop(1, popContext)
    }

    internal fun popContentToRoot(contentView: View) {
        val screenSwitcherState = contentView.getTag(R.id.screen_switcher_state) as ScreenSwitcherState
        if (screenSwitcherState.screenCount() <= 1) {
            (contentView as ViewGroup).getChildAt(0).smoothScrollToTop()
            return
        }
        val screenSwitcher = contentView.getTag(R.id.screen_switcher) as ScreenSwitcher
        screenSwitcher.pop(screenSwitcherState.screenCount() - 1, TabPopToRoot)
    }

    private inner class TabScreenSwitcherFinishHandler(
        private val presenter: TabBarPresenter
    ) : ScreenSwitcherFinishHandler {
        override val screenSwitcherShouldFinish: Boolean
            get() {
                val currentTabBarItem = presenter.currentTabBarItemStateHolder.state
                val tabBarItems = presenter.tabBarItems
                // Never remove the zero indexed tab completely.
                // It should remain while the exit confirmation is displayed, or if the exit confirmation is cancelled.
                val isFirstTab = tabBarItems.indexOf(currentTabBarItem) == 0
                if (isFirstTab) {
                    presenter.finishFirstTabCalled()
                }
                return !isFirstTab
            }

        override fun onScreenSwitcherFinished(
            finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler
        ) {
            screenSwitcherState = null
            presenter.removeCurrentContentView()
            finishCompleteHandler.finishComplete()
        }
    }
}

private fun View.smoothScrollToTop() {
    if (this is ScrollView) {
        smoothScrollTo(0, 0)
    } else if (this is RecyclerView) {
        smoothScrollToPosition(0)
    }
}

package com.jaynewstrom.screenswitcher.tabbar

import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import screenswitchersample.core.view.ViewPresenter
import screenswitchersample.core.view.setClickListener
import javax.inject.Inject

/**
 * A tab bar container that lazily instantiates it's TabBarItemFactories content views.
 * View saving/restoring is supported of each TabBarItemFactory's content view.
 */
internal class TabBarPresenter(view: View, component: TabBarComponent) :
    ViewPresenter(view), StateAwareFrameLayout.SaveStateDelegate {
    companion object {
        @LayoutRes val layoutId = R.layout.tab_bar
    }

    @Inject lateinit var tabBarItems: List<TabBarItem>
    @Inject lateinit var state: TabBarState
    @Inject lateinit var currentTabBarItemStateHolder: CurrentTabBarItemStateHolder
    @Inject @PopListener lateinit var popListener: (view: @JvmSuppressWildcards View) -> Boolean

    private val contentViewMap: MutableMap<TabBarItem, View> = mutableMapOf()

    private val content = bindView<StateAwareFrameLayout>(R.id.tab_bar_content)
    private val itemsContainer = bindView<LinearLayout>(R.id.tab_bar_items)

    private var activeItem: TabBarItem? = null

    init {
        component.inject(this)
        content.saveStateDelegate = this
        addItemsToContainer()

        registerObservable(currentTabBarItemStateHolder.stateObservable, ::setContentView)
    }

    private fun addItemsToContainer() {
        tabBarItems.forEach { item ->
            val indicatorView = item.createIndicatorView(itemsContainer)
            indicatorView.setClickListener {
                if (activeItem == item) {
                    item.popContentToRoot(contentViewMap.getValue(item))
                } else {
                    currentTabBarItemStateHolder.state = item
                }
            }
            val layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            itemsContainer.addView(indicatorView, layoutParams)
            if (item.badgeCountObservable != null) {
                registerObservable(item.badgeCountObservable, indicatorView::setBadgeText)
            }
        }
    }

    private fun setIndicators() {
        for (i in 0 until itemsContainer.childCount) {
            itemsContainer.getChildAt(i).isSelected = false
        }

        val tabIndex = tabBarItems.indexOf(currentTabBarItemStateHolder.state)
        val currentTab = itemsContainer.getChildAt(tabIndex)
        currentTab.isSelected = true

        // Clear accessibility focus so that it resets at the beginning of the page.
        currentTab.performAccessibilityAction(
            AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS,
            null
        )
    }

    private fun setContentView(item: TabBarItem) {
        if (item == activeItem) {
            return
        }

        activeItem = item

        for (i in 0 until content.childCount) {
            content.getChildAt(i).visibility = View.GONE
        }

        val contentView = contentViewMap.getOrElse(item) {
            contentViewMap[item] ?: createAndRestoreViewForItem(item)
        }
        contentView.visibility = View.VISIBLE

        setIndicators()
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        contentViewMap.forEach { (itemFactory, view) ->
            val parcelableSparseArray = SparseArray<Parcelable>()
            view.saveHierarchyState(parcelableSparseArray)
            state.savedContentViewState[itemFactory] = parcelableSparseArray
        }
    }

    private fun createAndRestoreViewForItem(item: TabBarItem): View {
        val contentView = item.createContentView(content, this) // Create view.
        contentViewMap[item] = contentView // Store in the map to allow for state preservation.

        // Restore cache, if it exists.
        state.savedContentViewState.remove(item)?.let(contentView::restoreHierarchyState)

        val matchParent = FrameLayout.LayoutParams.MATCH_PARENT
        val layoutParams = FrameLayout.LayoutParams(matchParent, matchParent)
        content.addView(contentView, layoutParams)

        return contentView
    }

    fun handleScreenPop(popContext: Any?) {
        val activeItem = currentTabBarItemStateHolder.state
        activeItem.handleScreenPop(contentViewMap.getValue(activeItem), popContext)
    }

    fun removeCurrentContentView() {
        contentViewMap.remove(activeItem)
        currentTabBarItemStateHolder.state = tabBarItems.first()
    }

    fun finishFirstTabCalled() {
        popListener(view)
    }
}

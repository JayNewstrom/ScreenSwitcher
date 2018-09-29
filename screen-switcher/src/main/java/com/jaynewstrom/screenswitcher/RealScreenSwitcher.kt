package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

internal class RealScreenSwitcher(
    private val context: Context,
    val state: ScreenSwitcherState,
    private val host: ScreenSwitcherHost
) : ScreenSwitcher {
    private var transitioningStackTrace: Array<StackTraceElement>? = null
    private val activity: Activity
    private val screenViewMap: MutableMap<Screen, View>

    override var isTransitioning: Boolean = false
        set(transitioning) {
            field = transitioning

            transitioningStackTrace = if (transitioning) {
                Throwable().stackTrace
            } else {
                null
            }
        }

    init {
        this.activity = getActivity(context)
        this.screenViewMap = LinkedHashMap()
        initializeActivityState()
        setupHostViewForHidingKeyboard()
        host.hostView().setupForViewExtensions(this, state)
    }

    private fun initializeActivityState() {
        val screens = state.screens
        for (screen in screens) {
            createView(screen)
        }
        state.lifecycleListener.onScreenBecameActive(screens[screens.size - 1])
        hideAllButTopScreen()
    }

    private fun createView(screen: Screen): View {
        val view = screen.createView(context, host.hostView())
        view.setTag(R.id.screen_switcher_screen, screen)
        checkArgument(view.parent == null) { "createView should not return a view that has a parent." }
        screenViewMap[screen] = view
        host.addView(view)
        return view
    }

    private fun setupHostViewForHidingKeyboard() {
        host.hostView().isFocusable = true
        host.hostView().isFocusableInTouchMode = true
    }

    override fun push(screen: Screen) {
        ensureTransitionIsNotOccurring("push")
        val screens = state.screens
        checkState(!screens.isEmpty()) { "no screens to transition from" }

        hideKeyboard()

        val backgroundScreen = screens[screens.size - 1]
        val backgroundView = screenViewMap.getValue(backgroundScreen)
        state.lifecycleListener.onScreenBecameInactive(backgroundScreen)

        state.addScreen(screen)
        val view = createView(screen)

        screen.transition().transitionIn(view, backgroundView, EndTransitionRunnable())
        state.lifecycleListener.onScreenBecameActive(screen)
    }

    override fun pop(numberToPop: Int) {
        ensureTransitionIsNotOccurring("pop")
        checkNumberToPop(numberToPop)

        hideKeyboard()

        if (!popListenerConsumedPop(numberToPop)) {
            performPop(numberToPop)
        }
    }

    override fun replaceScreensWith(numberToPop: Int, newScreens: List<Screen>) {
        ensureTransitionIsNotOccurring("replaceScreensWith")
        checkNumberToPop(numberToPop)
        checkArgument(!newScreens.isEmpty()) { "screens must contain at least one screen" }
        val screens = ArrayList(newScreens) // Make defensive copy.

        hideKeyboard()

        if (!popListenerConsumedPop(numberToPop)) {
            val screensToRemove = ArrayList<Screen>(numberToPop)
            for (i in numberToPop downTo 1) {
                screensToRemove.add(state.screens[state.screens.size - i])
            }

            val backgroundScreen = state.screens[state.screens.size - 1]
            val backgroundView = screenViewMap.getValue(backgroundScreen)

            state.lifecycleListener.onScreenBecameInactive(backgroundScreen)

            // Remove the previous screens from state before adding the new ones.
            val onTransitionCompleted = RemoveScreenRunnable(screensToRemove)

            for (screen in screens) {
                state.addScreen(screen)
                val view = createView(screen)
                view.visibility = View.GONE
            }

            val topScreen = screens[screens.size - 1]
            val topView = screenViewMap.getValue(topScreen)
            topView.visibility = View.VISIBLE
            state.lifecycleListener.onScreenBecameActive(topScreen)

            topScreen.transition().transitionIn(topView, backgroundView, onTransitionCompleted)
        }
    }

    private fun hideKeyboard() {
        val currentFocus = activity.currentFocus
        if (currentFocus != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            host.hostView().requestFocus()
        }
    }

    private fun ensureTransitionIsNotOccurring(transitionType: String) {
        checkState(!isTransitioning) {
            String.format("Can't %s while a transition is occurring.\nPrevious transition from: %s",
                transitionType,
                transitioningStackTrace?.contentToString()
            )
        }
    }

    private fun checkNumberToPop(numberToPop: Int) {
        checkArgument(numberToPop >= 1) { "numberToPop < 1" }
        checkArgument(numberToPop <= state.screens.size) {
            String.format("%d screens exists, can't pop %d screens.", state.screens.size, numberToPop)
        }
    }

    private fun popListenerConsumedPop(numberToPop: Int): Boolean {
        val screens = state.screens
        for (i in 1..numberToPop) {
            val screen = screens[screens.size - i]
            val view = screenViewMap.getValue(screen)
            if (state.handlesPop(view, screen)) {
                if (i > 1) {
                    performPop(i - 1)
                }
                return true
            }
        }
        return false
    }

    private fun performPop(numberToPop: Int) {
        val screens = state.screens
        if (screens.size > numberToPop) {
            for (i in 1 until numberToPop) {
                removeScreen(screens[screens.size - 2], true)
            }
            performPopTransition(screens[screens.size - 1], screens[screens.size - 2])
        } else {
            state.lifecycleListener.onScreenBecameInactive(screens[screens.size - 1])
            isTransitioning = true
            for (screen in ArrayList(state.screens)) {
                state.removeScreen(screen)
            }
            host.onLastScreenPopped(object : ScreenSwitcherPopHandler.PopCompleteHandler {
                override fun popComplete() {
                    for (screen in ArrayList(screenViewMap.keys)) {
                        removeScreen(screen, false)
                    }
                }
            })
        }
    }

    private fun performPopTransition(screenToRemove: Screen, screenToBecomeVisible: Screen) {
        state.lifecycleListener.onScreenBecameInactive(screenToRemove)
        val viewToBecomeVisible = screenViewMap.getValue(screenToBecomeVisible)
        viewToBecomeVisible.visibility = View.VISIBLE
        val viewToRemove = screenViewMap.getValue(screenToRemove)
        val completionRunnable = RemoveScreenRunnable(screenToRemove)
        screenToRemove.transition().transitionOut(viewToRemove, viewToBecomeVisible, completionRunnable)
        state.lifecycleListener.onScreenBecameActive(screenToBecomeVisible)
    }

    fun hideAllButTopScreen() {
        val screens = state.screens
        var i = 0
        val max = screens.size - 1
        while (i < max) {
            screenViewMap.getValue(screens[i]).visibility = View.GONE
            i++
        }
    }

    fun removeScreen(screen: Screen, removeFromState: Boolean) {
        val view = screenViewMap.remove(screen)!!
        screen.destroyScreen(view)
        (view.parent as ViewGroup).removeView(view)
        if (removeFromState) {
            state.removeScreen(screen)
        }
    }

    private inner class EndTransitionRunnable : Runnable {
        private var executed: Boolean = false

        init {
            isTransitioning = true
        }

        override fun run() {
            checkState(!executed) { "Transition complete runnable already executed." }
            executed = true
            isTransitioning = false
            hideAllButTopScreen()
            val transitionFunction = state.removeActiveScreenTransition()
            transitionFunction?.invoke(this@RealScreenSwitcher)
        }
    }

    private inner class RemoveScreenRunnable(
        private val screensToRemove: List<Screen>
    ) : Runnable {
        private var executed: Boolean = false

        constructor(screen: Screen) : this(listOf<Screen>(screen))

        init {
            for (screen in screensToRemove) {
                state.removeScreen(screen)
            }

            isTransitioning = true
        }

        override fun run() {
            checkState(!executed) { "RemoveScreenRunnable already executed." }
            executed = true

            for (screen in screensToRemove) {
                removeScreen(screen, false)
            }

            isTransitioning = false
            hideAllButTopScreen()
            val transitionFunction = state.removeActiveScreenTransition()
            transitionFunction?.invoke(this@RealScreenSwitcher)
        }
    }

    companion object {
        @JvmStatic private fun getActivity(context: Context): Activity {
            if (context is Activity) {
                return context
            }
            if (context is ContextWrapper) {
                return getActivity(context.baseContext)
            }
            throw IllegalArgumentException("context is not an instance of Activity")
        }
    }
}

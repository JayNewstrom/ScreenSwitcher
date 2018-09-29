package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import org.junit.Assert.fail
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import java.util.concurrent.atomic.AtomicReference

internal object ScreenTestUtils {
    fun defaultState(screen: Screen): ScreenSwitcherState {
        return defaultState(listOf(screen))
    }

    fun defaultState(screens: List<Screen>): ScreenSwitcherState {
        return ScreenSwitcherState(AssertingScreenLifecycleListener(), screens)
    }

    fun initialActivityScreenSwitcher(extraScreen: Screen? = null, extraScreenIndex: Int = -1): RealScreenSwitcher {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val screens = ArrayList(listOf(screen1, screen2))
        if (extraScreen != null) {
            mockCreateView(extraScreen)
            screens.add(extraScreenIndex, extraScreen)
        }
        `when`(activity.findViewById<View>(android.R.id.content)).thenReturn(mock(ViewGroup::class.java))
        val state = ScreenTestUtils.defaultState(screens)
        val popHandler = mock(ScreenSwitcherPopHandler::class.java)
        return ScreenSwitcherFactory.activityScreenSwitcher(activity, state, popHandler) as RealScreenSwitcher
    }

    fun mockCreateView(screen: Screen, createViewCallback: (() -> Unit)? = null): View {
        val view = mock(View::class.java)
        val context = mock(Context::class.java)
        `when`(view.context).thenReturn(context)
        `when`(screen.createView(kotlinAny(), kotlinAny())).thenAnswer {
            createViewCallback?.invoke()
            view
        }
        return view
    }

    fun addTransitionOut(screen: Screen): AtomicReference<Runnable> {
        val transitionCompletedRunnable = AtomicReference<Runnable>()
        val secondScreenTransition = object : ScreenTransition {
            override fun transitionIn(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable) {
                fail()
            }

            override fun transitionOut(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable) {
                transitionCompletedRunnable.getAndSet(onTransitionCompleted)
            }
        }
        `when`(screen.transition()).thenReturn(secondScreenTransition)
        return transitionCompletedRunnable
    }

    fun addTransitionIn(screenToTransition: Screen): AtomicReference<Runnable> {
        val transitionCompletedRunnable = AtomicReference<Runnable>()
        val secondScreenTransition = object : ScreenTransition {
            override fun transitionIn(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable) {
                transitionCompletedRunnable.getAndSet(onTransitionCompleted)
            }

            override fun transitionOut(foregroundView: View, backgroundView: View, onTransitionCompleted: Runnable) {
                fail()
            }
        }
        `when`(screenToTransition.transition()).thenReturn(secondScreenTransition)
        return transitionCompletedRunnable
    }

    fun testScreenSwitcher(activity: Activity, state: ScreenSwitcherState): ScreenSwitcher {
        val popHandler = mock(ScreenSwitcherPopHandler::class.java)
        return testScreenSwitcher(activity, state, popHandler)
    }

    fun testScreenSwitcher(
        activity: Activity,
        state: ScreenSwitcherState,
        popHandler: ScreenSwitcherPopHandler
    ): ScreenSwitcher {
        val hostView = mock(ViewGroup::class.java)
        `when`(hostView.context).thenReturn(activity)
        `when`(activity.findViewById<View>(android.R.id.content)).thenReturn(hostView)
        doAnswer { invocation ->
            val childView = invocation.getArgument<View>(0)
            `when`<ViewParent>(childView.parent).thenReturn(invocation.mock as ViewGroup)
            null
        }.`when`(hostView).addView(kotlinAny())
        return ScreenSwitcherFactory.viewScreenSwitcher(hostView, state, popHandler)
    }
}

fun <T> kotlinAny(): T {
    Mockito.any<T>()
    return uninitialized()
}

@Suppress("UNCHECKED_CAST") // Hacks on hacks!
private fun <T> uninitialized(): T = null as T

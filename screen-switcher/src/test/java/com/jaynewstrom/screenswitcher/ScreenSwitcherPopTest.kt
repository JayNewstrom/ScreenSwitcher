package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.screenswitcher.ScreenTestUtils.addTransitionOut
import com.jaynewstrom.screenswitcher.ScreenTestUtils.initialActivityScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenTestUtils.mockCreateView
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.concurrent.atomic.AtomicInteger

class ScreenSwitcherPopTest {
    @Test fun popFailsWhenTransitionIsOccurring() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        activityScreenSwitcher.isTransitioning = true
        try {
            activityScreenSwitcher.pop(1, null)
            fail()
        } catch (expected: IllegalStateException) {
            assertThat(expected).hasMessageContaining("Can't pop while a transition is occurring")
        }
    }

    @Test fun popFailsWhenNumberToPopIsLessThanOne() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        try {
            activityScreenSwitcher.pop(0, null)
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("numberToPop < 1")
        }
    }

    @Test fun popFailsWhenPoppingMoreThanTheNumberOfScreens() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        try {
            activityScreenSwitcher.pop(3, null)
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("2 screens exists, can't pop 3 screens.")
        }
    }

    @Test fun doesNotPopWhenTheTopScreenHandlesPopIsTrue() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        val view2 = mockCreateView(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val popListener = mock(ScreenPopListener::class.java)
        `when`(popListener.onScreenPop(view2, screen2, null)).thenReturn(true)
        state.setPopListener(screen2, popListener)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1, null)
        verify(popListener).onScreenPop(view2, screen2, null)
        assertThat(state.screens.size).isEqualTo(2)
    }

    @Test fun onlyPopsUntilHandlesPopIsTrue() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        val view1 = mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val transitionCompletedRunnable = addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val popListener = mock(ScreenPopListener::class.java)
        `when`(popListener.onScreenPop(view1, screen1, null)).thenReturn(true)
        state.setPopListener(screen1, popListener)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(2, null)
        transitionCompletedRunnable.get().run()
        verify(popListener).onScreenPop(view1, screen1, null)
        assertThat(state.screens.size).isEqualTo(1)
    }

    @Test fun whenPoppingMultipleScreensEnsureScreensAreRemovedImmediately() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val screen3 = mock(Screen::class.java)
        mockCreateView(screen3)
        val transitionCompletedRunnable = addTransitionOut(screen3)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2, screen3))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(2, null)
        assertThat(state.screens.size).isEqualTo(1)
        transitionCompletedRunnable.get().run()
        assertThat(state.screens.size).isEqualTo(1)
    }

    @Test fun whenTheScreenIsRemovedEnsureDestroyScreenIsCalled() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val transitionCompletedRunnable = addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1, null)
        transitionCompletedRunnable.get().run()
        verify(screen1, never()).destroyScreen(kotlinAny())
        verify(screen2).destroyScreen(kotlinAny())
    }

    @Test fun whenTheScreenIsRemovedEnsureItIsRemovedFromItsParentView() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        val view2 = mockCreateView(screen2)
        val transitionCompletedRunnable = addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1, null)
        verify(view2.parent as ViewGroup, never()).removeView(view2)
        transitionCompletedRunnable.get().run()
        verify(view2.parent as ViewGroup).removeView(view2)
    }

    @Test fun popMakesBackgroundVisibleDuringTransition() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        val view1 = mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val transitionCompletedRunnable = addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        verify(view1, never()).visibility = View.VISIBLE
        activityScreenSwitcher.pop(1, null)
        verify(view1).visibility = View.VISIBLE
        transitionCompletedRunnable.get().run()
    }

    @Test fun isTransitioningWhenPopping() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val transitionCompletedRunnable = addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
        activityScreenSwitcher.pop(1, null)
        assertThat(activityScreenSwitcher.isTransitioning).isTrue
        transitionCompletedRunnable.get().run()
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
    }

    @Test fun poppingLastScreenCallsFinishHandler() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val state = ScreenTestUtils.defaultState(listOf(screen1))
        val finishHandler = mock(ScreenSwitcherFinishHandler::class.java)
        `when`(finishHandler.screenSwitcherShouldFinish).thenReturn(true)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state, finishHandler)
        activityScreenSwitcher.pop(1, null)
        verify(finishHandler).onScreenSwitcherFinished(kotlinAny())
        assertThat(state.screenCount()).isEqualTo(0)
        assertThat(activityScreenSwitcher.isTransitioning).isTrue
    }

    @Test fun poppingLastScreenPreservesStateWhenFinishHandlerShouldFinishIsFalse() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val state = ScreenTestUtils.defaultState(listOf(screen1))
        val finishHandler = mock(ScreenSwitcherFinishHandler::class.java)
        `when`(finishHandler.screenSwitcherShouldFinish).thenReturn(false)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state, finishHandler)
        activityScreenSwitcher.pop(1, null)
        verify(finishHandler, never()).onScreenSwitcherFinished(kotlinAny())
        assertThat(state.screenCount()).isEqualTo(1)
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
    }

    @Test fun poppingLastScreenAndCallingFinishHandlerDestroysScreen() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val state = ScreenTestUtils.defaultState(listOf(screen1))
        val finishHandler = object : ScreenSwitcherFinishHandler {
            override fun onScreenSwitcherFinished(
                finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler
            ) {
                finishCompleteHandler.finishComplete()
            }
        }
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state, finishHandler)
        activityScreenSwitcher.pop(1, null)
        assertThat(state.screenCount()).isEqualTo(0)
        assertThat(activityScreenSwitcher.isTransitioning).isTrue
        verify(screen1).destroyScreen(kotlinAny())
    }

    @Test fun poppingRemovesCorrectScreens() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val screen3 = mock(Screen::class.java)
        mockCreateView(screen3)
        val screen4 = mock(Screen::class.java)
        mockCreateView(screen4)
        addTransitionOut(screen4)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2, screen3, screen4))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(3, null)
        assertThat(state.screenCount()).isEqualTo(1)
        assertThat(state.screens).contains(screen1)
    }

    @Test fun enqueuedTransitionIsExecutedAfterPopping() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val transitionCompletedRunnable = addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1, null)
        val transitionCalledCounter = AtomicInteger()
        state.enqueueTransition(screen1) {
            transitionCalledCounter.incrementAndGet()
        }
        assertThat(transitionCalledCounter.get()).isZero
        transitionCompletedRunnable.get().run()
        assertThat(transitionCalledCounter.get()).isEqualTo(1)
    }

    @Test fun ensureScreenViewHierarchyInfoIsRemovedFromState() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        val savedHierarchy = SparseArray<Parcelable>()
        state.saveViewHierarchyState(screen2, savedHierarchy)
        activityScreenSwitcher.pop(1, null)
        assertThat(state.screenCount()).isEqualTo(1)
        assertThat(state.screens).containsExactly(screen1)
        assertThat(state.removeViewHierarchyState(screen2)).isNull() // Pop removed it, this didn't.
    }

    @Test fun popContextIsPassedToPopListener() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val popListenerCalledCount = AtomicInteger(0)
        val popListener = object : ScreenPopListener {
            override fun onScreenPop(view: View, screen: Screen, popContext: Any?): Boolean {
                assertThat(popContext).isEqualTo("Hello")
                popListenerCalledCount.incrementAndGet()
                return true
            }
        }
        val popContext = "Hello"
        state.setPopListener(screen2, popListener)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1, popContext)
        assertThat(popListenerCalledCount.get()).isEqualTo(1)
    }
}

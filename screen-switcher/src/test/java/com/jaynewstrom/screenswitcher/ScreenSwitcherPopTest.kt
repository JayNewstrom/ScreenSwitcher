package com.jaynewstrom.screenswitcher

import android.app.Activity
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
import java.util.Arrays

class ScreenSwitcherPopTest {
    @Test fun popFailsWhenTransitionIsOccurring() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        activityScreenSwitcher.isTransitioning = true
        try {
            activityScreenSwitcher.pop(1)
            fail()
        } catch (expected: IllegalStateException) {
            assertThat(expected).hasMessageContaining("Can't pop while a transition is occurring")
        }
    }

    @Test fun popFailsWhenNumberToPopIsLessThanOne() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        try {
            activityScreenSwitcher.pop(0)
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("numberToPop < 1")
        }
    }

    @Test fun popFailsWhenPoppingMoreThanTheNumberOfScreens() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        try {
            activityScreenSwitcher.pop(3)
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
        mockCreateView(screen2)
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2))
        val popListener = mock(ScreenPopListener::class.java)
        `when`(popListener.onScreenPop(screen2)).thenReturn(true)
        state.registerPopListener(screen2, popListener)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1)
        verify(popListener).onScreenPop(screen2)
        assertThat(state.screens.size).isEqualTo(2)
    }

    @Test fun onlyPopsUntilHandlesPopIsTrue() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val transitionCompletedRunnable = addTransitionOut(screen2)
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2))
        val popListener = mock(ScreenPopListener::class.java)
        `when`(popListener.onScreenPop(screen1)).thenReturn(true)
        state.registerPopListener(screen1, popListener)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(2)
        transitionCompletedRunnable.get().run()
        verify(popListener).onScreenPop(screen1)
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
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2, screen3))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(2)
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
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1)
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
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(1)
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
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        verify(view1, never()).visibility = View.VISIBLE
        activityScreenSwitcher.pop(1)
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
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
        activityScreenSwitcher.pop(1)
        assertThat(activityScreenSwitcher.isTransitioning).isTrue
        transitionCompletedRunnable.get().run()
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
    }

    @Test fun poppingLastScreenCallsPopHandler() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val state = ScreenTestUtils.defaultState(listOf(screen1))
        val popHandler = mock(ScreenSwitcherPopHandler::class.java)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state, popHandler)
        activityScreenSwitcher.pop(1)
        verify(popHandler).onLastScreenPopped(kotlinAny())
        assertThat(state.screenCount()).isEqualTo(1)
        assertThat(activityScreenSwitcher.isTransitioning).isTrue
    }

    @Test fun poppingLastScreenAndCallingPopHandlerDestroysScreen() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val state = ScreenTestUtils.defaultState(listOf(screen1))
        val popHandler = object : ScreenSwitcherPopHandler {
            override fun onLastScreenPopped(popCompleteHandler: ScreenSwitcherPopHandler.PopCompleteHandler) {
                popCompleteHandler.popComplete()
            }
        }
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state, popHandler)
        activityScreenSwitcher.pop(1)
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
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2, screen3, screen4))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        activityScreenSwitcher.pop(3)
        assertThat(state.screenCount()).isEqualTo(1)
        assertThat(state.screens).contains(screen1)
    }
}

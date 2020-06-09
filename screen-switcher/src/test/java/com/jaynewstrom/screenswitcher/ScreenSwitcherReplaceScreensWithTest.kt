package com.jaynewstrom.screenswitcher

import android.app.Activity
import com.jaynewstrom.screenswitcher.ScreenTestUtils.addTransitionIn
import com.jaynewstrom.screenswitcher.ScreenTestUtils.initialActivityScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenTestUtils.mockCreateView
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class ScreenSwitcherReplaceScreensWithTest {
    @Test fun failsWhenTransitionIsOccurring() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        activityScreenSwitcher.isTransitioning = true
        try {
            activityScreenSwitcher.replaceScreensWith(1, listOf(mock(Screen::class.java)))
            fail()
        } catch (expected: IllegalStateException) {
            assertThat(expected).hasMessageContaining("Can't replaceScreensWith while a transition is occurring")
        }
    }

    @Test fun failsWhenNumberToPopIsLessThanOne() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        try {

            activityScreenSwitcher.replaceScreensWith(0, listOf(mock(Screen::class.java)))
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("numberToPop < 1")
        }
    }

    @Test fun failsWhenPoppingMoreThanTheNumberOfScreens() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        try {
            activityScreenSwitcher.replaceScreensWith(3, listOf(mock(Screen::class.java)))
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("2 screens exists, can't pop 3 screens.")
        }
    }

    @Test fun failsWhenScreensIsEmpty() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        try {
            activityScreenSwitcher.replaceScreensWith(1, emptyList())
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("screens must contain at least one screen")
        }
    }

    @Test fun isTransitioningWhenReplacing() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
        val newScreen = mock(Screen::class.java)
        mockCreateView(newScreen)
        val transitionCompletedRunnable = addTransitionIn(newScreen)
        activityScreenSwitcher.replaceScreensWith(1, listOf(newScreen))
        assertThat(activityScreenSwitcher.isTransitioning).isTrue
        transitionCompletedRunnable.get().run()
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
    }

    @Test fun givesPreviousStacktraceWhenCallingTransitionWhenTransitioning() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        `when`(screen2.transition()).thenReturn(mock(ScreenTransition::class.java))
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        testCallingTransitionHelper(activityScreenSwitcher)
        try {
            activityScreenSwitcher.replaceScreensWith(1, listOf(mock(Screen::class.java)))
            fail()
        } catch (expected: IllegalStateException) {
            assertThat(expected).hasMessageContaining("testCallingTransitionHelper")
        }
    }

    private fun testCallingTransitionHelper(screenSwitcher: ScreenSwitcher) {
        screenSwitcher.pop(1)
    }

    @Test fun enqueuedTransitionIsExecutedAfterReplace() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        val newScreen = mock(Screen::class.java)
        mockCreateView(newScreen)
        val transitionCompletedRunnable = addTransitionIn(newScreen)
        activityScreenSwitcher.replaceScreensWith(1, listOf(newScreen))
        val transitionCalledCounter = AtomicInteger()
        state.enqueueTransition(newScreen) {
            transitionCalledCounter.incrementAndGet()
        }
        assertThat(transitionCalledCounter.get()).isZero
        transitionCompletedRunnable.get().run()
        assertThat(transitionCalledCounter.get()).isEqualTo(1)
    }

    @Test fun replaceWithShouldRemoveAllScreensFromStateBeforeAddingNewOnes() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        val newScreen = mock(Screen::class.java)
        val createViewCalled = AtomicBoolean(false)
        mockCreateView(newScreen) {
            assertThat(state.indexOf(newScreen)).isEqualTo(0)
            assertThat(state.screenCount()).isEqualTo(1)
            createViewCalled.set(true)
        }
        addTransitionIn(newScreen)
        assertThat(createViewCalled.get()).isFalse
        activityScreenSwitcher.replaceScreensWith(2, listOf(newScreen))
        assertThat(createViewCalled.get()).isTrue
    }
}

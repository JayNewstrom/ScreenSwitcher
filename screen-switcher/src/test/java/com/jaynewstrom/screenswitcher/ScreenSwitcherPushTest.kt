package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.view.View
import com.jaynewstrom.screenswitcher.ScreenTestUtils.addTransitionIn
import com.jaynewstrom.screenswitcher.ScreenTestUtils.initialActivityScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenTestUtils.mockCreateView
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.Arrays

class ScreenSwitcherPushTest {
    @Test fun pushFailsWhenTransitionIsOccurring() {
        val activityScreenSwitcher = initialActivityScreenSwitcher()
        activityScreenSwitcher.isTransitioning = true
        try {
            activityScreenSwitcher.push(mock(Screen::class.java))
            fail()
        } catch (expected: IllegalStateException) {
            assertThat(expected).hasMessageContaining("Can't push while a transition is occurring")
        }
    }

    @Test fun pushFailsWhenNoScreensExist() {
        val screen = mock(Screen::class.java)
        mockCreateView(screen)
        val activity = mock(Activity::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        state.removeScreen(state.screens[0])
        try {
            activityScreenSwitcher.push(mock(Screen::class.java))
            fail()
        } catch (expected: IllegalStateException) {
            assertThat(expected).hasMessage("no screens to transition from")
        }
    }

    @Test fun backgroundIsVisibleDuringPush() {
        val activity = mock(Activity::class.java)
        val initialScreen = mock(Screen::class.java)
        val initialView = mockCreateView(initialScreen)
        val state = ScreenTestUtils.defaultState(initialScreen)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        val secondScreen = mock(Screen::class.java)
        mockCreateView(secondScreen)
        val transitionCompletedRunnable = addTransitionIn(secondScreen)
        activityScreenSwitcher.push(secondScreen)
        verify(secondScreen).transition()
        verify(initialView, never()).visibility = View.GONE
        transitionCompletedRunnable.get().run()
        verify(initialView).visibility = View.GONE
    }

    @Test fun pushAddsNewScreenAfterCurrentScreen() {
        val activity = mock(Activity::class.java)
        val screen = mock(Screen::class.java)
        mockCreateView(screen)
        val state = ScreenTestUtils.defaultState(screen)
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        val pushedScreen = mock(Screen::class.java)
        mockCreateView(pushedScreen)
        val transitionCompletedRunnable = addTransitionIn(pushedScreen)
        activityScreenSwitcher.push(pushedScreen)
        transitionCompletedRunnable.get().run()
        assertThat(state.screens.indexOf(screen)).isEqualTo(0)
        assertThat(state.screens.indexOf(pushedScreen)).isEqualTo(1)
    }

    @Test fun isTransitioningWhenPushing() {
        val activity = mock(Activity::class.java)
        val screen1 = mock(Screen::class.java)
        mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        mockCreateView(screen2)
        val state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2))
        val activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
        val newScreen = mock(Screen::class.java)
        mockCreateView(newScreen)
        val transitionCompletedRunnable = addTransitionIn(newScreen)
        activityScreenSwitcher.push(newScreen)
        assertThat(activityScreenSwitcher.isTransitioning).isTrue
        transitionCompletedRunnable.get().run()
        assertThat(activityScreenSwitcher.isTransitioning).isFalse
    }
}

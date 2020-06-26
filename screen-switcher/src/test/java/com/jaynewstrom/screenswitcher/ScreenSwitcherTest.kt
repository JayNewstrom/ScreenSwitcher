package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class ScreenSwitcherTest {
    @Test fun transitionIsNotCalledWhenCreatingScreenSwitcher() {
        val screen = mock(Screen::class.java)
        ScreenTestUtils.mockCreateView(screen)
        val activity = mock(Activity::class.java)
        ScreenTestUtils.testScreenSwitcher(activity, ScreenTestUtils.defaultState(screen))
        verify(screen).createView(kotlinAny(), kotlinAny())
        verify(screen, never()).transition()
    }

    @Test fun allButTopScreenIsGoneWhenCreatingScreenSwitcher() {
        val activity = mock(Activity::class.java)
        val bottomScreen = mock(Screen::class.java)
        val bottomView = mock(View::class.java)
        `when`(bottomScreen.createView(kotlinAny(), kotlinAny())).thenReturn(bottomView)
        val topScreen = mock(Screen::class.java)
        val topView = mock(View::class.java)
        `when`(topScreen.createView(kotlinAny(), kotlinAny())).thenReturn(topView)
        val state = ScreenTestUtils.defaultState(listOf(bottomScreen, topScreen))
        ScreenTestUtils.testScreenSwitcher(activity, state)
        verify(bottomScreen).createView(kotlinAny(), kotlinAny())
        verify(topScreen).createView(kotlinAny(), kotlinAny())
        verify(bottomView).visibility = View.GONE
    }

    @Test fun ensureScreenViewHierarchyStateIsSaved() {
        val screen1 = mock(Screen::class.java)
        val view1 = ScreenTestUtils.mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        val view2 = ScreenTestUtils.mockCreateView(screen2)
        val activity = mock(Activity::class.java)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        val screenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state)
        screenSwitcher.saveViewHierarchyStateToScreenSwitcherState()
        verify(view1).saveHierarchyState(kotlinAny())
        verify(view2).saveHierarchyState(kotlinAny())
    }

    @Test fun ensureViewRestoreHierarchyStateIsCalledWhenPresent() {
        val screen1 = mock(Screen::class.java)
        val view1 = ScreenTestUtils.mockCreateView(screen1)
        val savedHierarchy1 = SparseArray<Parcelable>()
        val screen2 = mock(Screen::class.java)
        val view2 = ScreenTestUtils.mockCreateView(screen2)
        val savedHierarchy2 = SparseArray<Parcelable>()
        val activity = mock(Activity::class.java)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        state.saveViewHierarchyState(screen1, savedHierarchy1)
        state.saveViewHierarchyState(screen2, savedHierarchy2)
        ScreenTestUtils.testScreenSwitcher(activity, state)
        verify(view1).restoreHierarchyState(savedHierarchy1)
        verify(view2).restoreHierarchyState(savedHierarchy2)

        // Creating the ScreenSwitcher should remove the view hierarchy state.
        assertThat(state.removeViewHierarchyState(screen1)).isNull()
        assertThat(state.removeViewHierarchyState(screen2)).isNull()
    }

    @Test fun ensureViewRestoreHierarchyStateIsNotCalledWhenMissing() {
        val screen1 = mock(Screen::class.java)
        val view1 = ScreenTestUtils.mockCreateView(screen1)
        val screen2 = mock(Screen::class.java)
        val view2 = ScreenTestUtils.mockCreateView(screen2)
        val activity = mock(Activity::class.java)
        val state = ScreenTestUtils.defaultState(listOf(screen1, screen2))
        ScreenTestUtils.testScreenSwitcher(activity, state)
        verify(view1, never()).restoreHierarchyState(kotlinAny())
        verify(view2, never()).restoreHierarchyState(kotlinAny())
    }

    @Test fun ensureScreenSwitcherCreatedListenerIsCalled() {
        val screen1 = mock(Screen::class.java)
        ScreenTestUtils.mockCreateView(screen1)
        val activity = mock(Activity::class.java)
        val state = ScreenTestUtils.defaultState(screen1)
        val listener = mock(ScreenSwitcherState.ScreenSwitcherCreatedListener::class.java)
        state.registerScreenSwitcherCreatedListener(screen1, listener)
        ScreenTestUtils.testScreenSwitcher(activity, state)
        verify(listener).screenSwitcherCreated(kotlinAny())
    }
}

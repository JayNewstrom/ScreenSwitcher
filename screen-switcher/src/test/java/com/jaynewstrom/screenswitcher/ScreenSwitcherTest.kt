package com.jaynewstrom.screenswitcher

import android.app.Activity
import android.view.View
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.Arrays

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
        val state = ScreenTestUtils.defaultState(Arrays.asList(bottomScreen, topScreen))
        ScreenTestUtils.testScreenSwitcher(activity, state)
        verify(bottomScreen).createView(kotlinAny(), kotlinAny())
        verify(topScreen).createView(kotlinAny(), kotlinAny())
        verify(bottomView).visibility = View.GONE
    }
}

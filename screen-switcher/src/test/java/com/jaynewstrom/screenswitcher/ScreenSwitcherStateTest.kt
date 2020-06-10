package com.jaynewstrom.screenswitcher

import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class ScreenSwitcherStateTest {
    @Test fun constructorMakesDefensiveCopyOfScreensPassedIn() {
        val passedList = listOf(mock(Screen::class.java))
        val state = ScreenTestUtils.defaultState(passedList)
        state.screens.add(mock(Screen::class.java))
        assertThat(passedList).hasSize(1)
        assertThat(state.screens).hasSize(2)
    }

    @Test fun constructorDoesNotAllowEmptyScreens() {
        try {
            ScreenTestUtils.defaultState(emptyList())
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("screens must contain at least one screen")
        }
    }

    @Test fun constructorRejectsDuplicateScreens() {
        val screen = mock(Screen::class.java)
        try {
            ScreenTestUtils.defaultState(listOf(screen, screen))
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("screen already exists")
        }
    }

    @Test fun handlesPopIsFalseWhenNoPopListeners() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        assertThat(state.handlesPop(view, screen)).isFalse
    }

    @Test fun handlesPopIsFalseWhenPopListenerReturnsFalse() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.registerPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, screen)).thenReturn(false)
        assertThat(state.handlesPop(view, screen)).isFalse
    }

    @Test fun handlesPopIsTrueWhenPopListenerReturnsTrue() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.registerPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, screen)).thenReturn(true)
        assertThat(state.handlesPop(view, screen)).isTrue
    }

    @Test fun handlesPopIsFalseForScreenNotMatchingPopListenerThatReturnsTrue() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.registerPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, screen)).thenReturn(true)
        assertThat(state.handlesPop(mock(View::class.java), mock(Screen::class.java))).isFalse
    }

    @Test fun removeScreenRemovesPopListener() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.registerPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, screen)).thenReturn(true)
        assertThat(state.handlesPop(view, screen)).isTrue
        state.removeScreen(screen)
        assertThat(state.handlesPop(view, screen)).isFalse
    }

    @Test fun handlesPopKeepsPopListenerWhenReturnsTrue() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.registerPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, screen)).thenReturn(true)
        assertThat(state.handlesPop(view, screen)).isTrue
        assertThat(state.handlesPop(view, screen)).isTrue
        verify(popListener, times(2)).onScreenPop(view, screen)
    }

    @Test fun addScreenRejectsDuplicateScreen() {
        val screen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        try {
            state.addScreen(screen)
            fail()
        } catch (expected: IllegalArgumentException) {
            assertThat(expected).hasMessage("screen already exists")
        }
    }

    @Test fun indexOfReturnsNegativeOneIfTheScreenDoesNotExist() {
        val state = ScreenTestUtils.defaultState(mock(Screen::class.java))
        assertThat(state.indexOf(mock(Screen::class.java))).isEqualTo(-1)
    }

    @Test fun indexOfReturnsTheCorrectIndexOfAnExistingScreen() {
        val screen0 = mock(Screen::class.java)
        val screen1 = mock(Screen::class.java)
        val screen2 = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(listOf(screen0, screen1, screen2))
        assertThat(state.indexOf(screen1)).isEqualTo(1)
    }

    @Test fun screenCountIsUpdatedWhenScreensAreAdded() {
        val state = ScreenTestUtils.defaultState(listOf(mock(Screen::class.java), mock(Screen::class.java)))
        assertThat(state.screenCount()).isEqualTo(2)
        state.addScreen(mock(Screen::class.java))
        assertThat(state.screenCount()).isEqualTo(3)
    }

    @Test fun removeActiveScreenTransitionReturnsTheCorrectTransitionForScreen() {
        val screen0 = mock(Screen::class.java)
        val transition0: (screenSwitcher: ScreenSwitcher) -> Unit = { throw AssertionError("0") }
        val screen1 = mock(Screen::class.java)
        val transition1: (screenSwitcher: ScreenSwitcher) -> Unit = { throw AssertionError("1") }
        val screen2 = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(listOf(screen0, screen1, screen2))
        state.enqueueTransition(screen0, transition0)
        state.enqueueTransition(screen1, transition1)
        assertThat(state.removeActiveScreenTransition()).isNull() // 2
        state.removeScreen(screen2)
        assertThat(state.removeActiveScreenTransition()).isSameAs(transition1) // 1
        assertThat(state.removeActiveScreenTransition()).isNull() // 1 is gone now
        state.removeScreen(screen1)
        assertThat(state.removeActiveScreenTransition()).isSameAs(transition0) // 0
        state.removeScreen(screen0)
        assertThat(state.removeActiveScreenTransition()).isNull() // None left
    }

    @Test fun viewHierarchyStateCanBeAddedAndRemoved() {
        val screen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val savedHierarchy = SparseArray<Parcelable>()
        state.saveViewHierarchyState(screen, savedHierarchy)
        assertThat(state.removeViewHierarchyState(screen)).isEqualTo(savedHierarchy)
        assertThat(state.removeViewHierarchyState(screen)).isNull() // It's gone!
    }

    @Test fun removeScreenRemovesViewHierarchyState() {
        val screen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val savedHierarchy = SparseArray<Parcelable>()
        state.saveViewHierarchyState(screen, savedHierarchy)
        state.removeScreen(screen)
        assertThat(state.removeViewHierarchyState(screen)).isNull() // It's gone!
    }
}

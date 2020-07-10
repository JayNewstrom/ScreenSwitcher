package com.jaynewstrom.screenswitcher

import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import org.fest.assertions.api.Assertions.assertThat
import org.fest.assertions.data.MapEntry
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import java.util.concurrent.atomic.AtomicInteger

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
        assertThat(state.handlesPop(view, screen, null)).isFalse
    }

    @Test fun setPopListenerSkipsMissingScreens() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        val missingScreen = mock(Screen::class.java)
        assertThat(state.setPopListener(missingScreen, popListener)).isFalse
        assertThat(state.handlesPop(view, screen, null)).isFalse
        verify(popListener, never()).onScreenPop(kotlinAny(), kotlinAny())
    }

    @Test fun handlesPopIsFalseWhenPopListenerReturnsFalse() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.setPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, null)).thenReturn(false)
        assertThat(state.handlesPop(view, screen, null)).isFalse
    }

    @Test fun handlesPopIsTrueWhenPopListenerReturnsTrue() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.setPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, null)).thenReturn(true)
        assertThat(state.handlesPop(view, screen, null)).isTrue
    }

    @Test fun handlesPopIsFalseForScreenNotMatchingPopListenerThatReturnsTrue() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.setPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, null)).thenReturn(true)
        assertThat(state.handlesPop(mock(View::class.java), mock(Screen::class.java), null)).isFalse
    }

    @Test fun removeScreenRemovesPopListener() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.setPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, null)).thenReturn(true)
        assertThat(state.handlesPop(view, screen, null)).isTrue
        state.removeScreen(screen)
        assertThat(state.handlesPop(view, screen, null)).isFalse
    }

    @Test fun handlesPopKeepsPopListenerWhenReturnsTrue() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListener = mock(ScreenPopListener::class.java)
        state.setPopListener(screen, popListener)
        `when`(popListener.onScreenPop(view, null)).thenReturn(true)
        assertThat(state.handlesPop(view, screen, null)).isTrue
        assertThat(state.handlesPop(view, screen, null)).isTrue
        verify(popListener, times(2)).onScreenPop(view, null)
    }

    @Test fun handlesPopPassesPopContextToPopListener() {
        val screen = mock(Screen::class.java)
        val view = mock(View::class.java)
        val state = ScreenTestUtils.defaultState(screen)
        val popListenerCalledCount = AtomicInteger(0)
        val popListener = object : ScreenPopListener {
            override fun onScreenPop(view: View, popContext: Any?): Boolean {
                @Suppress("UNCHECKED_CAST")
                assertThat(popContext as Map<String, String>).contains(MapEntry.entry("Foo", "Bar"))
                popListenerCalledCount.incrementAndGet()
                return false
            }
        }
        state.setPopListener(screen, popListener)
        val popContext = mapOf(Pair("Foo", "Bar"))
        assertThat(state.handlesPop(view, screen, popContext)).isFalse
        assertThat(popListenerCalledCount.get()).isEqualTo(1)
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
        val transition0: (data: ScreenSwitcherData) -> Unit = { throw AssertionError("0") }
        val screen1 = mock(Screen::class.java)
        val transition1: (data: ScreenSwitcherData) -> Unit = { throw AssertionError("1") }
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

    @Test fun createNestedStateCopiesLifecycleListener() {
        val rootScreen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(rootScreen)
        val nestedScreen = mock(Screen::class.java)
        val nestedState = state.createNestedState(listOf(nestedScreen))
        assertThat(state.lifecycleListener).isSameAs(nestedState.lifecycleListener)
    }

    @Test fun testScreenSwitcherCreatedListenerNotAddedWhenScreenDoesNotExist() {
        val rootScreen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(rootScreen)
        val associatedScreen = mock(Screen::class.java)
        val listener = mock(ScreenSwitcherState.ScreenSwitcherCreatedListener::class.java)
        assertThat(state.registerScreenSwitcherCreatedListener(associatedScreen, listener)).isFalse
        state.screenSwitcherCreated(mock(ScreenSwitcher::class.java))
        verify(listener, never()).screenSwitcherCreated(kotlinAny())
        assertThat(state.unregisterScreenSwitcherCreatedListener(associatedScreen, listener)).isFalse
    }

    @Test fun testScreenSwitcherCreatedListenerHappyPath() {
        val rootScreen = mock(Screen::class.java)
        val associatedScreen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(listOf(rootScreen, associatedScreen))
        val listener = mock(ScreenSwitcherState.ScreenSwitcherCreatedListener::class.java)
        assertThat(state.registerScreenSwitcherCreatedListener(associatedScreen, listener)).isTrue
        state.screenSwitcherCreated(mock(ScreenSwitcher::class.java))
        verify(listener).screenSwitcherCreated(kotlinAny())
        verifyNoMoreInteractions(listener)
        assertThat(state.unregisterScreenSwitcherCreatedListener(associatedScreen, listener)).isTrue
        state.screenSwitcherCreated(mock(ScreenSwitcher::class.java))
        assertThat(state.unregisterScreenSwitcherCreatedListener(associatedScreen, listener)).isFalse
    }

    @Test fun testScreenSwitcherCreatedListenerMultipleListenersCalled() {
        val rootScreen = mock(Screen::class.java)
        val associatedScreen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(listOf(rootScreen, associatedScreen))
        val listener1 = mock(ScreenSwitcherState.ScreenSwitcherCreatedListener::class.java)
        val listener2 = mock(ScreenSwitcherState.ScreenSwitcherCreatedListener::class.java)
        assertThat(state.registerScreenSwitcherCreatedListener(associatedScreen, listener1)).isTrue
        assertThat(state.registerScreenSwitcherCreatedListener(associatedScreen, listener2)).isTrue
        state.screenSwitcherCreated(mock(ScreenSwitcher::class.java))
        verify(listener1).screenSwitcherCreated(kotlinAny())
        verify(listener2).screenSwitcherCreated(kotlinAny())
        assertThat(state.unregisterScreenSwitcherCreatedListener(associatedScreen, listener1)).isTrue
        assertThat(state.unregisterScreenSwitcherCreatedListener(associatedScreen, listener2)).isTrue
        verifyNoMoreInteractions(listener1, listener2)
        state.screenSwitcherCreated(mock(ScreenSwitcher::class.java))
    }

    @Test fun testScreenSwitcherCreatedListenerIsRemovedWhenScreenIsRemoved() {
        val rootScreen = mock(Screen::class.java)
        val associatedScreen = mock(Screen::class.java)
        val state = ScreenTestUtils.defaultState(listOf(rootScreen, associatedScreen))
        val listener = mock(ScreenSwitcherState.ScreenSwitcherCreatedListener::class.java)
        assertThat(state.registerScreenSwitcherCreatedListener(associatedScreen, listener)).isTrue
        state.screenSwitcherCreated(mock(ScreenSwitcher::class.java))
        verify(listener).screenSwitcherCreated(kotlinAny())
        verifyNoMoreInteractions(listener)
        state.removeScreen(associatedScreen)
        state.screenSwitcherCreated(mock(ScreenSwitcher::class.java))
        assertThat(state.unregisterScreenSwitcherCreatedListener(associatedScreen, listener)).isFalse
    }
}

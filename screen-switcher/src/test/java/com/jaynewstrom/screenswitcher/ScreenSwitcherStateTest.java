package com.jaynewstrom.screenswitcher;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class ScreenSwitcherStateTest {

    @Test public void constructorMakesDefensiveCopyOfScreensPassedIn() {
        List<Screen> passedList = Collections.singletonList(mock(Screen.class));
        ScreenSwitcherState state = ScreenTestUtils.defaultState(passedList);
        state.getScreens().add(mock(Screen.class));
        assertThat(passedList).hasSize(1);
        assertThat(state.getScreens()).hasSize(2);
    }

    @Test public void constructorDoesNotAllowNulScreens() {
        try {
            //noinspection ConstantConditions
            ScreenTestUtils.defaultState((List<Screen>) null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screens == null");
        }
    }

    @Test public void constructorDoesNotAllowEmptyScreens() {
        try {
            ScreenTestUtils.defaultState(Collections.<Screen>emptyList());
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("screens must contain at least one screen");
        }
    }

    @Test public void constructorRejectsNullInTheListOfScreens() {
        try {
            ScreenTestUtils.defaultState(Collections.<Screen>singletonList(null));
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screen == null");
        }
    }

    @Test public void constructorRejectsDuplicateScreens() {
        Screen screen = mock(Screen.class);
        try {
            ScreenTestUtils.defaultState(Arrays.asList(screen, screen));
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("screen already exists");
        }
    }

    @Test public void handlesPopIsFalseWhenNoPopListeners() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screen);
        assertThat(state.handlesPop(screen)).isFalse();
    }

    @Test public void handlesPopIsFalseWhenPopListenerReturnsFalse() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screen);
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(false);
        assertThat(state.handlesPop(screen)).isFalse();
    }

    @Test public void handlesPopIsTrueWhenPopListenerReturnsTrue() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screen);
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(true);
        assertThat(state.handlesPop(screen)).isTrue();
    }

    @Test public void handlesPopIsFalseForScreenNotMatchingPopListenerThatReturnsTrue() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screen);
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(true);
        assertThat(state.handlesPop(mock(Screen.class))).isFalse();
    }

    @Test public void handlesPopRemovesPopListenerWhenReturnsFalse() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screen);
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(false);
        assertThat(state.handlesPop(screen)).isFalse(); // Will be removed here.
        assertThat(state.handlesPop(screen)).isFalse(); // Will return false because it's gone.
        verify(popListener).onScreenPop(screen); // Will verify it was only used once, even though #handlesPop was called twice.
    }

    @Test public void handlesPopKeepsPopListenerWhenReturnsTrue() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screen);
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(true);
        assertThat(state.handlesPop(screen)).isTrue();
        assertThat(state.handlesPop(screen)).isTrue();
        verify(popListener, times(2)).onScreenPop(screen);
    }

    @Test public void addScreenRejectsDuplicateScreen() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screen);
        try {
            state.addScreen(screen);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("screen already exists");
        }
    }

    @Test public void addScreenRejectsNullScreen() {
        ScreenSwitcherState state = ScreenTestUtils.defaultState(mock(Screen.class));
        try {
            state.addScreen(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screen == null");
        }
    }

    @Test public void indexOfRejectsNullScreen() {
        ScreenSwitcherState state = ScreenTestUtils.defaultState(mock(Screen.class));
        try {
            //noinspection ConstantConditions
            state.indexOf(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screen == null");
        }
    }

    @Test public void indexOfReturnsNegativeOneIfTheScreenDoesNotExist() {
        ScreenSwitcherState state = ScreenTestUtils.defaultState(mock(Screen.class));
        assertThat(state.indexOf(mock(Screen.class))).isEqualTo(-1);
    }

    @Test public void indexOfReturnsTheCorrectIndexOfAnExistingScreen() {
        Screen screen0 = mock(Screen.class);
        Screen screen1 = mock(Screen.class);
        Screen screen2 = mock(Screen.class);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(Arrays.asList(screen0, screen1, screen2));
        assertThat(state.indexOf(screen1)).isEqualTo(1);
    }

    @Test public void screenCountIsUpdatedWhenScreensAreAdded() {
        ScreenSwitcherState state = ScreenTestUtils.defaultState(Arrays.asList(mock(Screen.class), mock(Screen.class)));
        assertThat(state.screenCount()).isEqualTo(2);
        state.addScreen(mock(Screen.class));
        assertThat(state.screenCount()).isEqualTo(3);
    }
}

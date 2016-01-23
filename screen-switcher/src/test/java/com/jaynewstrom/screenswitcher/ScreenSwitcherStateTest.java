package com.jaynewstrom.screenswitcher;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ScreenSwitcherStateTest {

    @Test public void constructorMakesDefensiveCopyOfScreensPassedIn() {
        Screen screen = mock(Screen.class);
        List<Screen> passedList = Collections.singletonList(screen);
        ScreenSwitcherState state = new ScreenSwitcherState(passedList);
        state.getScreens().add(mock(Screen.class));
        assertThat(passedList).hasSize(1);
        assertThat(state.getScreens()).hasSize(2);
    }

    @Test public void constructorDoesNotAllowNulScreens() {
        try {
            //noinspection ConstantConditions
            new ScreenSwitcherState(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screens == null");
        }
    }

    @Test public void constructorDoesNotAllowEmptyScreens() {
        try {
            new ScreenSwitcherState(Collections.<Screen>emptyList());
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("screens must contain at least one screen");
        }
    }

    @Test public void constructorRejectsNullInTheListOfScreens() {
        try {
            new ScreenSwitcherState(Collections.<Screen>singletonList(null));
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screen == null");
        }
    }

    @Test public void constructorRejectsDuplicateScreens() {
        try {
            Screen screen = mock(Screen.class);
            new ScreenSwitcherState(Arrays.asList(screen, screen));
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("screen already exists");
        }
    }

    @Test public void handlesPopIsFalseWhenNoPopListeners() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        assertThat(state.handlesPop(screen)).isFalse();
    }

    @Test public void handlesPopIsFalseWhenPopListenerReturnsFalse() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(false);
        assertThat(state.handlesPop(screen)).isFalse();
    }

    @Test public void handlesPopIsTrueWhenPopListenerReturnsTrue() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(true);
        assertThat(state.handlesPop(screen)).isTrue();
    }

    @Test public void handlesPopIsFalseForScreenNotMatchingPopListenerThatReturnsTrue() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(true);
        assertThat(state.handlesPop(mock(Screen.class))).isFalse();
    }

    @Test public void handlesPopRemovesPopListenerWhenReturnsTrue() {
        Screen screen = mock(Screen.class);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        state.registerPopListener(screen, popListener);
        when(popListener.onScreenPop(screen)).thenReturn(true);
        assertThat(state.handlesPop(screen)).isTrue();
        assertThat(state.handlesPop(screen)).isFalse(); // False because it doesn't exist.
    }

    @Test public void addScreenRejectsDuplicateScreen() {
        try {
            Screen screen = mock(Screen.class);
            ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
            state.addScreen(screen);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("screen already exists");
        }
    }

    @Test public void addScreenRejectsNullScreen() {
        try {
            Screen screen = mock(Screen.class);
            ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
            state.addScreen(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screen == null");
        }
    }
}

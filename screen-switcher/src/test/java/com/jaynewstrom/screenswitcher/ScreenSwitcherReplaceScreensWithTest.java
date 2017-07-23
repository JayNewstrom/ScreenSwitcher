package com.jaynewstrom.screenswitcher;

import android.app.Activity;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import static com.jaynewstrom.screenswitcher.ScreenTestUtils.addTransitionIn;
import static com.jaynewstrom.screenswitcher.ScreenTestUtils.initialActivityScreenSwitcher;
import static com.jaynewstrom.screenswitcher.ScreenTestUtils.mockCreateView;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public final class ScreenSwitcherReplaceScreensWithTest {

    @Test public void failsWhenTransitionIsOccurring() {
        RealScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        activityScreenSwitcher.setTransitioning(true);
        try {
            activityScreenSwitcher.replaceScreensWith(1, Collections.singletonList(mock(Screen.class)));
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessage("Can't replaceScreensWith while a transition is occurring");
        }
    }

    @Test public void failsWhenNumberToPopIsLessThanOne() {
        RealScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            //noinspection Range
            activityScreenSwitcher.replaceScreensWith(0, Collections.singletonList(mock(Screen.class)));
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("numberToPop < 1");
        }
    }

    @Test public void failsWhenPoppingMoreThanTheNumberOfScreens() {
        RealScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            activityScreenSwitcher.replaceScreensWith(3, Collections.singletonList(mock(Screen.class)));
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("2 screens exists, can't pop 3 screens.");
        }
    }

    @Test public void failsWhenScreensIsNull() {
        RealScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            //noinspection ConstantConditions
            activityScreenSwitcher.replaceScreensWith(1, null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screens == null");
        }
    }

    @Test public void failsWhenScreensIsEmpty() {
        RealScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            activityScreenSwitcher.replaceScreensWith(1, Collections.<Screen>emptyList());
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("screens must contain at least one screen");
        }
    }

    @Test public void failsWhenAScreenPassedInIsNull() {
        RealScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            activityScreenSwitcher.replaceScreensWith(1, Collections.<Screen>singletonList(null));
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screen == null");
        }
    }

    @Test public void isTransitioningWhenReplacing() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(screen2);
        ScreenSwitcherState state = ScreenTestUtils.defaultState(Arrays.asList(screen1, screen2));
        ScreenSwitcher activityScreenSwitcher = ScreenTestUtils.testScreenSwitcher(activity, state);
        assertThat(activityScreenSwitcher.isTransitioning()).isFalse();
        Screen newScreen = mock(Screen.class);
        mockCreateView(newScreen);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionIn(newScreen);
        activityScreenSwitcher.replaceScreensWith(1, Collections.singletonList(newScreen));
        assertThat(activityScreenSwitcher.isTransitioning()).isTrue();
        transitionCompletedRunnable.get().run();
        assertThat(activityScreenSwitcher.isTransitioning()).isFalse();
    }
}

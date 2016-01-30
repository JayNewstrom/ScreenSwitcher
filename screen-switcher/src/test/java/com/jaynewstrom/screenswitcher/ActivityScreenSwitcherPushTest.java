package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.view.View;

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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public final class ActivityScreenSwitcherPushTest {

    @Test public void pushFailsWhenTransitionIsOccurring() {
        ActivityScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        activityScreenSwitcher.setTransitioning(true);
        try {
            activityScreenSwitcher.push(mock(Screen.class));
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessage("Can't push while a transition is occurring");
        }
    }

    @Test public void pushFailsWhenScreenIsNull() {
        ActivityScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            //noinspection ConstantConditions
            activityScreenSwitcher.push(null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("screen == null");
        }
    }

    @Test public void pushFailsWhenNoScreensExist() {
        Screen screen = mock(Screen.class);
        Activity activity = mock(Activity.class);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        state.getScreens().remove(0);
        try {
            activityScreenSwitcher.push(mock(Screen.class));
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessage("no screens to transition from");
        }
    }

    @Test public void backgroundIsVisibleDuringPush() {
        Activity activity = mock(Activity.class);
        Screen initialScreen = mock(Screen.class);
        View initialView = mockCreateView(activity, initialScreen);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(initialScreen));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        Screen secondScreen = mock(Screen.class);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionIn(secondScreen);
        activityScreenSwitcher.push(secondScreen);
        verify(secondScreen).transition();
        verify(initialView, never()).setVisibility(View.GONE);
        transitionCompletedRunnable.get().run();
        verify(initialView).setVisibility(View.GONE);
    }

    @Test public void pushAddsNewScreenAfterCurrentScreen() {
        Activity activity = mock(Activity.class);
        Screen screen = mock(Screen.class);
        mockCreateView(activity, screen);
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        Screen pushedScreen = mock(Screen.class);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionIn(pushedScreen);
        activityScreenSwitcher.push(pushedScreen);
        transitionCompletedRunnable.get().run();
        assertThat(state.getScreens().indexOf(screen)).isEqualTo(0);
        assertThat(state.getScreens().indexOf(pushedScreen)).isEqualTo(1);
    }

    @Test public void isTransitioningWhenPushing() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        assertThat(activityScreenSwitcher.isTransitioning()).isFalse();
        Screen newScreen = mock(Screen.class);
        mockCreateView(activity, newScreen);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionIn(newScreen);
        activityScreenSwitcher.push(newScreen);
        assertThat(activityScreenSwitcher.isTransitioning()).isTrue();
        transitionCompletedRunnable.get().run();
        assertThat(activityScreenSwitcher.isTransitioning()).isFalse();
    }
}

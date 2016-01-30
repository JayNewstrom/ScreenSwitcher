package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static com.jaynewstrom.screenswitcher.ScreenTestUtils.addTransitionOut;
import static com.jaynewstrom.screenswitcher.ScreenTestUtils.initialActivityScreenSwitcher;
import static com.jaynewstrom.screenswitcher.ScreenTestUtils.mockCreateView;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class ActivityScreenSwitcherPopTest {

    @Test public void popFailsWhenTransitionIsOccurring() {
        ActivityScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        activityScreenSwitcher.setTransitioning(true);
        try {
            activityScreenSwitcher.pop(1);
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected).hasMessage("Can't pop while a transition is occurring");
        }
    }

    @Test public void popFailsWhenNumberToPopIsLessThanOne() {
        ActivityScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            //noinspection Range
            activityScreenSwitcher.pop(0);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("numberToPop < 1");
        }
    }

    @Test public void popFailsWhenPoppingMoreThanTheNumberOfScreens() {
        ActivityScreenSwitcher activityScreenSwitcher = initialActivityScreenSwitcher();
        try {
            activityScreenSwitcher.pop(3);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("2 screens exists, can't pop 3 screens.");
        }
    }

    @Test public void doesNotPopWhenTheTopScreenHandlesPopIsTrue() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2));
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        when(popListener.onScreenPop(screen2)).thenReturn(true);
        state.registerPopListener(screen2, popListener);
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        activityScreenSwitcher.pop(1);
        verify(popListener).onScreenPop(screen2);
        assertThat(state.getScreens().size()).isEqualTo(2);
    }

    @Test public void onlyPopsUntilHandlesPopIsTrue() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionOut(screen2);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2));
        ScreenPopListener popListener = mock(ScreenPopListener.class);
        when(popListener.onScreenPop(screen1)).thenReturn(true);
        state.registerPopListener(screen1, popListener);
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        activityScreenSwitcher.pop(2);
        transitionCompletedRunnable.get().run();
        verify(popListener).onScreenPop(screen1);
        assertThat(state.getScreens().size()).isEqualTo(1);
    }

    @Test public void whenPoppingMultipleScreensEnsureScreensAreRemovedImmediately() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        Screen screen3 = mock(Screen.class);
        mockCreateView(activity, screen3);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionOut(screen3);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2, screen3));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        activityScreenSwitcher.pop(2);
        assertThat(state.getScreens().size()).isEqualTo(1);
        transitionCompletedRunnable.get().run();
        assertThat(state.getScreens().size()).isEqualTo(1);
    }

    @Test public void whenTheScreenIsRemovedEnsureDestroyScreenIsCalled() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionOut(screen2);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        activityScreenSwitcher.pop(1);
        transitionCompletedRunnable.get().run();
        verify(screen1, never()).destroyScreen(any(View.class));
        verify(screen2).destroyScreen(any(View.class));
    }

    @Test public void whenTheScreenIsRemovedEnsureItIsRemovedFromItsParentView() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        View view2 = mockCreateView(activity, screen2);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionOut(screen2);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        activityScreenSwitcher.pop(1);
        verify((ViewGroup) view2.getParent(), never()).removeView(view2);
        transitionCompletedRunnable.get().run();
        verify((ViewGroup) view2.getParent()).removeView(view2);
    }

    @Test public void popMakesBackgroundVisibleDuringTransition() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        View view1 = mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionOut(screen2);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        verify(view1, never()).setVisibility(View.VISIBLE);
        activityScreenSwitcher.pop(1);
        verify(view1).setVisibility(View.VISIBLE);
        transitionCompletedRunnable.get().run();
    }

    @Test public void isTransitioningWhenPopping() {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransitionOut(screen2);
        ScreenSwitcherState state = new ScreenSwitcherState(Arrays.asList(screen1, screen2));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        assertThat(activityScreenSwitcher.isTransitioning()).isFalse();
        activityScreenSwitcher.pop(1);
        assertThat(activityScreenSwitcher.isTransitioning()).isTrue();
        transitionCompletedRunnable.get().run();
        assertThat(activityScreenSwitcher.isTransitioning()).isFalse();
    }
}

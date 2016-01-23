package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class ActivityScreenSwitcherPushTest {

    @Test public void transitionIsNotCalledWhenCreatingScreenSwitcher() {
        Screen screen = mock(Screen.class);
        Activity activity = mock(Activity.class);
        new ActivityScreenSwitcher(activity, new ScreenSwitcherState(Collections.singletonList(screen)));
        verify(screen).createView(activity);
        verify(screen, never()).transition();
    }

    @Test public void allButTopScreenIsGoneWhenCreatingScreenSwitcher() {
        Activity activity = mock(Activity.class);
        Screen bottomScreen = mock(Screen.class);
        View bottomView = mock(View.class);
        when(bottomScreen.createView(activity)).thenReturn(bottomView);
        Screen topScreen = mock(Screen.class);
        View topView = mock(View.class);
        when(topScreen.createView(activity)).thenReturn(topView);
        new ActivityScreenSwitcher(activity, new ScreenSwitcherState(Arrays.asList(bottomScreen, topScreen)));
        verify(bottomScreen).createView(activity);
        verify(topScreen).createView(activity);
        verify(bottomView).setVisibility(View.GONE);
    }

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
        View initialView = mock(View.class);
        when(initialScreen.createView(activity)).thenReturn(initialView);
        when(initialScreen.transition()).thenThrow(new AssertionError("Transition shouldn't be called for the initial screen."));
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(initialScreen));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        Screen secondScreen = mock(Screen.class);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransition(activity, secondScreen);
        activityScreenSwitcher.push(secondScreen);
        verify(secondScreen).transition();
        verify(initialView, never()).setVisibility(View.GONE);
        transitionCompletedRunnable.get().run();
        verify(initialView).setVisibility(View.GONE);
    }

    @Test public void pushAddsNewScreenAfterCurrentScreen() {
        Activity activity = mock(Activity.class);
        Screen screen = mock(Screen.class);
        when(screen.createView(activity)).thenReturn(mock(View.class));
        ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(screen));
        ActivityScreenSwitcher activityScreenSwitcher = new ActivityScreenSwitcher(activity, state);
        Screen pushedScreen = mock(Screen.class);
        AtomicReference<Runnable> transitionCompletedRunnable = addTransition(activity, pushedScreen);
        activityScreenSwitcher.push(pushedScreen);
        transitionCompletedRunnable.get().run();
        assertThat(state.getScreens().indexOf(screen)).isEqualTo(0);
        assertThat(state.getScreens().indexOf(pushedScreen)).isEqualTo(1);
    }

    private static ActivityScreenSwitcher initialActivityScreenSwitcher() {
        Screen screen = mock(Screen.class);
        Activity activity = mock(Activity.class);
        return new ActivityScreenSwitcher(activity, new ScreenSwitcherState(Collections.singletonList(screen)));
    }

    private static AtomicReference<Runnable> addTransition(Context context, Screen screenToTransition) {
        final AtomicReference<Runnable> transitionCompletedRunnable = new AtomicReference<>();
        ScreenTransition secondScreenTransition = new ScreenTransition() {
            @Override
            public void transitionIn(@NonNull View foregroundView, @NonNull View backgroundView,
                    @NonNull Runnable onTransitionCompleted) {
                transitionCompletedRunnable.getAndSet(onTransitionCompleted);
            }

            @Override
            public void transitionOut(@NonNull View foregroundView, @NonNull View backgroundView,
                    @NonNull Runnable onTransitionCompleted) {
                fail();
            }
        };
        when(screenToTransition.createView(context)).thenReturn(mock(View.class));
        when(screenToTransition.transition()).thenReturn(secondScreenTransition);
        return transitionCompletedRunnable;
    }
}

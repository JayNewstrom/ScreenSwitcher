package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ScreenTestUtils {

    private ScreenTestUtils() {
    }

    static ScreenSwitcherState defaultState(Screen screen) {
        return defaultState(Collections.singletonList(screen));
    }

    static ScreenSwitcherState defaultState(List<Screen> screens) {
        return new ScreenSwitcherState(new AssertingScreenLifecycleListener(), screens);
    }

    static RealScreenSwitcher initialActivityScreenSwitcher() {
        return initialActivityScreenSwitcher(null, -1);
    }

    static RealScreenSwitcher initialActivityScreenSwitcher(@Nullable Screen extraScreen, int extraScreenIndex) {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(screen2);
        List<Screen> screens = new ArrayList<>(Arrays.asList(screen1, screen2));
        if (extraScreen != null) {
            mockCreateView(extraScreen);
            screens.add(extraScreenIndex, extraScreen);
        }
        when(activity.findViewById(android.R.id.content)).thenReturn(mock(ViewGroup.class));
        ScreenSwitcherState state = ScreenTestUtils.defaultState(screens);
        ScreenSwitcherPopHandler popHandler = mock(ScreenSwitcherPopHandler.class);
        return (RealScreenSwitcher) ScreenSwitcherFactory.activityScreenSwitcher(activity, state, popHandler);
    }

    static View mockCreateView(Screen screen) {
        View view = mock(View.class);
        when(screen.createView(any(Context.class), any(ViewGroup.class))).thenReturn(view);
        return view;
    }

    static AtomicReference<Runnable> addTransitionOut(Screen screen) {
        final AtomicReference<Runnable> transitionCompletedRunnable = new AtomicReference<>();
        ScreenTransition secondScreenTransition = new ScreenTransition() {
            @Override public void transitionIn(View foregroundView, View backgroundView, Runnable onTransitionCompleted) {
                fail();
            }

            @Override public void transitionOut(View foregroundView, View backgroundView, Runnable onTransitionCompleted) {
                transitionCompletedRunnable.getAndSet(onTransitionCompleted);
            }
        };
        when(screen.transition()).thenReturn(secondScreenTransition);
        return transitionCompletedRunnable;
    }

    static AtomicReference<Runnable> addTransitionIn(Screen screenToTransition) {
        final AtomicReference<Runnable> transitionCompletedRunnable = new AtomicReference<>();
        ScreenTransition secondScreenTransition = new ScreenTransition() {
            @Override public void transitionIn(View foregroundView, View backgroundView, Runnable onTransitionCompleted) {
                transitionCompletedRunnable.getAndSet(onTransitionCompleted);
            }

            @Override public void transitionOut(View foregroundView, View backgroundView, Runnable onTransitionCompleted) {
                fail();
            }
        };
        when(screenToTransition.transition()).thenReturn(secondScreenTransition);
        return transitionCompletedRunnable;
    }

    static ScreenSwitcher testScreenSwitcher(Activity activity, ScreenSwitcherState state) {
        ScreenSwitcherPopHandler popHandler = mock(ScreenSwitcherPopHandler.class);
        return testScreenSwitcher(activity, state, popHandler);
    }

    static ScreenSwitcher testScreenSwitcher(Activity activity, ScreenSwitcherState state, ScreenSwitcherPopHandler popHandler) {
        ViewGroup hostView = mock(ViewGroup.class);
        when(hostView.getContext()).thenReturn(activity);
        when(activity.findViewById(android.R.id.content)).thenReturn(hostView);
        doAnswer(new Answer() {
            @Override public Object answer(InvocationOnMock invocation) throws Throwable {
                View childView = invocation.getArgumentAt(0, View.class);
                when(childView.getParent()).thenReturn((ViewGroup) invocation.getMock());
                return null;
            }
        }).when(hostView).addView(any(View.class));
        return ScreenSwitcherFactory.viewScreenSwitcher(hostView, state, popHandler);
    }
}

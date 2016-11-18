package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class ScreenTestUtils {

    private ScreenTestUtils() {
    }

    static RealScreenSwitcher initialActivityScreenSwitcher() {
        return initialActivityScreenSwitcher(null, -1);
    }

    static RealScreenSwitcher initialActivityScreenSwitcher(@Nullable Screen extraScreen, int extraScreenIndex) {
        Activity activity = mock(Activity.class);
        Screen screen1 = mock(Screen.class);
        mockCreateView(activity, screen1);
        Screen screen2 = mock(Screen.class);
        mockCreateView(activity, screen2);
        List<Screen> screens = new ArrayList<>(Arrays.asList(screen1, screen2));
        if (extraScreen != null) {
            mockCreateView(activity, extraScreen);
            screens.add(extraScreenIndex, extraScreen);
        }
        return (RealScreenSwitcher) ScreenSwitcherFactory.activityScreenSwitcher(activity, new ScreenSwitcherState(screens));
    }

    static View mockCreateView(Context context, Screen screen) {
        View view = mock(View.class);
        ViewGroup viewParent = mock(FrameLayout.class);
        when(view.getParent()).thenReturn(viewParent);
        when(screen.createView(context)).thenReturn(view);
        return view;
    }

    static AtomicReference<Runnable> addTransitionOut(Screen screen) {
        final AtomicReference<Runnable> transitionCompletedRunnable = new AtomicReference<>();
        ScreenTransition secondScreenTransition = new ScreenTransition() {
            @Override
            public void transitionIn(@NonNull View foregroundView, @NonNull View backgroundView,
                    @NonNull Runnable onTransitionCompleted) {
                fail();
            }

            @Override
            public void transitionOut(@NonNull View foregroundView, @NonNull View backgroundView,
                    @NonNull Runnable onTransitionCompleted) {
                transitionCompletedRunnable.getAndSet(onTransitionCompleted);
            }
        };
        when(screen.transition()).thenReturn(secondScreenTransition);
        return transitionCompletedRunnable;
    }

    static AtomicReference<Runnable> addTransitionIn(Screen screenToTransition) {
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
        when(screenToTransition.transition()).thenReturn(secondScreenTransition);
        return transitionCompletedRunnable;
    }
}

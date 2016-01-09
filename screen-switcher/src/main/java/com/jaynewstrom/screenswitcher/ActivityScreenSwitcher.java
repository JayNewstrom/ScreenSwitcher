package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jaynewstrom.screenswitcher.Preconditions.check;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;

final class ActivityScreenSwitcher implements ScreenSwitcher {

    boolean transitioning;

    private final Activity activity;
    private final ScreenSwitcherState state;
    private final Map<Screen, View> screenViewMap;

    ActivityScreenSwitcher(Activity activity, ScreenSwitcherState state) {
        this.activity = checkNotNull(activity, "activity == null");
        this.state = checkNotNull(state, "state == null");
        this.screenViewMap = new LinkedHashMap<>();
        initializeActivityState();
    }

    private void initializeActivityState() {
        for (Screen screen : state.getScreens()) {
            createView(screen);
        }
    }

    private View createView(Screen screen) {
        View view = screen.createView(activity);
        screenViewMap.put(screen, view);
        activity.addContentView(view, new WindowManager.LayoutParams());
        return view;
    }

    @Override public void push(Screen screen) {
        ensureTransitionIsNotOccurring("push");
        checkNotNull(screen, "screen == null");
        hideKeyboard();
        state.getScreens().add(screen);
        View view = createView(screen);
        screen.animationConfiguration().animateIn(view, new EndTransitionRunnable());
    }

    @Override public void pop(int numberToPop) {
        ensureTransitionIsNotOccurring("pop");
        check(numberToPop >= 1, "numberToPop < 1");
        hideKeyboard();
        List<Screen> screens = state.getScreens();
        if (screens.size() > numberToPop) {
            for (int i = 1; i < numberToPop; i++) {
                new RemoveScreenRunnable(screens.get(screens.size() - i - 1), screenViewMap, state).run();
            }
            final Screen screenToRemove = screens.get(screens.size() - 1);
            View viewToRemove = screenViewMap.get(screenToRemove);
            Runnable completionRunnable = new RemoveScreenRunnable(screenToRemove, screenViewMap, state, new EndTransitionRunnable());
            screenToRemove.animationConfiguration().animateOut(viewToRemove, completionRunnable);
        } else {
            for (Screen screen : new ArrayList<>(screenViewMap.keySet())) {
                new RemoveScreenRunnable(screen, screenViewMap, state).run();
            }
            activity.finish();
        }
    }

    @Override public boolean isTransitioning() {
        return transitioning;
    }

    private void hideKeyboard() {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) currentFocus.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            currentFocus.clearFocus();
        }
    }

    private void ensureTransitionIsNotOccurring(String transitionType) {
        if (transitioning) {
            throw new IllegalStateException(String.format("Can't %s while a transition is occurring", transitionType));
        }
    }

    final class EndTransitionRunnable implements Runnable {

        EndTransitionRunnable() {
            transitioning = true;
        }

        @Override public void run() {
            transitioning = false;
        }
    }

    private static final class RemoveScreenRunnable implements Runnable {

        private final Screen screen;
        private final Map<Screen, View> screenViewMap;
        private final ScreenSwitcherState state;
        private final Runnable completionRunnable;

        RemoveScreenRunnable(Screen screen, Map<Screen, View> screenViewMap, ScreenSwitcherState state) {
            this(screen, screenViewMap, state, null);
        }

        RemoveScreenRunnable(Screen screen, Map<Screen, View> screenViewMap, ScreenSwitcherState state, Runnable completionRunnable) {
            this.screen = screen;
            this.screenViewMap = screenViewMap;
            this.state = state;
            this.completionRunnable = completionRunnable;
        }

        @Override public void run() {
            View view = screenViewMap.remove(screen);
            screen.destroyScreen(view);
            ((ViewGroup) view.getParent()).removeView(view);
            state.getScreens().remove(screen);
            if (completionRunnable != null) {
                completionRunnable.run();
            }
        }
    }
}

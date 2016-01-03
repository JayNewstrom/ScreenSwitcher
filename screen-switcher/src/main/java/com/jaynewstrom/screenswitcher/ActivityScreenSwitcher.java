package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jaynewstrom.screenswitcher.Preconditions.check;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;

final class ActivityScreenSwitcher implements ScreenSwitcher {

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
        checkNotNull(screen, "screen == null");
        hideKeyboard();
        state.getScreens().add(screen);
        View view = createView(screen);
        screen.animationConfiguration().animateIn(view, new Runnable() {
            @Override public void run() {
            }
        });
    }

    @Override public void pop(int numberToPop) {
        check(numberToPop >= 1, "numberToPop < 1");
        hideKeyboard();
        List<Screen> screens = state.getScreens();
        if (screens.size() > numberToPop) {
            for (int i = 1; i < numberToPop; i++) {
                new RemoveScreenRunnable(screens.get(screens.size() - i - 1), screenViewMap, state).run();
            }
            final Screen screenToRemove = screens.get(screens.size() - 1);
            View viewToRemove = screenViewMap.get(screenToRemove);
            Runnable removeScreenRunnable = new RemoveScreenRunnable(screenToRemove, screenViewMap, state);
            screenToRemove.animationConfiguration().animateOut(viewToRemove, removeScreenRunnable);
        } else {
            activity.finish();
        }
    }

    private void hideKeyboard() {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) currentFocus.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            currentFocus.clearFocus();
        }
    }

    private static final class RemoveScreenRunnable implements Runnable {

        private final Screen screen;
        private final Map<Screen, View> screenViewMap;
        private final ScreenSwitcherState state;

        RemoveScreenRunnable(Screen screen, Map<Screen, View> screenViewMap, ScreenSwitcherState state) {
            this.screen = screen;
            this.screenViewMap = screenViewMap;
            this.state = state;
        }

        @Override public void run() {
            View view = screenViewMap.remove(screen);
            screen.destroyScreen(view);
            ((ViewGroup) view.getParent()).removeView(view);
            state.getScreens().remove(screen);
        }
    }
}

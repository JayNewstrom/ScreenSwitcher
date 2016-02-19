package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jaynewstrom.screenswitcher.Preconditions.checkArgument;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;
import static com.jaynewstrom.screenswitcher.Preconditions.checkState;
import static com.jaynewstrom.screenswitcher.Utils.checkScreen;

final class ActivityScreenSwitcher implements ScreenSwitcher {

    private static final String TOO_MANY_SCREENS_ERROR_FORMAT = "%d screens exists, can't pop %d screens.";

    private boolean transitioning;

    private final Activity activity;
    private final ScreenSwitcherState state;
    private final Map<Screen, View> screenViewMap;

    ActivityScreenSwitcher(Activity activity, ScreenSwitcherState state) {
        this.activity = activity;
        this.state = state;
        this.screenViewMap = new LinkedHashMap<>();
        initializeActivityState();
    }

    private void initializeActivityState() {
        List<Screen> screens = state.getScreens();
        for (int i = 0, size = screens.size(); i < size; i++) {
            createView(screens.get(i));
        }
        hideAllButTopScreen();
    }

    private View createView(Screen screen) {
        View view = screen.createView(activity);
        screenViewMap.put(screen, view);
        activity.addContentView(view, new WindowManager.LayoutParams());
        return view;
    }

    @Override public void push(@NonNull Screen screen) {
        ensureTransitionIsNotOccurring("push");
        List<Screen> screens = state.getScreens();
        checkState(!screens.isEmpty(), "no screens to transition from");

        hideKeyboard();

        View backgroundView = screenViewMap.get(screens.get(screens.size() - 1));

        state.addScreen(screen);
        View view = createView(screen);

        screen.transition().transitionIn(view, backgroundView, new EndTransitionRunnable());
    }

    @Override public void pop(int numberToPop) {
        ensureTransitionIsNotOccurring("pop");
        checkNumberToPop(numberToPop);

        hideKeyboard();

        if (!popListenerConsumedPop(numberToPop)) {
            performPop(numberToPop);
        }
    }

    @Override public void replaceScreensWith(int numberToPop, @NonNull List<Screen> screens) {
        ensureTransitionIsNotOccurring("replaceScreensWith");
        checkNumberToPop(numberToPop);
        checkNotNull(screens, "screens == null");
        checkArgument(!screens.isEmpty(), "screens must contain at least one screen");
        screens = new ArrayList<>(screens); // Make defensive copy.
        for (int i = 0, size = screens.size(); i < size; i++) {
            checkScreen(state, screens.get(i));
        }

        hideKeyboard();

        if (!popListenerConsumedPop(numberToPop)) {
            List<Screen> screensToRemove = new ArrayList<>(numberToPop);
            for (int i = numberToPop; i > 0; i--) {
                screensToRemove.add(state.getScreens().get(state.getScreens().size() - i));
            }

            Screen backgroundScreen = state.getScreens().get(state.getScreens().size() - 1);
            View backgroundView = screenViewMap.get(backgroundScreen);

            for (int i = 0, size = screens.size(); i < size; i++) {
                Screen screen = screens.get(i);
                state.addScreen(screen);
                View view = createView(screen);
                view.setVisibility(View.GONE);
            }

            Screen topScreen = screens.get(screens.size() - 1);
            View topView = screenViewMap.get(topScreen);
            topView.setVisibility(View.VISIBLE);

            topScreen.transition().transitionIn(topView, backgroundView, new RemoveScreenRunnable(screensToRemove, state));
        }
    }

    @Override public boolean isTransitioning() {
        return transitioning;
    }

    private void hideKeyboard() {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            currentFocus.clearFocus();
        }
    }

    private void ensureTransitionIsNotOccurring(String transitionType) {
        checkState(!transitioning, String.format("Can't %s while a transition is occurring", transitionType));
    }

    private void checkNumberToPop(int numberToPop) {
        checkArgument(numberToPop >= 1, "numberToPop < 1");
        String tooManyScreensErrorMessage = String.format(TOO_MANY_SCREENS_ERROR_FORMAT, state.getScreens().size(), numberToPop);
        checkArgument(numberToPop <= state.getScreens().size(), tooManyScreensErrorMessage);
    }

    private boolean popListenerConsumedPop(int numberToPop) {
        List<Screen> screens = state.getScreens();
        for (int i = 1; i <= numberToPop; i++) {
            if (state.handlesPop(screens.get(screens.size() - i))) {
                if (i > 1) {
                    performPop(i - 1);
                }
                return true;
            }
        }
        return false;
    }

    private void performPop(int numberToPop) {
        List<Screen> screens = state.getScreens();
        if (screens.size() > numberToPop) {
            for (int i = 1; i < numberToPop; i++) {
                removeScreen(screens.get(screens.size() - i - 1));
            }
            performPopTransition(screens.get(screens.size() - 1), screens.get(screens.size() - 2));
        } else {
            View matchingView = Utils.createViewWithDrawMatching(activity.findViewById(android.R.id.content));
            activity.addContentView(matchingView, new WindowManager.LayoutParams());
            List<Screen> screensToRemove = new ArrayList<>(screenViewMap.keySet());
            for (int i = 0, size = screensToRemove.size(); i < size; i++) {
                removeScreen(screensToRemove.get(i));
            }
            activity.finish();
        }
    }

    private void performPopTransition(Screen screenToRemove, Screen screenToBecomeVisible) {
        View viewToBecomeVisible = screenViewMap.get(screenToBecomeVisible);
        viewToBecomeVisible.setVisibility(View.VISIBLE);
        View viewToRemove = screenViewMap.get(screenToRemove);
        Runnable completionRunnable = new RemoveScreenRunnable(screenToRemove, state);
        screenToRemove.transition().transitionOut(viewToRemove, viewToBecomeVisible, completionRunnable);
    }

    void setTransitioning(boolean transitioning) {
        this.transitioning = transitioning;
    }

    void hideAllButTopScreen() {
        List<Screen> screens = state.getScreens();
        for (int i = 0, max = screens.size() - 1; i < max; i++) {
            screenViewMap.get(screens.get(i)).setVisibility(View.GONE);
        }
    }

    void removeScreen(Screen screen) {
        View view = screenViewMap.remove(screen);
        screen.destroyScreen(view);
        ((ViewGroup) view.getParent()).removeView(view);
        state.getScreens().remove(screen);
    }

    private final class EndTransitionRunnable implements Runnable {

        EndTransitionRunnable() {
            setTransitioning(true);
        }

        @Override public void run() {
            setTransitioning(false);
            hideAllButTopScreen();
        }
    }

    private final class RemoveScreenRunnable implements Runnable {

        private List<Screen> screens;

        RemoveScreenRunnable(Screen screen, ScreenSwitcherState state) {
            this(Collections.singletonList(screen), state);
        }

        RemoveScreenRunnable(List<Screen> screens, ScreenSwitcherState state) {
            this.screens = screens;
            for (int i = 0, size = screens.size(); i < size; i++) {
                state.getScreens().remove(screens.get(i));
            }
            setTransitioning(true);
        }

        @Override public void run() {
            for (int i = 0, size = screens.size(); i < size; i++) {
                removeScreen(screens.get(i));
            }
            screens = null;
            setTransitioning(false);
            hideAllButTopScreen();
        }
    }
}

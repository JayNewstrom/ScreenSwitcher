package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.jaynewstrom.screenswitcher.Preconditions.checkArgument;
import static com.jaynewstrom.screenswitcher.Preconditions.checkNotNull;
import static com.jaynewstrom.screenswitcher.Preconditions.checkState;
import static com.jaynewstrom.screenswitcher.Utils.checkScreen;

final class RealScreenSwitcher implements ScreenSwitcher {

    private static final String TOO_MANY_SCREENS_ERROR_FORMAT = "%d screens exists, can't pop %d screens.";

    private boolean transitioning;
    private StackTraceElement[] transitioningStackTrace;

    private final Context context;
    private final ScreenSwitcherState state;
    private final ScreenSwitcherHost host;
    private final Activity activity;
    private final Map<Screen, View> screenViewMap;

    RealScreenSwitcher(Context context, ScreenSwitcherState state, ScreenSwitcherHost host) {
        this.context = context;
        this.state = state;
        this.host = host;
        this.activity = getActivity(context);
        this.screenViewMap = new LinkedHashMap<>();
        initializeActivityState();
        setupHostViewForHidingKeyboard();
    }

    private void initializeActivityState() {
        List<Screen> screens = state.getScreens();
        for (int i = 0, size = screens.size(); i < size; i++) {
            createView(screens.get(i));
        }
        state.lifecycleListener().onScreenBecameActive(screens.get(screens.size() - 1));
        hideAllButTopScreen();
    }

    private View createView(Screen screen) {
        View view = screen.createView(context, host.hostView());
        Preconditions.checkArgument(view.getParent() == null, "createView should not return a view that has a parent.");
        screenViewMap.put(screen, view);
        host.addView(view);
        return view;
    }

    private void setupHostViewForHidingKeyboard() {
        host.hostView().setFocusable(true);
        host.hostView().setFocusableInTouchMode(true);
    }

    @Override public void push(Screen screen) {
        ensureTransitionIsNotOccurring("push");
        List<Screen> screens = state.getScreens();
        checkState(!screens.isEmpty(), "no screens to transition from");

        hideKeyboard();

        Screen backgroundScreen = screens.get(screens.size() - 1);
        View backgroundView = screenViewMap.get(backgroundScreen);
        state.lifecycleListener().onScreenBecameInactive(backgroundScreen);

        state.addScreen(screen);
        View view = createView(screen);

        screen.transition().transitionIn(view, backgroundView, new EndTransitionRunnable());
        state.lifecycleListener().onScreenBecameActive(screen);
    }

    @Override public void pop(int numberToPop) {
        ensureTransitionIsNotOccurring("pop");
        checkNumberToPop(numberToPop);

        hideKeyboard();

        if (!popListenerConsumedPop(numberToPop)) {
            performPop(numberToPop);
        }
    }

    @Override public void replaceScreensWith(int numberToPop, List<Screen> screens) {
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

            state.lifecycleListener().onScreenBecameInactive(backgroundScreen);

            for (int i = 0, size = screens.size(); i < size; i++) {
                Screen screen = screens.get(i);
                state.addScreen(screen);
                View view = createView(screen);
                view.setVisibility(View.GONE);
            }

            Screen topScreen = screens.get(screens.size() - 1);
            View topView = screenViewMap.get(topScreen);
            topView.setVisibility(View.VISIBLE);
            state.lifecycleListener().onScreenBecameActive(topScreen);

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
            host.hostView().requestFocus();
        }
    }

    private static Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return getActivity(((ContextWrapper) context).getBaseContext());
        }
        throw new IllegalArgumentException("context is not an instance of Activity");
    }

    private void ensureTransitionIsNotOccurring(String transitionType) {
        if (!transitioning) {
            return;
        }
        String exceptionMessage = String.format("Can't %s while a transition is occurring.\nPrevious transition from: %s",
                transitionType,
                Arrays.toString(transitioningStackTrace)
        );
        throw new IllegalStateException(exceptionMessage);
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
                removeScreen(screens.get(screens.size() - 2), true);
            }
            performPopTransition(screens.get(screens.size() - 1), screens.get(screens.size() - 2));
        } else {
            state.lifecycleListener().onScreenBecameInactive(screens.get(screens.size() - 1));
            setTransitioning(true);
            host.onLastScreenPopped(new ScreenSwitcherPopHandler.PopCompleteHandler() {
                @Override public void popComplete() {
                    List<Screen> screensToRemove = new ArrayList<>(screenViewMap.keySet());
                    for (int i = 0, size = screensToRemove.size(); i < size; i++) {
                        removeScreen(screensToRemove.get(i), true);
                    }
                }
            });
        }
    }

    private void performPopTransition(Screen screenToRemove, Screen screenToBecomeVisible) {
        state.lifecycleListener().onScreenBecameInactive(screenToRemove);
        View viewToBecomeVisible = screenViewMap.get(screenToBecomeVisible);
        viewToBecomeVisible.setVisibility(View.VISIBLE);
        View viewToRemove = screenViewMap.get(screenToRemove);
        Runnable completionRunnable = new RemoveScreenRunnable(screenToRemove, state);
        screenToRemove.transition().transitionOut(viewToRemove, viewToBecomeVisible, completionRunnable);
        state.lifecycleListener().onScreenBecameActive(screenToBecomeVisible);
    }

    void setTransitioning(boolean transitioning) {
        this.transitioning = transitioning;

        if (transitioning) {
            transitioningStackTrace = new Throwable().getStackTrace();
        } else {
            transitioningStackTrace = null;
        }
    }

    void hideAllButTopScreen() {
        List<Screen> screens = state.getScreens();
        for (int i = 0, max = screens.size() - 1; i < max; i++) {
            screenViewMap.get(screens.get(i)).setVisibility(View.GONE);
        }
    }

    void removeScreen(Screen screen, boolean removeFromState) {
        View view = screenViewMap.remove(screen);
        screen.destroyScreen(view);
        ((ViewGroup) view.getParent()).removeView(view);
        if (removeFromState) {
            state.removeScreen(screen);
        }
    }

    private final class EndTransitionRunnable implements Runnable {
        private boolean executed;

        EndTransitionRunnable() {
            setTransitioning(true);
        }

        @Override public void run() {
            if (executed) {
                throw new IllegalStateException("Transition complete runnable already executed.");
            } else {
                executed = true;
            }
            setTransitioning(false);
            hideAllButTopScreen();
        }
    }

    private final class RemoveScreenRunnable implements Runnable {
        private boolean executed;
        private List<Screen> screens;

        RemoveScreenRunnable(Screen screen, ScreenSwitcherState state) {
            this(Collections.singletonList(screen), state);
        }

        RemoveScreenRunnable(List<Screen> screens, ScreenSwitcherState state) {
            this.screens = screens;
            for (int i = 0, size = screens.size(); i < size; i++) {
                state.removeScreen(screens.get(i));
            }
            setTransitioning(true);
        }

        @Override public void run() {
            if (executed) {
                throw new IllegalStateException("RemoveScreenRunnable already executed.");
            } else {
                executed = true;
            }

            for (int i = 0, size = screens.size(); i < size; i++) {
                removeScreen(screens.get(i), false);
            }
            screens = null;
            setTransitioning(false);
            hideAllButTopScreen();
        }
    }
}

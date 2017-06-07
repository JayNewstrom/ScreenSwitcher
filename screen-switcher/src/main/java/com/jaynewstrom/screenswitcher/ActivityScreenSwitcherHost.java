package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

final class ActivityScreenSwitcherHost implements ScreenSwitcherHost {
    private final Activity activity;
    private final ScreenSwitcherPopHandler popHandler;

    ActivityScreenSwitcherHost(Activity activity, ScreenSwitcherPopHandler popHandler) {
        this.activity = activity;
        this.popHandler = popHandler;
    }

    @Override public void onLastScreenPopped(ScreenSwitcherPopHandler.PopCompleteHandler popCompleteHandler) {
        popHandler.onLastScreenPopped(popCompleteHandler);
        activity.finish();
    }

    @Override public void addView(View view) {
        activity.addContentView(view, new WindowManager.LayoutParams());
    }

    @Override public ViewGroup hostView() {
        return (ViewGroup) activity.findViewById(android.R.id.content);
    }
}

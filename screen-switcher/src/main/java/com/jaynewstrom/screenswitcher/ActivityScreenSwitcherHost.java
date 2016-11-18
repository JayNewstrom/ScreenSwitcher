package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

final class ActivityScreenSwitcherHost implements ScreenSwitcherHost {
    private final Activity activity;

    ActivityScreenSwitcherHost(Activity activity) {
        this.activity = activity;
    }

    @Override public void onLastScreenPopped() {
        activity.finish();
    }

    @Override public void addView(View view) {
        activity.addContentView(view, new WindowManager.LayoutParams());
    }

    @Override public View hostView() {
        return activity.findViewById(android.R.id.content);
    }
}

package com.jaynewstrom.screenswitcher;

import android.view.View;
import android.view.ViewGroup;

final class ViewScreenSwitcherHost implements ScreenSwitcherHost {
    private final ViewGroup viewGroup;
    private final ViewScreenSwitcherHandler handler;

    ViewScreenSwitcherHost(ViewGroup viewGroup, ViewScreenSwitcherHandler handler) {
        this.viewGroup = viewGroup;
        this.handler = handler;
    }

    @Override public void onLastScreenPopped() {
        handler.onLastScreenPopped();
    }

    @Override public void addView(View view) {
        viewGroup.addView(view);
    }

    @Override public View hostView() {
        return viewGroup;
    }
}

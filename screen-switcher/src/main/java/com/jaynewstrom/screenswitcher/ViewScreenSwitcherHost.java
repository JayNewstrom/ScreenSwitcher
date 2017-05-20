package com.jaynewstrom.screenswitcher;

import android.view.View;
import android.view.ViewGroup;

final class ViewScreenSwitcherHost implements ScreenSwitcherHost {
    private final ViewGroup viewGroup;
    private final ScreenSwitcherPopHandler popHandler;

    ViewScreenSwitcherHost(ViewGroup viewGroup, ScreenSwitcherPopHandler popHandler) {
        this.viewGroup = viewGroup;
        this.popHandler = popHandler;
    }

    @Override public void onLastScreenPopped(ScreenSwitcherPopHandler.PopCompleteHandler popCompleteHandler) {
        popHandler.onLastScreenPopped(popCompleteHandler);
    }

    @Override public void addView(View view) {
        viewGroup.addView(view);
    }

    @Override public View hostView() {
        return viewGroup;
    }
}

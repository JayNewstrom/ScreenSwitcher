package com.jaynewstrom.screenswitcher;

import android.view.View;
import android.view.ViewGroup;

interface ScreenSwitcherHost {
    void onLastScreenPopped(ScreenSwitcherPopHandler.PopCompleteHandler popCompleteHandler);

    void addView(View view);

    ViewGroup hostView();
}

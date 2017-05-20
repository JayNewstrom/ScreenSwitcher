package com.jaynewstrom.screenswitcher;

import android.view.View;

interface ScreenSwitcherHost {
    void onLastScreenPopped(ScreenSwitcherPopHandler.PopCompleteHandler popCompleteHandler);

    void addView(View view);

    View hostView();
}

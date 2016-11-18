package com.jaynewstrom.screenswitcher;

import android.view.View;

interface ScreenSwitcherHost {
    void onLastScreenPopped();

    void addView(View view);

    View hostView();
}

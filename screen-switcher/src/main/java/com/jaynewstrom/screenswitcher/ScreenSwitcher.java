package com.jaynewstrom.screenswitcher;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public interface ScreenSwitcher {

    void push(@NonNull Screen screen);

    void pop(@IntRange(from = 1) int numberToPop);

    boolean isTransitioning();
}

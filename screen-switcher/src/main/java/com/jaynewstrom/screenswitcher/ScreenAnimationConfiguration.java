package com.jaynewstrom.screenswitcher;

import android.view.View;

public interface ScreenAnimationConfiguration {

    void animateIn(View viewToAnimate, Runnable onAnimationCompleted);

    void animateOut(View viewToAnimate, Runnable onAnimationCompleted);
}

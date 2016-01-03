package com.jaynewstrom.screenswitchersample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.jaynewstrom.screenswitcher.ScreenAnimationConfiguration;

public final class DefaultAnimationConfiguration implements ScreenAnimationConfiguration {

    @Override public void animateIn(View viewToAnimate, final Runnable onAnimationCompleted) {
        viewToAnimate.setX(((View) viewToAnimate.getParent()).getMeasuredWidth());
        viewToAnimate.animate()
                     .x(0)
                     .setDuration(300)
                     .setListener(new AnimatorListenerAdapter() {
                         @Override public void onAnimationEnd(Animator animation) {
                             onAnimationCompleted.run();
                         }
                     });
    }

    @Override public void animateOut(View viewToAnimate, final Runnable onAnimationCompleted) {
        viewToAnimate.animate()
                     .x(((View) viewToAnimate.getParent()).getMeasuredWidth())
                     .setDuration(300)
                     .setListener(new AnimatorListenerAdapter() {
                         @Override public void onAnimationEnd(Animator animation) {
                             onAnimationCompleted.run();
                         }
                     });
    }
}

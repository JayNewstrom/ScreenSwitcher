package com.jaynewstrom.screenswitcher;

import android.support.annotation.NonNull;
import android.view.View;

public interface ScreenTransition {

    void transitionIn(@NonNull View foregroundView, @NonNull View backgroundView, @NonNull Runnable onTransitionCompleted);

    void transitionOut(@NonNull View foregroundView, @NonNull View backgroundView, @NonNull Runnable onTransitionCompleted);
}

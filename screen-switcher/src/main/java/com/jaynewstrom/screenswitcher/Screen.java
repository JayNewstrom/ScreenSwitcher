package com.jaynewstrom.screenswitcher;

import android.content.Context;
import android.view.View;

public interface Screen {

    View createView(Context context);

    void destroyScreen(View viewToDestroy);

    ScreenAnimationConfiguration animationConfiguration();
}

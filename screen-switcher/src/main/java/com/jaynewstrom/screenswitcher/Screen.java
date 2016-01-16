package com.jaynewstrom.screenswitcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

public interface Screen {

    View createView(@NonNull Context context);

    void destroyScreen(@NonNull View viewToDestroy);

    ScreenTransition transition();
}

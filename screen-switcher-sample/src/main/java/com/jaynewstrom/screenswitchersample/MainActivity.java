package com.jaynewstrom.screenswitchersample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.jaynewstrom.screenswitcher.ScreenSwitcher;
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory;
import com.jaynewstrom.screenswitcher.ScreenSwitcherState;

import javax.inject.Inject;

public final class MainActivity extends Activity {

    private static MainActivityComponent mainActivityComponent;

    @Inject ScreenSwitcherState screenSwitcherState;
    @Inject ScreenManager screenManager;

    private ScreenSwitcher activityScreenSwitcher;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mainActivityComponent == null) {
            mainActivityComponent = createMainActivityComponent();
        }
        mainActivityComponent.inject(this);
        activityScreenSwitcher = ScreenSwitcherFactory.activityScreenSwitcher(this, screenSwitcherState);
        screenManager.take(activityScreenSwitcher);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        screenManager.drop(activityScreenSwitcher);
        if (isFinishing()) {
            mainActivityComponent = null;
        }
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        return activityScreenSwitcher.isTransitioning() || super.dispatchTouchEvent(ev);
    }

    @Override public void onBackPressed() {
        if (!activityScreenSwitcher.isTransitioning()) {
            screenManager.pop();
        }
    }

    @Override public Object getSystemService(@NonNull String name) {
        if (MainActivityComponent.SCOPE_NAME.equals(name)) {
            return mainActivityComponent;
        }
        return super.getSystemService(name);
    }

    private MainActivityComponent createMainActivityComponent() {
        //noinspection WrongConstant
        ApplicationComponent applicationComponent = (ApplicationComponent) getApplicationContext().getSystemService
                (ApplicationComponent.SCOPE_NAME);
        return DaggerMainActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .mainActivityModule(new MainActivityModule())
                .build();
    }
}

package com.jaynewstrom.screenswitchersample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.screenswitcher.ScreenSwitcher;
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory;
import com.jaynewstrom.screenswitcher.ScreenSwitcherState;

import javax.inject.Inject;

public final class MainActivity extends Activity {

    @Inject ScreenSwitcherState screenSwitcherState;
    @Inject ScreenManager screenManager;

    private ScreenSwitcher activityScreenSwitcher;
    private ConcreteWall<MainActivityComponent> activityWall;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConcreteWall<ApplicationComponent> foundation = Concrete.findWall(getApplicationContext());
        activityWall = foundation.stack(new MainActivityBlock(foundation.getComponent()));
        activityWall.getComponent().inject(this);
        activityScreenSwitcher = ScreenSwitcherFactory.activityScreenSwitcher(this, screenSwitcherState);
        screenManager.take(activityScreenSwitcher);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        screenManager.drop(activityScreenSwitcher);
        if (isFinishing()) {
            activityWall.destroy();
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
        if (Concrete.isService(name)) {
            return activityWall;
        }
        return super.getSystemService(name);
    }
}

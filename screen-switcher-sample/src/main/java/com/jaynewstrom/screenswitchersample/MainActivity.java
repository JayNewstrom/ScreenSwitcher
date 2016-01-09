package com.jaynewstrom.screenswitchersample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.screenswitcher.ScreenSwitcher;
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory;
import com.jaynewstrom.screenswitcher.ScreenSwitcherState;

import javax.inject.Inject;

public final class MainActivity extends Activity {

    @Inject ScreenSwitcherState screenSwitcherState;
    @Inject ScreenManager screenManager;

    private ConcreteWall activityConcreteWall;
    private ScreenSwitcher activityScreenSwitcher;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityConcreteWall = Concrete.findWall(getApplicationContext()).stack(new MainActivityBlock());
        Concrete.inject(this, this);
        activityScreenSwitcher = ScreenSwitcherFactory.activityScreenSwitcher(this, screenSwitcherState);
        screenManager.take(activityScreenSwitcher);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        screenManager.drop(activityScreenSwitcher);
        if (isFinishing()) {
            activityConcreteWall.destroy();
        }
    }

    @Override public void onBackPressed() {
        if (!activityScreenSwitcher.isTransitioning()) {
            screenManager.pop();
        }
    }

    @Override public Object getSystemService(@NonNull String name) {
        if (Concrete.isService(name)) {
            return activityConcreteWall;
        }
        return super.getSystemService(name);
    }
}

package com.jaynewstrom.screenswitchersample;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concrete.ConcreteWall;
import com.jaynewstrom.screenswitcher.ScreenSwitcher;
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory;
import com.jaynewstrom.screenswitcher.ScreenSwitcherPopHandler;
import com.jaynewstrom.screenswitcher.ScreenSwitcherState;
import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

import javax.annotation.Nullable;
import javax.inject.Inject;

public final class MainActivity extends Activity implements ScreenSwitcherPopHandler {

    @Inject ScreenSwitcherState screenSwitcherState;
    @Inject ScreenManager screenManager;
    @Inject DialogHub dialogHub;

    private ScreenSwitcher activityScreenSwitcher;
    private ConcreteWall<MainActivityComponent> activityWall;
    private PopCompleteHandler popCompleteHandler;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConcreteWall<ApplicationComponent> foundation = Concrete.findWall(getApplicationContext());
        activityWall = foundation.stack(new MainActivityBlock(foundation.getComponent()));
        activityWall.getComponent().inject(this);
        activityScreenSwitcher = ScreenSwitcherFactory.activityScreenSwitcher(this, screenSwitcherState, this);
        screenManager.take(activityScreenSwitcher);
        dialogHub.attachActivity(this);
        dialogHub.restoreState();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        screenManager.drop(activityScreenSwitcher);
        dialogHub.dropActivity(this);
        if (popCompleteHandler != null) {
            popCompleteHandler.popComplete();
        }
        if (isFinishing()) {
            activityWall.destroy();
        } else {
            dialogHub.saveState();
        }
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        return activityScreenSwitcher.isTransitioning() || super.dispatchTouchEvent(ev);
    }

    @Override public void onBackPressed() {
        if (!activityScreenSwitcher.isTransitioning() && !isFinishing()) {
            screenManager.pop();
        }
    }

    @Override public Object getSystemService(String name) {
        if (Concrete.isService(name)) {
            return activityWall;
        }
        return super.getSystemService(name);
    }

    @Override public void onLastScreenPopped(PopCompleteHandler popCompleteHandler) {
        this.popCompleteHandler = popCompleteHandler;
    }
}

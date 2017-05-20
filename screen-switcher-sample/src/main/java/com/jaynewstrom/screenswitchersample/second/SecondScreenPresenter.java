package com.jaynewstrom.screenswitchersample.second;

import com.jaynewstrom.screenswitcher.Screen;
import com.jaynewstrom.screenswitcher.ScreenPopListener;
import com.jaynewstrom.screenswitchersample.ScreenManager;

import javax.inject.Inject;

@ForSecondScreen
final class SecondScreenPresenter implements ScreenPopListener {

    private boolean hasConfirmedPop;

    private SecondView view;

    @Inject SecondScreenPresenter(SecondScreen screen, ScreenManager screenManager) {
        screenManager.registerPopListener(screen, this);
    }

    void takeView(SecondView view) {
        this.view = view;
    }

    void dropView(SecondView view) {
        if (this.view == view) {
            this.view = null;
        }
    }

    void popConfirmed() {
        hasConfirmedPop = true;
    }

    @Override public boolean onScreenPop(Screen screen) {
        if (!hasConfirmedPop && view != null) {
            view.showConfirmPop();
            return true;
        }
        return false;
    }
}

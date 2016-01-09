package com.jaynewstrom.screenswitcher;

public interface ScreenSwitcher {

    void push(Screen screen);

    void pop(int numberToPop);

    boolean isTransitioning();
}

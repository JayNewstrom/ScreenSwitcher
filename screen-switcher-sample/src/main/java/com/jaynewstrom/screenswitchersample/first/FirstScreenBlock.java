package com.jaynewstrom.screenswitchersample.first;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;

final class FirstScreenBlock implements ConcreteBlock<FirstComponent> {
    private final MainActivityComponent theParentComponent;

    FirstScreenBlock(MainActivityComponent theParentComponent) {
        this.theParentComponent = theParentComponent;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public FirstComponent createComponent() {
        return DaggerFirstComponent.builder().mainActivityComponent(theParentComponent).build();
    }
}

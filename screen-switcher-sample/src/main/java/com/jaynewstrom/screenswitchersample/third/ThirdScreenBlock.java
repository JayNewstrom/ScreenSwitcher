package com.jaynewstrom.screenswitchersample.third;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;

final class ThirdScreenBlock implements ConcreteBlock<ThirdComponent> {
    private final MainActivityComponent theParentComponent;

    ThirdScreenBlock(MainActivityComponent theParentComponent) {
        this.theParentComponent = theParentComponent;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public ThirdComponent createComponent() {
        return DaggerThirdComponent.builder().mainActivityComponent(theParentComponent).build();
    }
}

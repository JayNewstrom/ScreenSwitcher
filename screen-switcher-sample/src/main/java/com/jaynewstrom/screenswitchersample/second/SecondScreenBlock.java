package com.jaynewstrom.screenswitchersample.second;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitchersample.MainActivityComponent;

final class SecondScreenBlock implements ConcreteBlock<SecondComponent> {
    private final MainActivityComponent theParentComponent;
    private final SecondScreen secondScreen;

    SecondScreenBlock(MainActivityComponent theParentComponent, SecondScreen secondScreen) {
        this.theParentComponent = theParentComponent;
        this.secondScreen = secondScreen;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public SecondComponent createComponent() {
        return DaggerSecondComponent.builder()
                .mainActivityComponent(theParentComponent)
                .secondScreenModule(new SecondScreenModule(secondScreen))
                .build();
    }
}

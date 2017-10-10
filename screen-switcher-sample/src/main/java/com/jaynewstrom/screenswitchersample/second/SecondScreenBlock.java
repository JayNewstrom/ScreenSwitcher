package com.jaynewstrom.screenswitchersample.second;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitchersample.base.ParentComponent;

final class SecondScreenBlock implements ConcreteBlock<SecondComponent> {
    private final ParentComponent parentComponent;
    private final SecondScreen secondScreen;

    SecondScreenBlock(ParentComponent parentComponent, SecondScreen secondScreen) {
        this.parentComponent = parentComponent;
        this.secondScreen = secondScreen;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public SecondComponent createComponent() {
        return DaggerSecondComponent.builder()
                .parentComponent(parentComponent)
                .secondScreenModule(new SecondScreenModule(secondScreen))
                .build();
    }
}

package com.jaynewstrom.screenswitchersample.first;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitchersample.base.ParentComponent;

final class FirstScreenBlock implements ConcreteBlock<FirstComponent> {
    private final ParentComponent parentComponent;

    FirstScreenBlock(ParentComponent parentComponent) {
        this.parentComponent = parentComponent;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public FirstComponent createComponent() {
        return DaggerFirstComponent.builder().parentComponent(parentComponent).build();
    }
}

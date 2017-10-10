package com.jaynewstrom.screenswitchersample.thirdscreen;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.screenswitchersample.base.ParentComponent;

final class ThirdScreenBlock implements ConcreteBlock<ThirdComponent> {
    private final ParentComponent parentComponent;
    private final ThirdScreenNavigator navigator;

    ThirdScreenBlock(ParentComponent parentComponent, ThirdScreenNavigator navigator) {
        this.parentComponent = parentComponent;
        this.navigator = navigator;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public ThirdComponent createComponent() {
        return DaggerThirdComponent
                .builder()
                .parentComponent(parentComponent)
                .thirdScreenModule(new ThirdScreenModule(navigator))
                .build();
    }
}

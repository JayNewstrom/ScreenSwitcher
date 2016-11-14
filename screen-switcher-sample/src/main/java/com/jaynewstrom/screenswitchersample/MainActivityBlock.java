package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.concrete.ConcreteBlock;

final class MainActivityBlock implements ConcreteBlock<MainActivityComponent> {
    private final ApplicationComponent applicationComponent;

    MainActivityBlock(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    @Override public String name() {
        return getClass().getName();
    }

    @Override public MainActivityComponent createComponent() {
        return DaggerMainActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .mainActivityModule(new MainActivityModule())
                .build();
    }
}

package com.jaynewstrom.screenswitchersample;

import com.jaynewstrom.concrete.ConcreteBlock;

final class MainActivityBlock implements ConcreteBlock {

    @Override public String name() {
        return getClass().getName();
    }

    @Override public Object daggerModule() {
        return new MainActivityModule();
    }
}

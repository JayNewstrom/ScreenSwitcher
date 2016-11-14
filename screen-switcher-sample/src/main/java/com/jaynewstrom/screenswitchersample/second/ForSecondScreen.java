package com.jaynewstrom.screenswitchersample.second;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope
@Retention(RUNTIME)
@interface ForSecondScreen {
}

package com.jaynewstrom.screenswitchersample.application

import com.jaynewstrom.screenswitchersample.core.LeakWatcher
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
internal interface ApplicationComponent {
    val leakWatcher: LeakWatcher
}

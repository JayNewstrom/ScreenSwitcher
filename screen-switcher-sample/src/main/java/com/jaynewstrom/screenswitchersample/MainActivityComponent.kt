package com.jaynewstrom.screenswitchersample

import dagger.Component

@ForMainActivity
@Component(dependencies = [ApplicationComponent::class], modules = [MainActivityModule::class])
internal interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}

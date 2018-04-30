package com.jaynewstrom.screenswitchersample

import com.jnewstrom.screenswitcher.dialoghub.DialogHub

import dagger.Component

@ForMainActivity
@Component(dependencies = [ApplicationComponent::class], modules = [MainActivityModule::class])
internal interface MainActivityComponent {
    val screenManager: ScreenManager
    val dialogHub: DialogHub
    fun inject(mainActivity: MainActivity)
}

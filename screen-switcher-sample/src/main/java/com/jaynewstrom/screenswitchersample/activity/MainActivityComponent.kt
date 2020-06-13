package com.jaynewstrom.screenswitchersample.activity

import com.jaynewstrom.screenswitcher.dialogmanager.DialogManagerComponent
import com.jaynewstrom.screenswitchersample.application.ApplicationComponent
import com.jaynewstrom.screenswitchersample.core.ScreenParentComponent
import dagger.Component

@ForMainActivity
@Component(dependencies = [ApplicationComponent::class], modules = [MainActivityModule::class])
internal interface MainActivityComponent : ScreenParentComponent, DialogManagerComponent {
    fun inject(mainActivity: MainActivity)
}

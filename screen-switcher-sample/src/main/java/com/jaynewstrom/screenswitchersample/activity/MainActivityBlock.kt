package com.jaynewstrom.screenswitchersample.activity

import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitchersample.application.ApplicationComponent

internal class MainActivityBlock(
    private val applicationComponent: ApplicationComponent
) : ConcreteBlock<MainActivityComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): MainActivityComponent {
        return DaggerMainActivityComponent.builder()
            .applicationComponent(applicationComponent)
            .build()
    }
}

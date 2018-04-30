package com.jaynewstrom.screenswitchersample

import com.jaynewstrom.concrete.ConcreteBlock

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

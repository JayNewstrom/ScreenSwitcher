package com.jaynewstrom.screenswitchersample.first

import android.content.Context
import android.view.View
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition
import com.jaynewstrom.screenswitchersample.MainActivityComponent
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen
import dagger.Component
import javax.inject.Scope

object FirstScreenFactory {
    fun create(): Screen = FirstScreen()
}

private class FirstScreen : ConcreteScreen<FirstComponent>() {
    override fun transition() = DefaultScreenTransition

    override fun block(theParentComponent: MainActivityComponent): ConcreteBlock<FirstComponent> {
        return FirstScreenBlock(theParentComponent)
    }

    public override fun createView(context: Context, component: FirstComponent): View {
        return FirstView(context, component)
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ForFirstScreen

@ForFirstScreen
@Component(dependencies = [MainActivityComponent::class])
internal interface FirstComponent {
    fun inject(view: FirstView)
}

private class FirstScreenBlock(private val theParentComponent: MainActivityComponent) : ConcreteBlock<FirstComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): FirstComponent {
        return DaggerFirstComponent.builder().mainActivityComponent(theParentComponent).build()
    }
}

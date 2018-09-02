package com.jaynewstrom.screenswitchersample.second

import android.content.Context
import android.view.View
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition
import com.jaynewstrom.screenswitchersample.MainActivityComponent
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen
import dagger.Component
import javax.inject.Scope

object SecondScreenFactory {
    fun create(): Screen = SecondScreen()
}

private class SecondScreen : ConcreteScreen<SecondComponent>() {
    override fun transition() = DefaultScreenTransition

    override fun block(theParentComponent: MainActivityComponent): ConcreteBlock<SecondComponent> {
        return SecondScreenBlock(theParentComponent)
    }

    override fun createView(context: Context, component: SecondComponent): View {
        return SecondView(context, component)
    }

    override fun equals(other: Any?) = other is SecondScreen || super.equals(other)

    override fun hashCode() = javaClass.name.hashCode()
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ForSecondScreen

@ForSecondScreen
@Component(dependencies = [MainActivityComponent::class])
internal interface SecondComponent {
    fun inject(view: SecondView)
}

private class SecondScreenBlock(
    private val theParentComponent: MainActivityComponent
) : ConcreteBlock<SecondComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): SecondComponent {
        return DaggerSecondComponent.builder()
            .mainActivityComponent(theParentComponent)
            .build()
    }
}

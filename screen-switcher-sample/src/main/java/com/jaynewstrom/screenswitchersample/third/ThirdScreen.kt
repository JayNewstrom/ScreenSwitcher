package com.jaynewstrom.screenswitchersample.third

import android.content.Context
import android.view.View
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitchersample.DefaultScreenTransition
import com.jaynewstrom.screenswitchersample.MainActivityComponent
import com.jaynewstrom.screenswitchersample.concrete.ConcreteScreen
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope

object ThirdScreenFactory {
    fun create(): Screen = ThirdScreen()
}

private class ThirdScreen : ConcreteScreen<ThirdComponent>() {
    override fun block(theParentComponent: MainActivityComponent): ConcreteBlock<ThirdComponent> {
        return ThirdScreenBlock(theParentComponent)
    }

    public override fun createView(context: Context, component: ThirdComponent): View {
        return ThirdView(context, component)
    }

    override fun transition() = DefaultScreenTransition
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ForThirdScreen

@ForThirdScreen
@Component(dependencies = [MainActivityComponent::class], modules = [ThirdScreenModule::class])
internal interface ThirdComponent {
    fun inject(view: ThirdView)
    fun inject(thirdScreenDialog: ThirdScreenDialog)
}

private class ThirdScreenBlock(private val theParentComponent: MainActivityComponent) : ConcreteBlock<ThirdComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): ThirdComponent {
        return DaggerThirdComponent.builder().mainActivityComponent(theParentComponent).build()
    }
}

@Module
private object ThirdScreenModule {
    @JvmStatic @Provides @Named("dialogMessage") fun provideDialogMessage(): String {
        return "Rotate the device to see if it works!"
    }
}

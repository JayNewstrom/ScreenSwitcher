package com.jaynewstrom.screenswitchersample.first

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitchersample.core.BaseScreen
import com.jaynewstrom.screenswitchersample.core.DefaultScreenWallManager
import com.jaynewstrom.screenswitchersample.core.ScreenScope
import com.jaynewstrom.screenswitchersample.core.ScreenWallManager
import dagger.Component
import dagger.Module
import dagger.Provides

object FirstScreenFactory {
    fun create(navigator: FirstNavigator): Screen = FirstScreen(navigator)
}

private class FirstScreen(private val navigator: FirstNavigator) : BaseScreen<FirstComponent>() {
    override fun createWallManager(): ScreenWallManager<FirstComponent> {
        return DefaultScreenWallManager({
            FirstScreenBlock(navigator)
        })
    }

    public override fun createView(context: Context, hostView: ViewGroup, component: FirstComponent): View {
        return FirstPresenter.createView(context, hostView, component)
    }
}

@ScreenScope
@Component(modules = [FirstModule::class])
internal interface FirstComponent {
    fun inject(presenter: FirstPresenter)
}

@Module
internal class FirstModule(private val navigator: FirstNavigator) {
    @Provides fun provideNavigator() = navigator
}

private class FirstScreenBlock(private val navigator: FirstNavigator) : ConcreteBlock<FirstComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): FirstComponent = DaggerFirstComponent.builder()
        .firstModule(FirstModule(navigator))
        .build()
}

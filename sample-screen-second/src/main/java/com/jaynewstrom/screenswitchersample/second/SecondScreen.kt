package com.jaynewstrom.screenswitchersample.second

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitchersample.core.BaseScreen
import com.jaynewstrom.screenswitchersample.core.DefaultScreenWallManager
import com.jaynewstrom.screenswitchersample.core.ScreenParentComponent
import com.jaynewstrom.screenswitchersample.core.ScreenScope
import com.jaynewstrom.screenswitchersample.core.ScreenWallManager
import dagger.Component
import dagger.Module
import dagger.Provides

object SecondScreenFactory {
    fun create(navigator: SecondNavigator): Screen = SecondScreen(navigator)
}

private class SecondScreen(private val navigator: SecondNavigator) : BaseScreen<SecondComponent>() {
    override fun createWallManager(): ScreenWallManager<SecondComponent> {
        return DefaultScreenWallManager({ parentComponent ->
            SecondScreenBlock(parentComponent, navigator)
        }, { wall ->
            val component = wall.component
            component.screenSwitcherState.registerPopListener(this, component.secondPopListener)
        })
    }

    override fun createView(context: Context, hostView: ViewGroup, component: SecondComponent): View {
        return SecondPresenter.createView(context, hostView, component)
    }

    override fun equals(other: Any?) = other is SecondScreen || super.equals(other)

    override fun hashCode() = javaClass.name.hashCode()
}

@Module
private class SecondModule(private val navigator: SecondNavigator) {
    @Provides fun provideNavigator() = navigator
}

@ScreenScope
@Component(dependencies = [ScreenParentComponent::class], modules = [SecondModule::class])
internal interface SecondComponent {
    fun inject(presenter: SecondPresenter)

    val screenSwitcherState: ScreenSwitcherState
    val secondPopListener: SecondPopListener
}

private class SecondScreenBlock(
    private val parentComponent: ScreenParentComponent,
    private val navigator: SecondNavigator
) : ConcreteBlock<SecondComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): SecondComponent = DaggerSecondComponent.builder()
        .screenParentComponent(parentComponent)
        .secondModule(SecondModule(navigator))
        .build()
}

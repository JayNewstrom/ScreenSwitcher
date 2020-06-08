package com.jaynewstrom.screenswitchersample.second

import android.view.View
import androidx.annotation.LayoutRes
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitchersample.core.BaseScreen
import com.jaynewstrom.screenswitchersample.core.DefaultScreenWallManager
import com.jaynewstrom.screenswitchersample.core.ScreenScope
import com.jaynewstrom.screenswitchersample.core.ScreenWallManager
import dagger.Component
import dagger.Module
import dagger.Provides

object SecondScreenFactory {
    fun create(navigator: SecondNavigator): Screen = SecondScreen(navigator)
}

private class SecondScreen(private val navigator: SecondNavigator) : BaseScreen<SecondComponent>() {
    override fun createWallManager(screenSwitcherState: ScreenSwitcherState): ScreenWallManager<SecondComponent> {
        return DefaultScreenWallManager({
            SecondScreenBlock(navigator)
        }, { wall ->
            val component = wall.component
            screenSwitcherState.registerPopListener(this, component.secondPopListener)
        })
    }

    @LayoutRes override fun layoutId(): Int = SecondPresenter.layoutId()

    override fun bindView(view: View, component: SecondComponent) {
        SecondPresenter.bindView(view, component)
    }

    override fun equals(other: Any?) = other is SecondScreen || super.equals(other)

    override fun hashCode() = javaClass.name.hashCode()
}

@Module
private class SecondModule(private val navigator: SecondNavigator) {
    @Provides fun provideNavigator() = navigator
}

@ScreenScope
@Component(modules = [SecondModule::class])
internal interface SecondComponent {
    fun inject(presenter: SecondPresenter)

    val secondPopListener: SecondPopListener
}

private class SecondScreenBlock(
    private val navigator: SecondNavigator
) : ConcreteBlock<SecondComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): SecondComponent = DaggerSecondComponent.builder()
        .secondModule(SecondModule(navigator))
        .build()
}

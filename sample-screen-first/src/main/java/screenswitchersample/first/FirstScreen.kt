package screenswitchersample.first

import android.view.View
import androidx.annotation.LayoutRes
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import dagger.Component
import dagger.Module
import dagger.Provides
import screenswitchersample.core.screen.BaseScreen
import screenswitchersample.core.screen.DefaultScreenWallManager
import screenswitchersample.core.screen.ScreenScope
import screenswitchersample.core.screen.ScreenWallManager

object FirstScreenFactory {
    fun create(navigator: FirstNavigator): Screen = FirstScreen(navigator)
}

private class FirstScreen(private val navigator: FirstNavigator) : BaseScreen<FirstComponent>() {
    override fun createWallManager(screenSwitcherState: ScreenSwitcherState): ScreenWallManager<FirstComponent> {
        return DefaultScreenWallManager({
            FirstScreenBlock(navigator)
        })
    }

    @LayoutRes override fun layoutId(): Int = FirstPresenter.layoutId()

    override fun bindView(view: View, component: FirstComponent) {
        FirstPresenter.bindView(view, component)
    }
}

@ScreenScope
@Component(modules = [FirstModule::class])
internal interface FirstComponent {
    fun inject(presenter: FirstPresenter)
    fun inject(presenter: NavigateDialogPresenter)
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

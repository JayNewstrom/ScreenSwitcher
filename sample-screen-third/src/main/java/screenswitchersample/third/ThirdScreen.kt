package screenswitchersample.third

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
import javax.inject.Named

object ThirdScreenFactory {
    fun create(navigator: ThirdNavigator): Screen = ThirdScreen(navigator)
}

private class ThirdScreen(private val navigator: ThirdNavigator) : BaseScreen<ThirdComponent>() {
    override fun createWallManager(screenSwitcherState: ScreenSwitcherState): ScreenWallManager<ThirdComponent> {
        return DefaultScreenWallManager({
            ThirdScreenBlock(navigator)
        })
    }

    @LayoutRes override fun layoutId(): Int = ThirdPresenter.layoutId()

    override fun bindView(view: View, component: ThirdComponent) {
        ThirdPresenter.bindView(view, component)
    }
}

@ScreenScope
@Component(modules = [ThirdModule::class])
internal interface ThirdComponent {
    fun inject(dialog: ThirdScreenDialog)
    fun inject(presenter: ThirdPresenter)
}

private class ThirdScreenBlock(private val navigator: ThirdNavigator) : ConcreteBlock<ThirdComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): ThirdComponent = DaggerThirdComponent.builder()
        .thirdModule(ThirdModule(navigator))
        .build()
}

@Module
private class ThirdModule(private val navigator: ThirdNavigator) {
    @Provides fun provideNavigator() = navigator

    @Module
    companion object {
        @JvmStatic @Provides @Named("dialogMessage") fun provideDialogMessage(): String {
            return "Rotate the device to see if it works!"
        }
    }
}

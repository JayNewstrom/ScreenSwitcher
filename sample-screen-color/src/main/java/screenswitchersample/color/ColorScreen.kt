package screenswitchersample.color

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
import javax.inject.Qualifier

interface ColorScreenNavigator {
    fun colorSubmitted(colorHex: String, fromView: View)
}

object ColorScreenFactory {
    fun create(navigator: ColorScreenNavigator, colorHex: String): Screen = ColorScreen(navigator, colorHex)
}

private class ColorScreen(
    private val navigator: ColorScreenNavigator,
    private val colorHex: String
) : BaseScreen<ColorComponent>() {
    override fun createWallManager(screenSwitcherState: ScreenSwitcherState): ScreenWallManager<ColorComponent> {
        return DefaultScreenWallManager({
            ColorScreenBlock(navigator, colorHex)
        })
    }

    @LayoutRes override fun layoutId(): Int = ColorPresenter.layoutId()

    override fun bindView(view: View, component: ColorComponent) {
        ColorPresenter.bindView(view, component)
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ColorHex

@ScreenScope
@Component(modules = [ColorModule::class])
internal interface ColorComponent {
    fun inject(presenter: ColorPresenter)
}

@Module
internal class ColorModule(private val navigator: ColorScreenNavigator, private val colorHex: String) {
    @Provides @ColorHex fun provideColorHex() = colorHex
    @Provides fun provideNavigator() = navigator
}

private class ColorScreenBlock(
    private val navigator: ColorScreenNavigator,
    private val colorHex: String
) : ConcreteBlock<ColorComponent> {
    override fun name(): String = javaClass.name + colorHex

    override fun createComponent(): ColorComponent = DaggerColorComponent.builder()
        .colorModule(ColorModule(navigator, colorHex))
        .build()
}

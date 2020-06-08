package com.jaynewstrom.screenswitchersample.color

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
import javax.inject.Qualifier

object ColorScreenFactory {
    fun create(colorHex: String): Screen = ColorScreen(colorHex)
}

private class ColorScreen(private val colorHex: String) : BaseScreen<ColorComponent>() {
    override fun createWallManager(screenSwitcherState: ScreenSwitcherState): ScreenWallManager<ColorComponent> {
        return DefaultScreenWallManager({
            ColorScreenBlock(colorHex)
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
internal class ColorModule(private val colorHex: String) {
    @Provides @ColorHex fun provideColorHex() = colorHex
}

private class ColorScreenBlock(private val colorHex: String) : ConcreteBlock<ColorComponent> {
    override fun name(): String = javaClass.name + colorHex

    override fun createComponent(): ColorComponent = DaggerColorComponent.builder()
        .colorModule(ColorModule(colorHex))
        .build()
}

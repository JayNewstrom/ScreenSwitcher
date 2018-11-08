package com.jaynewstrom.screenswitchersample.third

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
import javax.inject.Named

object ThirdScreenFactory {
    fun create(navigator: ThirdNavigator): Screen = ThirdScreen(navigator)
}

private class ThirdScreen(private val navigator: ThirdNavigator) : BaseScreen<ThirdComponent>() {
    override fun createWallManager(): ScreenWallManager<ThirdComponent> {
        return DefaultScreenWallManager({
            ThirdScreenBlock(navigator)
        })
    }

    public override fun createView(context: Context, hostView: ViewGroup, component: ThirdComponent): View {
        return ThirdPresenter.createView(context, hostView, component)
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

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
    fun create(): Screen = ThirdScreen()
}

private class ThirdScreen : BaseScreen<ThirdComponent>() {
    override fun createWallManager(): ScreenWallManager<ThirdComponent> {
        return DefaultScreenWallManager({
            ThirdScreenBlock()
        })
    }

    public override fun createView(context: Context, hostView: ViewGroup, component: ThirdComponent): View {
        return ThirdPresenter.createView(context, hostView)
    }
}

@ScreenScope
@Component(modules = [ThirdScreenModule::class])
internal interface ThirdComponent {
    fun inject(thirdScreenDialog: ThirdScreenDialog)
}

private class ThirdScreenBlock : ConcreteBlock<ThirdComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): ThirdComponent = DaggerThirdComponent.create()
}

@Module
private object ThirdScreenModule {
    @JvmStatic @Provides @Named("dialogMessage") fun provideDialogMessage(): String {
        return "Rotate the device to see if it works!"
    }
}

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

object FirstScreenFactory {
    fun create(): Screen = FirstScreen()
}

private class FirstScreen : BaseScreen<FirstComponent>() {
    override fun createWallManager(): ScreenWallManager<FirstComponent> {
        return DefaultScreenWallManager({
            FirstScreenBlock()
        })
    }

    public override fun createView(context: Context, hostView: ViewGroup, component: FirstComponent): View {
        return FirstPresenter.createView(context, hostView, component)
    }
}

@ScreenScope
@Component
internal interface FirstComponent {
    fun inject(presenter: FirstPresenter)
}

private class FirstScreenBlock : ConcreteBlock<FirstComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): FirstComponent = DaggerFirstComponent.create()
}

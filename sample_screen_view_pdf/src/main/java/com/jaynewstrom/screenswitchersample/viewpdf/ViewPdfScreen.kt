package com.jaynewstrom.screenswitchersample.viewpdf

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

object ViewPdfScreenFactory {
    @JvmStatic fun create(): Screen = ViewPdfScreen()
}

private class ViewPdfScreen : BaseScreen<ViewPdfComponent>() {
    override fun createWallManager(): ScreenWallManager<ViewPdfComponent> {
        return DefaultScreenWallManager({
            ViewPdfScreenBlock()
        })
    }

    public override fun createView(context: Context, hostView: ViewGroup, component: ViewPdfComponent): View {
        return ViewPdfPresenter.createView(context, hostView, component)
    }
}

@ScreenScope
@Component
internal interface ViewPdfComponent

private class ViewPdfScreenBlock : ConcreteBlock<ViewPdfComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): ViewPdfComponent = DaggerViewPdfComponent.create()
}

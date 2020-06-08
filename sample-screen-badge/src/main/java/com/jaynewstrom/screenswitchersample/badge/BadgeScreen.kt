package com.jaynewstrom.screenswitchersample.badge

import android.view.View
import androidx.annotation.LayoutRes
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitchersample.core.BaseScreen
import com.jaynewstrom.screenswitchersample.core.DefaultScreenWallManager
import com.jaynewstrom.screenswitchersample.core.ScreenScope
import com.jaynewstrom.screenswitchersample.core.ScreenWallManager
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

object BadgeScreenFactory {
    fun create(badgeCountRelay: BehaviorRelay<Int>): Screen = BadgeScreen(badgeCountRelay)
}

private class BadgeScreen(private val badgeCountRelay: BehaviorRelay<Int>) : BaseScreen<BadgeComponent>() {
    override fun createWallManager(): ScreenWallManager<BadgeComponent> {
        return DefaultScreenWallManager({
            BadgeScreenBlock(badgeCountRelay)
        })
    }

    @LayoutRes override fun layoutId(): Int = BadgePresenter.layoutId()

    override fun bindView(view: View, component: BadgeComponent) {
        BadgePresenter.bindView(view, component)
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BadgeCount

@ScreenScope
@Component(modules = [BadgeModule::class])
internal interface BadgeComponent {
    fun inject(presenter: BadgePresenter)
}

@Module
internal class BadgeModule(private val badgeCountRelay: BehaviorRelay<Int>) {
    @Provides @BadgeCount fun provideBadgeCountRelay() = badgeCountRelay
}

private class BadgeScreenBlock(private val badgeCountRelay: BehaviorRelay<Int>) : ConcreteBlock<BadgeComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): BadgeComponent = DaggerBadgeComponent.builder()
        .badgeModule(BadgeModule(badgeCountRelay))
        .build()
}

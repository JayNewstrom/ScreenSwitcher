package com.jaynewstrom.screenswitcher.tabbar

import android.view.View
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitchersample.core.BaseScreen
import com.jaynewstrom.screenswitchersample.core.DefaultScreenWallManager
import com.jaynewstrom.screenswitchersample.core.ScreenParentComponent
import com.jaynewstrom.screenswitchersample.core.ScreenScope
import com.jaynewstrom.screenswitchersample.core.ScreenWallManager
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Qualifier

object TabBarScreenFactory {
    fun create(
        tabBarItems: List<TabBarItem>,
        externalInitializationAction: () -> Unit,
        popListener: (view: View) -> Boolean
    ): Screen {
        return TabBarScreen(tabBarItems, externalInitializationAction, popListener)
    }
}

private class TabBarScreen(
    private val tabBarItems: List<TabBarItem>,
    private val externalInitializationAction: () -> Unit,
    private val popListener: (view: View) -> Boolean
) : BaseScreen<TabBarComponent>() {
    private lateinit var currentTabBarItemStateHolder: CurrentTabBarItemStateHolder

    override fun createWallManager(screenSwitcherState: ScreenSwitcherState): ScreenWallManager<TabBarComponent> {
        return DefaultScreenWallManager(
            { parentComponent ->
                TabBarBlock(parentComponent, tabBarItems, popListener, screenSwitcherState)
            },
            { wall ->
                externalInitializationAction()

                val wallComponent = wall.component
                currentTabBarItemStateHolder = wallComponent.currentTabBarItemStateHolder

                tabBarItems.forEach { tabBarItem ->
                    // This is to keep the connected observables connected, even on rotation, so we don't make multiple
                    // network requests on rotation.
                    tabBarItem.badgeCountObservable?.let { wallComponent.compositeDisposable.add(it.subscribe()) }
                }

                screenSwitcherState.registerPopListener(this, wallComponent.tabBarPopHandler)

                wall.addDestructionAction { component -> component.compositeDisposable.dispose() }
            }
        )
    }

    override fun layoutId(): Int = TabBarPresenter.layoutId

    override fun bindView(view: View, component: TabBarComponent) {
        TabBarPresenter(view, component)
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class PopListener

@ScreenScope
@Component(
    dependencies = [ScreenParentComponent::class],
    modules = [TabBarModule::class]
)
internal interface TabBarComponent : ScreenParentComponent {
    fun inject(tabBarPresenter: TabBarPresenter)
    val currentTabBarItemStateHolder: CurrentTabBarItemStateHolder
    val compositeDisposable: CompositeDisposable
    val tabBarPopHandler: TabBarPopHandler
}

@Module
private class TabBarModule(
    private val tabBarItems: List<TabBarItem>,
    private val popListener: (view: View) -> Boolean,
    private val screenSwitcherState: ScreenSwitcherState
) {
    @Provides fun provideTabBarItemFactories() = tabBarItems
    @Provides @PopListener fun providePopListener(): (view: View) -> Boolean = popListener
    @Provides @ScreenScope fun provideCurrentTabBarItemStateHolder() = CurrentTabBarItemStateHolder(tabBarItems.first())
    @Provides fun provideScreenSwitcherState() = screenSwitcherState

    @Module
    companion object {
        @Provides @ScreenScope @JvmStatic fun provideDisposable() = CompositeDisposable()
    }
}

private class TabBarBlock(
    private val parentComponent: ScreenParentComponent,
    private val tabBarItems: List<TabBarItem>,
    private val popListener: (view: View) -> Boolean,
    private val screenSwitcherState: ScreenSwitcherState
) : ConcreteBlock<TabBarComponent> {
    override fun name(): String = javaClass.name

    override fun createComponent(): TabBarComponent {
        return DaggerTabBarComponent
            .builder()
            .screenParentComponent(parentComponent)
            .tabBarModule(TabBarModule(tabBarItems, popListener, screenSwitcherState))
            .build()
    }
}

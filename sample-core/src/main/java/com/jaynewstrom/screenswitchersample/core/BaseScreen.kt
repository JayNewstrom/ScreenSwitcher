package com.jaynewstrom.screenswitchersample.core

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.concrete.ConcreteWall
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.ScreenTransition

abstract class BaseScreen<C> : Screen {
    private var state: BaseScreenState<C>? = null

    protected abstract fun createWallManager(): ScreenWallManager<C>

    protected abstract fun createView(context: Context, hostView: ViewGroup, component: C): View

    final override fun createView(context: Context, hostView: ViewGroup): View {
        val state = state
        val screenWall: ConcreteWall<C>
        if (state == null) {
            val parentWall = Concrete.findWall<ConcreteWall<ScreenParentComponent>>(context)
            val wallManager = createWallManager()
            screenWall = wallManager.createScreenWall(parentWall)
            val leakWatcher = parentWall.component.leakWatcher
            this.state = BaseScreenState(screenWall, leakWatcher, wallManager)
        } else {
            screenWall = state.screenWall
        }
        return createView(screenWall.createContext(context), hostView, screenWall.component)
    }

    final override fun destroyScreen(viewToDestroy: View) {
        val state = state!!
        val component = state.screenWall.component
        state.screenWall.destroy()
        state.leakWatcher.watch(this)
        state.leakWatcher.watch(viewToDestroy)
        state.leakWatcher.watch(component)
    }

    override fun transition(): ScreenTransition = DefaultScreenTransition
}

private data class BaseScreenState<C>(
    val screenWall: ConcreteWall<C>,
    val leakWatcher: LeakWatcher,
    val wallManager: ScreenWallManager<C>
)

interface ScreenWallManager<C> {
    fun createScreenWall(parentWall: ConcreteWall<ScreenParentComponent>): ConcreteWall<C>
}

class DefaultScreenWallManager<C>(
    private val blockFactory: (parentComponent: ScreenParentComponent) -> ConcreteBlock<C>,
    private val initializationAction: ((wall: ConcreteWall<C>) -> Unit)? = null
) : ScreenWallManager<C> {
    override fun createScreenWall(parentWall: ConcreteWall<ScreenParentComponent>): ConcreteWall<C> {
        return parentWall.stack(blockFactory(parentWall.component), initializationAction)
    }
}

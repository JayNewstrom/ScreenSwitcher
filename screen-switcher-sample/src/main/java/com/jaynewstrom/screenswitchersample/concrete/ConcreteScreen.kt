package com.jaynewstrom.screenswitchersample.concrete

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteBlock
import com.jaynewstrom.concrete.ConcreteWall
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitchersample.MainActivityComponent
import com.jaynewstrom.screenswitchersample.ScreenSwitcherApplication

internal abstract class ConcreteScreen<C> : Screen {
    private lateinit var screenWall: ConcreteWall<C>

    override fun createView(context: Context, hostView: ViewGroup): View {
        val activityWall = Concrete.findWall<ConcreteWall<MainActivityComponent>>(context)
        screenWall = activityWall.stack(block(activityWall.component))
        return createView(screenWall.createContext(context), screenWall.component)
    }

    protected abstract fun block(theParentComponent: MainActivityComponent): ConcreteBlock<C>

    protected abstract fun createView(context: Context, component: C): View

    override fun destroyScreen(viewToDestroy: View) {
        ScreenSwitcherApplication.watchObject(viewToDestroy.context, this)
        ScreenSwitcherApplication.watchObject(viewToDestroy.context, viewToDestroy.context)
        ScreenSwitcherApplication.watchObject(viewToDestroy.context, screenWall.component as Any)
        screenWall.destroy()
    }
}

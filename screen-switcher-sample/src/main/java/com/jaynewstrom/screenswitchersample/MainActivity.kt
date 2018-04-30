package com.jaynewstrom.screenswitchersample

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent

import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory
import com.jaynewstrom.screenswitcher.ScreenSwitcherPopHandler
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jnewstrom.screenswitcher.dialoghub.DialogHub
import javax.inject.Inject

class MainActivity : Activity(), ScreenSwitcherPopHandler {
    @Inject internal lateinit var screenSwitcherState: ScreenSwitcherState
    @Inject internal lateinit var screenManager: ScreenManager
    @Inject internal lateinit var dialogHub: DialogHub

    private lateinit var activityScreenSwitcher: ScreenSwitcher
    private lateinit var activityWall: ConcreteWall<MainActivityComponent>
    private var popCompleteHandler: ScreenSwitcherPopHandler.PopCompleteHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val foundation = Concrete.findWall<ConcreteWall<ApplicationComponent>>(applicationContext)
        activityWall = foundation.stack(MainActivityBlock(foundation.component))
        activityWall.component.inject(this)
        activityScreenSwitcher = ScreenSwitcherFactory.activityScreenSwitcher(this, screenSwitcherState, this)
        screenManager.take(activityScreenSwitcher)
        dialogHub.attachActivity(this)
        dialogHub.restoreState()
    }

    override fun onDestroy() {
        super.onDestroy()
        screenManager.drop(activityScreenSwitcher)
        dialogHub.dropActivity(this)
        popCompleteHandler?.popComplete()
        if (isFinishing) {
            activityWall.destroy()
        } else {
            dialogHub.saveState()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return activityScreenSwitcher.isTransitioning || super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (!activityScreenSwitcher.isTransitioning && !isFinishing) {
            screenManager.pop()
        }
    }

    override fun getSystemService(name: String): Any? {
        return if (Concrete.isService(name)) {
            activityWall
        } else {
            super.getSystemService(name)
        }
    }

    override fun onLastScreenPopped(popCompleteHandler: ScreenSwitcherPopHandler.PopCompleteHandler) {
        this.popCompleteHandler = popCompleteHandler
    }
}

package com.jaynewstrom.screenswitchersample.activity

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory
import com.jaynewstrom.screenswitcher.ScreenSwitcherPopHandler
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.dialogmanager.DialogManager
import com.jaynewstrom.screenswitcher.screenmanager.ScreenManager
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.application.ApplicationComponent
import javax.inject.Inject

class MainActivity : Activity(), ScreenSwitcherPopHandler {
    @Inject internal lateinit var screenSwitcherState: ScreenSwitcherState
    @Inject internal lateinit var screenManager: ScreenManager
    @Inject internal lateinit var dialogManager: DialogManager

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
        dialogManager.attachActivity(this)
        dialogManager.restoreState()

        val contentView = findViewById<View>(android.R.id.content)
        contentView.setTag(R.id.screen_manager, screenManager)
        contentView.setTag(R.id.dialog_manager, dialogManager)
    }

    override fun onDestroy() {
        super.onDestroy()
        screenManager.drop(activityScreenSwitcher)
        dialogManager.dropActivity(this)
        popCompleteHandler?.popComplete()
        if (isFinishing) {
            activityWall.destroy()
        } else {
            dialogManager.saveState()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return activityScreenSwitcher.isTransitioning || super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (!activityScreenSwitcher.isTransitioning && !isFinishing) {
            activityScreenSwitcher.pop(1)
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

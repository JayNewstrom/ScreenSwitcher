package com.jaynewstrom.screenswitchersample.activity

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory
import com.jaynewstrom.screenswitcher.ScreenSwitcherFinishHandler
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.dialogmanager.DialogManager
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.application.ApplicationComponent
import javax.inject.Inject

class MainActivity : Activity(), ScreenSwitcherFinishHandler {
    @Inject internal lateinit var screenSwitcherState: ScreenSwitcherState
    @Inject internal lateinit var dialogManager: DialogManager

    private lateinit var activityScreenSwitcher: ScreenSwitcher
    private lateinit var activityWall: ConcreteWall<MainActivityComponent>
    private var finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val foundation = Concrete.findWall<ConcreteWall<ApplicationComponent>>(applicationContext)
        activityWall = foundation.stack(MainActivityBlock(foundation.component))
        activityWall.component.inject(this)
        setContentView(R.layout.main_activity)
        val screenHost = findViewById<ViewGroup>(R.id.screen_host)
        activityScreenSwitcher = ScreenSwitcherFactory.viewScreenSwitcher(screenHost, screenSwitcherState, this)
        dialogManager.attachActivity(this)
        dialogManager.restoreState()

        screenHost.isSaveFromParentEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityScreenSwitcher.saveViewHierarchyStateToScreenSwitcherState()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.dropActivity(this)
        finishCompleteHandler?.finishComplete()
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

    override fun onScreenSwitcherFinished(finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler) {
        this.finishCompleteHandler = finishCompleteHandler
    }
}

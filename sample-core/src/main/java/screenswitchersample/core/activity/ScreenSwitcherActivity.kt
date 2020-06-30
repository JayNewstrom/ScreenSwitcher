package screenswitchersample.core.activity

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewGroup
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.screenswitcher.ScreenSwitcher
import com.jaynewstrom.screenswitcher.ScreenSwitcherFactory
import com.jaynewstrom.screenswitcher.ScreenSwitcherFinishHandler
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.dialogmanager.DialogManager
import screenswitchersample.core.R
import javax.inject.Inject

class ScreenSwitcherActivity : Activity(), ScreenSwitcherFinishHandler {
    private val concreteHelper = ActivityConcreteHelper()

    @Inject internal lateinit var screenSwitcherState: ScreenSwitcherState
    @Inject internal lateinit var dialogManager: DialogManager

    private lateinit var screenSwitcher: ScreenSwitcher
    private var finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        concreteHelper.onActivityCreated(this, savedInstanceState)
        concreteHelper.activityWall?.component?.inject(this)
        setContentView(R.layout.screen_switcher_activity)
        val screenHost = findViewById<ViewGroup>(R.id.screen_host)
        screenSwitcher = ScreenSwitcherFactory.viewScreenSwitcher(screenHost, screenSwitcherState, this)
        dialogManager.attachActivity(this)
        dialogManager.restoreState()

        screenHost.isSaveFromParentEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        concreteHelper.onActivitySaveInstanceState(outState)
        screenSwitcher.saveViewHierarchyStateToScreenSwitcherState()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogManager.dropActivity(this)
        finishCompleteHandler?.finishComplete()
        finishCompleteHandler = null
        if (!isFinishing) {
            dialogManager.saveState()
        }

        concreteHelper.onActivityDestroyed(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return screenSwitcher.isTransitioning || super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        if (!screenSwitcher.isTransitioning && !isFinishing) {
            screenSwitcher.pop(1)
        }
    }

    override fun getSystemService(name: String): Any? {
        return if (Concrete.isService(name)) {
            concreteHelper.activityWall
        } else {
            super.getSystemService(name)
        }
    }

    override fun onScreenSwitcherFinished(finishCompleteHandler: ScreenSwitcherFinishHandler.FinishCompleteHandler) {
        this.finishCompleteHandler = finishCompleteHandler
        finish()
    }
}

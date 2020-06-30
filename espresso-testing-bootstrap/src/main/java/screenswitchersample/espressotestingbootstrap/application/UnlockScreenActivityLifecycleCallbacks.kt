package screenswitchersample.espressotestingbootstrap.application

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import screenswitchersample.core.utilities.AbstractActivityLifecycleCallbacks

@Suppress("DEPRECATION")
private const val UNLOCK_FLAGS =
    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON

internal object UnlockScreenActivityLifecycleCallbacks : AbstractActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activity.window.addFlags(UNLOCK_FLAGS)
    }
}

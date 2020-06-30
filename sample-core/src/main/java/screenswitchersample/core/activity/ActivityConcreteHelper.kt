package screenswitchersample.core.activity

import android.app.Activity
import android.os.Bundle
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import screenswitchersample.core.components.ActivityComponent
import screenswitchersample.core.components.ApplicationComponent
import java.util.UUID

private const val SAVED_INSTANCE_UNIQUE_IDENTIFIER = "uniqueIdentifier"

internal class ActivityConcreteHelper {
    var activityWall: ConcreteWall<ActivityComponent>? = null
        private set
    private lateinit var uniqueIdentifier: String

    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        uniqueIdentifier = if (savedInstanceState?.containsKey(SAVED_INSTANCE_UNIQUE_IDENTIFIER) == true) {
            savedInstanceState.getString(SAVED_INSTANCE_UNIQUE_IDENTIFIER)!!
        } else {
            UUID.randomUUID().toString()
        }
        val foundation = Concrete.findWall<ConcreteWall<ApplicationComponent>>(
            activity.applicationContext
        )
        activityWall = foundation.stack(
            ScreenSwitcherActivityConcreteBlock(foundation.component, activity.intent, uniqueIdentifier)
        )
    }

    fun onActivitySaveInstanceState(bundle: Bundle) {
        bundle.putString(SAVED_INSTANCE_UNIQUE_IDENTIFIER, uniqueIdentifier)
    }

    fun onActivityDestroyed(activity: Activity) {
        if (activity.isFinishing) {
            activityWall?.destroy()
            activityWall = null
        }
    }
}

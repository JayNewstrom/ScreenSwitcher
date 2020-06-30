package screenswitchersample.core.activity

import android.content.Intent
import screenswitchersample.core.components.ActivityComponent
import screenswitchersample.core.components.PassthroughComponent

interface ActivityComponentFactory {
    fun create(component: PassthroughComponent, intent: Intent?): ActivityComponent
}

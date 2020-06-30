package screenswitchersample.espressotestingbootstrap.activity

import android.content.Intent
import screenswitchersample.core.activity.ActivityComponentFactory
import screenswitchersample.core.activity.ActivityModule
import screenswitchersample.core.components.ActivityComponent
import screenswitchersample.core.components.PassthroughComponent

const val TEST_SCREEN_FACTORY = "testScreenFactory"

internal object TestActivityComponentFactory : ActivityComponentFactory {
    override fun create(component: PassthroughComponent, intent: Intent?): ActivityComponent {
        return DaggerTestActivityComponent.builder()
            .activityModule(ActivityModule(intent))
            .passthroughComponent(component)
            .build()
    }
}

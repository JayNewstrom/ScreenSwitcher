package screenswitchersample.activity

import android.content.Intent
import screenswitchersample.core.activity.ActivityComponentFactory
import screenswitchersample.core.activity.ActivityModule
import screenswitchersample.core.components.ActivityComponent
import screenswitchersample.core.components.PassthroughComponent

internal object SampleActivityComponentFactory : ActivityComponentFactory {
    override fun create(component: PassthroughComponent, intent: Intent?): ActivityComponent {
        return DaggerSampleActivityComponent.builder()
            .activityModule(ActivityModule(intent))
            .passthroughComponent(component)
            .build()
    }
}

package screenswitchersample.core.components

import screenswitchersample.core.activity.ActivityComponentFactory

interface ApplicationComponent : PassthroughComponent {
    val activityComponentFactory: ActivityComponentFactory
}

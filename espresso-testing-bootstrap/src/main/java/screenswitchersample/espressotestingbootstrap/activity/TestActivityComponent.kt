package screenswitchersample.espressotestingbootstrap.activity

import dagger.Component
import screenswitchersample.core.activity.ActivityModule
import screenswitchersample.core.activity.ForActivity
import screenswitchersample.core.components.ActivityComponent
import screenswitchersample.core.components.PassthroughComponent

@ForActivity
@Component(
    dependencies = [PassthroughComponent::class],
    modules = [TestActivityModule::class, ActivityModule::class]
)
internal interface TestActivityComponent : ActivityComponent

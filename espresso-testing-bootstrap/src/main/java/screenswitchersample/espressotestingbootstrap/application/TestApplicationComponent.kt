package screenswitchersample.espressotestingbootstrap.application

import dagger.Component
import screenswitchersample.core.components.ApplicationComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestApplicationModule::class
    ]
)
internal interface TestApplicationComponent : ApplicationComponent {
    fun inject(application: TestApplication)
}

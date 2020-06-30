package screenswitchersample.application

import dagger.Component
import screenswitchersample.core.components.ApplicationComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
internal interface SampleApplicationComponent : ApplicationComponent

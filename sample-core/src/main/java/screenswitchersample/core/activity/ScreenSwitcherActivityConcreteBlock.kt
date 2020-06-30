package screenswitchersample.core.activity

import android.content.Intent
import com.jaynewstrom.concrete.ConcreteBlock
import screenswitchersample.core.components.ActivityComponent
import screenswitchersample.core.components.ApplicationComponent

internal class ScreenSwitcherActivityConcreteBlock(
    private val applicationComponent: ApplicationComponent,
    private val intent: Intent,
    private val uniqueIdentifier: String
) : ConcreteBlock<ActivityComponent> {
    override fun name(): String = javaClass.name + uniqueIdentifier

    override fun createComponent(): ActivityComponent {
        return applicationComponent.activityComponentFactory.create(applicationComponent, intent)
    }
}

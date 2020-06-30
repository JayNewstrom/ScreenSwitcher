package screenswitchersample.core.components

import com.jaynewstrom.screenswitcher.dialogmanager.DialogManagerComponent
import screenswitchersample.core.activity.ScreenSwitcherActivity

interface ActivityComponent : PassthroughComponent, DialogManagerComponent {
    fun inject(activity: ScreenSwitcherActivity)
}

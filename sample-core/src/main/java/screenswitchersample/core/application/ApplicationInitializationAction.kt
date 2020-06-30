package screenswitchersample.core.application

import android.app.Application

class ApplicationInitializationAction(
    val initializationOrder: Int = 0,
    val performAction: (application: Application) -> Unit
)

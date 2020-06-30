package screenswitchersample.espressotestingbootstrap.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.jaynewstrom.concrete.Concrete
import screenswitchersample.core.components.ApplicationComponent

object ApplicationComponentFinder {
    fun find(): ApplicationComponent {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()
        return Concrete.getComponent(applicationContext)
    }
}

package screenswitchersample.application

import android.app.Application
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import timber.log.Timber

class ScreenSwitcherApplication : Application() {
    private lateinit var foundation: ConcreteWall<SampleApplicationComponent>

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        foundation = Concrete.pourFoundation(applicationComponent())
    }

    private fun applicationComponent(): SampleApplicationComponent {
        return DaggerSampleApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    override fun getSystemService(name: String): Any? {
        return if (Concrete.isService(name)) {
            foundation
        } else super.getSystemService(name)
    }
}

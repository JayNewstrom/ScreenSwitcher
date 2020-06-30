package screenswitchersample.espressotestingbootstrap.application

import android.app.Application
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import screenswitchersample.core.application.ApplicationInitializationAction
import javax.inject.Inject

internal class TestApplication : Application() {
    private lateinit var foundation: ConcreteWall<TestApplicationComponent>

    @Inject lateinit var applicationInitializationActions: Set<@JvmSuppressWildcards ApplicationInitializationAction>

    override fun onCreate() {
        super.onCreate()

        foundation = Concrete.pourFoundation(
            DaggerTestApplicationComponent.builder()
                .testApplicationModule(TestApplicationModule(this))
                .build()
        )

        foundation.component.inject(this)

        for (applicationInitializationAction in applicationInitializationActions) {
            applicationInitializationAction.performAction(this)
        }
    }

    override fun getSystemService(name: String): Any? {
        return if (Concrete.isService(name)) {
            foundation
        } else {
            super.getSystemService(name)
        }
    }
}

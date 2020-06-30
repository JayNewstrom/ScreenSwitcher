package screenswitchersample.espressotestingbootstrap.application

import android.app.Application
import android.content.Context
import com.squareup.rx2.idler.Rx2Idler
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import io.reactivex.plugins.RxJavaPlugins
import leakcanary.AppWatcher
import screenswitchersample.core.activity.ActivityComponentFactory
import screenswitchersample.core.application.ApplicationInitializationAction
import screenswitchersample.core.leak.LeakWatcher
import screenswitchersample.espressotestingbootstrap.activity.TestActivityComponentFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
internal class TestApplicationModule(
    private val application: Application
) {
    @Provides @Singleton fun provideApplicationContext(): Context {
        return application
    }

    @Module
    companion object {
        @Provides @Singleton @JvmStatic fun provideLeakWatcher(): LeakWatcher {
            return object : LeakWatcher {
                override fun <T> watch(t: T, description: String) {
                    AppWatcher.objectWatcher.watch(t as Any, description)
                }
            }
        }

        @Provides @JvmStatic
        fun provideScreenSwitcherActivityComponentFactory(): ActivityComponentFactory {
            return TestActivityComponentFactory
        }

        @Provides @IntoSet @JvmStatic
        fun provideTimberInitializationAction(): ApplicationInitializationAction {
            return ApplicationInitializationAction {
                Timber.plant(Timber.DebugTree())
            }
        }

        @Provides @IntoSet @JvmStatic
        fun provideUnlockScreenInitializationAction(): ApplicationInitializationAction {
            return ApplicationInitializationAction { application ->
                application.registerActivityLifecycleCallbacks(UnlockScreenActivityLifecycleCallbacks)
            }
        }

        @Provides @IntoSet @JvmStatic
        fun provideRxJavaIdlingResourceInitializationAction(): ApplicationInitializationAction {
            return ApplicationInitializationAction {
                RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("RxJava 2.x Computation Scheduler"))
                RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("RxJava 2.x IO Scheduler"))
            }
        }
    }
}

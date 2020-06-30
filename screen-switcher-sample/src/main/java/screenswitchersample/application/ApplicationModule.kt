package screenswitchersample.application

import android.content.Context
import dagger.Module
import dagger.Provides
import leakcanary.AppWatcher
import screenswitchersample.activity.SampleActivityComponentFactory
import screenswitchersample.core.activity.ActivityComponentFactory
import screenswitchersample.core.leak.LeakWatcher
import javax.inject.Singleton

@Module
internal class ApplicationModule(private val applicationContext: Context) {
    @Provides fun provideApplicationContext() = applicationContext

    @Module
    companion object {
        @Provides @JvmStatic @Singleton fun provideLeakWatcher() = object : LeakWatcher {
            override fun <T> watch(t: T, description: String) {
                AppWatcher.objectWatcher.watch(t as Any, description)
            }
        }

        @Provides @JvmStatic @Singleton
        fun provideActivityComponentFactory(): ActivityComponentFactory = SampleActivityComponentFactory
    }
}

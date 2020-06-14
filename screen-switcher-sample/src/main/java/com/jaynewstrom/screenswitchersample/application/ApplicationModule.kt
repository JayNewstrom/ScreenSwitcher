package com.jaynewstrom.screenswitchersample.application

import android.content.Context
import com.jaynewstrom.screenswitchersample.core.LeakWatcher
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class ApplicationModule(private val applicationContext: Context) {
    @Provides fun provideApplicationContext() = applicationContext

    @Module
    companion object {
        @Provides @JvmStatic @Singleton fun provideLeakWatcher(context: Context) = object : LeakWatcher {
            override fun <T> watch(t: T, description: String) {
                ScreenSwitcherApplication.watchObject(context, t, description)
            }
        }
    }
}

package com.jaynewstrom.screenswitchersample.application

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

class ScreenSwitcherApplication : Application() {
    private lateinit var foundation: ConcreteWall<ApplicationComponent>
    private lateinit var refWatcher: RefWatcher

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        refWatcher = LeakCanary.install(this)
        foundation = Concrete.pourFoundation(applicationComponent())
    }

    private fun applicationComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    override fun getSystemService(name: String): Any? {
        return if (Concrete.isService(name)) {
            foundation
        } else super.getSystemService(name)
    }

    companion object {
        fun <T> watchObject(context: Context, watchedReference: T) {
            val application = context.applicationContext as ScreenSwitcherApplication
            application.refWatcher.watch(watchedReference as Any)
        }
    }
}

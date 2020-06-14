package com.jaynewstrom.screenswitchersample.application

import android.app.Application
import android.content.Context
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concrete.ConcreteWall
import leakcanary.AppWatcher
import leakcanary.ObjectWatcher
import timber.log.Timber

class ScreenSwitcherApplication : Application() {
    private lateinit var foundation: ConcreteWall<ApplicationComponent>
    private lateinit var objectWatcher: ObjectWatcher

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        objectWatcher = AppWatcher.objectWatcher
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
        fun <T> watchObject(context: Context, watchedReference: T, description: String) {
            val application = context.applicationContext as ScreenSwitcherApplication
            application.objectWatcher.watch(watchedReference as Any, description)
        }
    }
}

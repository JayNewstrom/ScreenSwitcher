package screenswitchersample.espressotestingbootstrap.activity

import com.jaynewstrom.screenswitcher.dialogmanager.DialogManager
import com.jaynewstrom.screenswitcher.screenmanager.CompositeScreenLifecycleListener
import dagger.Binds
import dagger.Module
import dagger.Provides
import screenswitchersample.core.activity.ForActivity
import screenswitchersample.core.activity.InitialScreenFactory

@Module
internal abstract class TestActivityModule {
    @Binds abstract fun bindInitialScreenFactory(factory: TestInitialScreenFactory): InitialScreenFactory

    @Module
    companion object {
        @JvmStatic @Provides @ForActivity
        internal fun provideDialogManager(
            screenLifecycleListener: CompositeScreenLifecycleListener
        ) = DialogManager(screenLifecycleListener)

        @JvmStatic @Provides @ForActivity
        fun provideCompositeScreenLifecycleListener() = CompositeScreenLifecycleListener()
    }
}

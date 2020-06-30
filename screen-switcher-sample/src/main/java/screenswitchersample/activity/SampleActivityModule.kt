package screenswitchersample.activity

import com.jaynewstrom.screenswitcher.dialogmanager.DialogManager
import com.jaynewstrom.screenswitcher.screenmanager.CompositeScreenLifecycleListener
import dagger.Module
import dagger.Provides
import screenswitchersample.core.activity.ForActivity
import screenswitchersample.core.activity.InitialScreenFactory

@Module
internal object SampleActivityModule {
    @JvmStatic @Provides
    fun provideInitialScreenFactory(
        initialScreenFactory: SampleInitialScreenFactory
    ): InitialScreenFactory = initialScreenFactory

    @JvmStatic @Provides @ForActivity
    fun provideCompositeScreenLifecycleListener() = CompositeScreenLifecycleListener()

    @JvmStatic @Provides @ForActivity
    internal fun provideDialogManager(
        screenLifecycleListener: CompositeScreenLifecycleListener
    ) = DialogManager(screenLifecycleListener)
}

package screenswitchersample.core.activity

import android.content.Intent
import com.jaynewstrom.screenswitcher.ScreenSwitcherState
import com.jaynewstrom.screenswitcher.screenmanager.CompositeScreenLifecycleListener
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val intent: Intent?) {
    @Provides @ForActivity fun provideScreenSwitcherState(
        lifecycleListener: CompositeScreenLifecycleListener,
        initialScreenFactory: InitialScreenFactory
    ): ScreenSwitcherState {
        return ScreenSwitcherState(lifecycleListener, initialScreenFactory.create(intent))
    }
}

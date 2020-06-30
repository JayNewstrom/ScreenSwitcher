package screenswitchersample.core.activity

import android.content.Intent
import com.jaynewstrom.screenswitcher.Screen

interface InitialScreenFactory {
    fun create(intent: Intent?): List<Screen>
}

package screenswitchersample.core.components

import screenswitchersample.core.leak.LeakWatcher

interface PassthroughComponent {
    val leakWatcher: LeakWatcher
}

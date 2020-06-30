package screenswitchersample.core.leak

interface LeakWatcher {
    fun <T> watch(t: T, description: String)
}

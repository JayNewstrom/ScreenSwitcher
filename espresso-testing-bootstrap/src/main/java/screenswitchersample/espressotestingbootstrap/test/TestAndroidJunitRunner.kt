package screenswitchersample.espressotestingbootstrap.test

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context.KEYGUARD_SERVICE
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP
import android.os.PowerManager.ON_AFTER_RELEASE
import android.util.Log
import androidx.test.espresso.IdlingPolicies
import androidx.test.runner.AndroidJUnitRunner
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
private const val FULL_WAKE_LOCK = android.os.PowerManager.FULL_WAKE_LOCK

@SuppressLint("PrivateApi")
private fun turnOffAnimations() {
    try {
        val method = ValueAnimator::class.java.getDeclaredMethod("setDurationScale", Float::class.javaPrimitiveType)
        method.invoke(null, 0.toFloat())
    } catch (e: Exception) {
        throw RuntimeException("Unable to apply animation speed.", e)
    }
}

class TestAndroidJunitRunner : AndroidJUnitRunner() {
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onStart() {
        turnOffAnimations()
        readyDeviceForTests()
        IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.SECONDS)
        super.onStart()
    }

    @SuppressLint("WakelockTimeout") // Manifest lists DISABLE_KEYGUARD as a permission.
    private fun readyDeviceForTests() {
        val app = targetContext.applicationContext
        val name = TestAndroidJunitRunner::class.java.simpleName

        try {
            // Unlock the device so that the tests can input keystrokes.
            val keyguard = app.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            @Suppress("DEPRECATION")
            keyguard.newKeyguardLock(name).disableKeyguard()
        } catch (e: Exception) {
            Log.d("TestAndroidJunitRunner", "Failed to disable keyguard")
        }

        try {
            // Wake up the screen.
            val power = app.getSystemService(POWER_SERVICE) as PowerManager
            wakeLock = power.newWakeLock(FULL_WAKE_LOCK or ACQUIRE_CAUSES_WAKEUP or ON_AFTER_RELEASE, name)
            wakeLock?.acquire()
        } catch (e: Exception) {
            Log.d("TestAndroidJunitRunner", "Failed to acquire wake lock")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLock?.release()
    }
}

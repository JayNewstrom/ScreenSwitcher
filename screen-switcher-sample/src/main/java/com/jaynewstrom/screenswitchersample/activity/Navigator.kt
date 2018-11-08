package com.jaynewstrom.screenswitchersample.activity

import android.view.View
import com.jaynewstrom.screenswitcher.Screen
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.first.FirstNavigator
import com.jaynewstrom.screenswitchersample.second.SecondNavigator
import com.jaynewstrom.screenswitchersample.second.SecondScreenFactory
import com.jaynewstrom.screenswitchersample.third.ThirdNavigator
import com.jaynewstrom.screenswitchersample.third.ThirdScreenFactory
import javax.inject.Inject

internal class Navigator @Inject constructor() : FirstNavigator, SecondNavigator, ThirdNavigator {
    override fun pushToSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.push(SecondScreenFactory.create(this))
    }

    override fun replaceWithSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.replaceScreenWith(SecondScreenFactory.create(this))
    }

    override fun goToThirdScreen(fromView: View) {
        fromView.screenTransitioner()?.push(ThirdScreenFactory.create(this))
    }

    override fun popToSecondScreen(fromView: View) {
        fromView.screenTransitioner()?.popTo(SecondScreenFactory.create(this))
    }

    override fun viewPdf(fromView: View) {
//        val splitInstallManager = SplitInstallManagerFactory.create(fromView.context)
//        val request = SplitInstallRequest.newBuilder()
//            .addModule(fromView.context.getString(R.string.view_pdf_dynamic_feature_module))
//            .build()
//        splitInstallManager
//            .startInstall(request)
//            .addOnSuccessListener {
//                val context = fromView.context
//                val newContext = context.createPackageContext(context.packageName, 0)
//
//                SplitInstallHelper.loadLibrary(newContext, "libmodpdfium.so")
//                SplitInstallHelper.loadLibrary(newContext, "libmodft2.so")
//                SplitInstallHelper.loadLibrary(newContext, "libjniPdfium.so")
//                SplitInstallHelper.loadLibrary(newContext, "libmodpng.so")

                val clazz = Class.forName("com.jaynewstrom.screenswitchersample.viewpdf.ViewPdfScreenFactory")
                val method = clazz.getMethod("create")
                val screen = method.invoke(null) as Screen
                fromView.screenTransitioner()?.push(screen)
//            }
    }
}

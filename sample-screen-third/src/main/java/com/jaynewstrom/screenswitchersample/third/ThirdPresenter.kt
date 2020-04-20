package com.jaynewstrom.screenswitchersample.third

import android.view.View
import androidx.annotation.LayoutRes
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import javax.inject.Inject

internal class ThirdPresenter private constructor(private val view: View, component: ThirdComponent) {
    companion object {
        @LayoutRes fun layoutId() = R.layout.third_view
        fun bindView(view: View, component: ThirdComponent) = ThirdPresenter(view, component)
    }

    @Inject lateinit var navigator: ThirdNavigator

    init {
        component.inject(this)

        view.findViewById<View>(R.id.btn_pop).setOnClickListener {
            view.screenTransitioner()?.pop()
        }

        view.findViewById<View>(R.id.btn_pop_two).setOnClickListener {
            view.screenTransitioner()?.pop(2)
        }

        view.findViewById<View>(R.id.btn_pop_to_second_screen).setOnClickListener {
            navigator.popToSecondScreen(view)
        }

        view.findViewById<View>(R.id.btn_show_third_dialog).setOnClickListener {
            view.dialogDisplayer()?.show(ThirdScreenDialogFactory())
        }
    }
}

package com.jaynewstrom.screenswitchersample.third

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.core.inflate
import javax.inject.Inject

internal class ThirdPresenter private constructor(private val view: View, component: ThirdComponent) {
    companion object {
        fun createView(context: Context, container: ViewGroup, component: ThirdComponent): View {
            val view = container.inflate(layoutResId = R.layout.third_view, context = context)
            ThirdPresenter(view, component)
            return view
        }
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

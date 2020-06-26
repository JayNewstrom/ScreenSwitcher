package com.jaynewstrom.screenswitchersample.first

import android.view.View
import androidx.annotation.LayoutRes
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import javax.inject.Inject
import javax.inject.Provider

internal class FirstPresenter private constructor(private val view: View, component: FirstComponent) {
    companion object {
        @LayoutRes fun layoutId() = R.layout.first_view
        fun bindView(view: View, component: FirstComponent) = FirstPresenter(view, component)
    }

    @Inject lateinit var dialogFactoryProvider: Provider<FirstDialogFactory>
    @Inject lateinit var navigator: FirstNavigator

    init {
        component.inject(this)

        view.findViewById<View>(R.id.btn_second).setOnClickListener {
            navigator.pushToSecondScreen(view)
        }

        view.findViewById<View>(R.id.btn_replace_with_second).setOnClickListener {
            navigator.replaceWithSecondScreen(view)
        }

        view.findViewById<View>(R.id.btn_show_first_dialog).setOnClickListener {
            view.dialogDisplayer()?.show(dialogFactoryProvider.get())
        }

        view.findViewById<View>(R.id.btn_show_navigate_dialog).setOnClickListener {
            view.dialogDisplayer()?.show(NavigateDialogFactory())
        }
    }
}

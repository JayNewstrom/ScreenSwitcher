package com.jaynewstrom.screenswitchersample.first

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitchersample.core.inflate
import javax.inject.Inject
import javax.inject.Provider

internal class FirstPresenter private constructor(private val view: View, component: FirstComponent) {
    companion object {
        fun createView(context: Context, container: ViewGroup, component: FirstComponent): View {
            val view = container.inflate(layoutResId = R.layout.first_view, context = context)
            FirstPresenter(view, component)
            return view
        }
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
    }
}

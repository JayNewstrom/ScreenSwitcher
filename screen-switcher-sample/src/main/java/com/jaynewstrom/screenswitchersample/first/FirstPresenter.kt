package com.jaynewstrom.screenswitchersample.first

import android.content.Context
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.core.inflate
import com.jaynewstrom.screenswitchersample.second.SecondScreenFactory
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

    init {
        component.inject(this)
        ButterKnife.bind(this, view)
    }

    @OnClick(R.id.btn_second) fun onSecondScreenButtonClicked() {
        view.screenTransitioner()?.push(SecondScreenFactory.create())
    }

    @OnClick(R.id.btn_replace_with_second) fun onReplaceWithSecondScreenButtonClicked() {
        view.screenTransitioner()?.replaceScreenWith(SecondScreenFactory.create())
    }

    @OnClick(R.id.btn_show_first_dialog) fun onShowFirstDialogClicked() {
        view.dialogDisplayer()?.show(dialogFactoryProvider.get())
    }
}

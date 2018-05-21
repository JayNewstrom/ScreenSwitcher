package com.jaynewstrom.screenswitchersample.first

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import butterknife.ButterKnife
import butterknife.OnClick
import com.jaynewstrom.screenswitcher.screenmanager.screenManager
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.second.SecondScreenFactory
import com.jnewstrom.screenswitcher.dialoghub.dialogDisplayer
import javax.inject.Inject
import javax.inject.Provider

internal class FirstView(context: Context, component: FirstComponent) : LinearLayout(context) {
    @Inject lateinit var dialogFactoryProvider: Provider<FirstDialogFactory>

    init {
        orientation = LinearLayout.VERTICAL
        setBackgroundResource(android.R.color.white)
        component.inject(this)
        LayoutInflater.from(context).inflate(R.layout.first_view, this, true)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_second)
    fun onSecondScreenButtonClicked() {
        screenManager()?.push(SecondScreenFactory.create())
    }

    @OnClick(R.id.btn_replace_with_second)
    fun onReplaceWithSecondScreenButtonClicked() {
        screenManager()?.replaceScreenWith(SecondScreenFactory.create())
    }

    @OnClick(R.id.btn_show_first_dialog)
    fun onShowFirstDialogClicked() {
        dialogDisplayer()?.show(dialogFactoryProvider.get())
    }
}

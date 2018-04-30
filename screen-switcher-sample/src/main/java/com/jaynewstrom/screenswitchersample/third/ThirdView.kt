package com.jaynewstrom.screenswitchersample.third

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import butterknife.ButterKnife
import butterknife.OnClick
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.ScreenManager
import com.jaynewstrom.screenswitchersample.second.SecondScreenFactory
import com.jnewstrom.screenswitcher.dialoghub.DialogHub
import javax.inject.Inject

internal class ThirdView(context: Context, component: ThirdComponent) : LinearLayout(context) {
    @Inject lateinit var screenManager: ScreenManager
    @Inject lateinit var dialogHub: DialogHub

    init {
        orientation = LinearLayout.VERTICAL
        setBackgroundResource(android.R.color.holo_green_light)
        component.inject(this)
        LayoutInflater.from(context).inflate(R.layout.third_view, this, true)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_pop) fun onPopButtonClicked() {
        screenManager.pop()
    }

    @OnClick(R.id.btn_pop_two) fun onPopTwoButtonClicked() {
        screenManager.pop(2)
    }

    @OnClick(R.id.btn_pop_to_second_screen) fun onPopToSecondScreenButtonClicked() {
        screenManager.popTo(SecondScreenFactory.create())
    }

    @OnClick(R.id.btn_show_third_dialog) fun onShowThirdDialogClicked() {
        dialogHub.show(ThirdScreenDialogFactory(context))
    }
}

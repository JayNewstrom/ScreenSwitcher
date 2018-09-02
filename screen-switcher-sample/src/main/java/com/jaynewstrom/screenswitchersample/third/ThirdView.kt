package com.jaynewstrom.screenswitchersample.third

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import butterknife.ButterKnife
import butterknife.OnClick
import com.jaynewstrom.screenswitcher.dialogmanager.dialogDisplayer
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.R
import com.jaynewstrom.screenswitchersample.second.SecondScreenFactory

internal class ThirdView(context: Context) : LinearLayout(context) {
    init {
        orientation = LinearLayout.VERTICAL
        setBackgroundResource(android.R.color.holo_green_light)
        LayoutInflater.from(context).inflate(R.layout.third_view, this, true)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_pop) fun onPopButtonClicked() {
        screenTransitioner()?.pop()
    }

    @OnClick(R.id.btn_pop_two) fun onPopTwoButtonClicked() {
        screenTransitioner()?.pop(2)
    }

    @OnClick(R.id.btn_pop_to_second_screen) fun onPopToSecondScreenButtonClicked() {
        screenTransitioner()?.popTo(SecondScreenFactory.create())
    }

    @OnClick(R.id.btn_show_third_dialog) fun onShowThirdDialogClicked() {
        dialogDisplayer()?.show(ThirdScreenDialogFactory(context))
    }
}

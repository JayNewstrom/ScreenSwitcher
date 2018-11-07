package com.jaynewstrom.screenswitchersample.third

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

internal class ThirdPresenter private constructor(private val view: View) {
    companion object {
        fun createView(context: Context, container: ViewGroup): View {
            val view = container.inflate(layoutResId = R.layout.third_view, context = context)
            ThirdPresenter(view)
            return view
        }
    }

    init {
        ButterKnife.bind(this, view)
    }

    @OnClick(R.id.btn_pop) fun onPopButtonClicked() {
        view.screenTransitioner()?.pop()
    }

    @OnClick(R.id.btn_pop_two) fun onPopTwoButtonClicked() {
        view.screenTransitioner()?.pop(2)
    }

    @OnClick(R.id.btn_pop_to_second_screen) fun onPopToSecondScreenButtonClicked() {
        view.screenTransitioner()?.popTo(SecondScreenFactory.create())
    }

    @OnClick(R.id.btn_show_third_dialog) fun onShowThirdDialogClicked() {
        view.dialogDisplayer()?.show(ThirdScreenDialogFactory())
    }
}

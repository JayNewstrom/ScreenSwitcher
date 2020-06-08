package com.jaynewstrom.screenswitchersample.color

import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.jaynewstrom.screenswitcher.screenmanager.screenTransitioner
import com.jaynewstrom.screenswitchersample.core.ViewPresenter
import javax.inject.Inject

internal class ColorPresenter private constructor(view: View, component: ColorComponent) : ViewPresenter(view) {
    companion object {
        @LayoutRes fun layoutId() = R.layout.color_view
        fun bindView(view: View, component: ColorComponent) = ColorPresenter(view, component)
    }

    @Inject @ColorHex lateinit var colorHex: String

    private val newColorEditText = bindView<TextView>(R.id.edit_text)

    init {
        component.inject(this)

        bindView<View>(R.id.color_view).setBackgroundColor(Color.parseColor(colorHex))
        bindView<TextView>(R.id.color_text_view).text = colorHex
        bindClick(R.id.submit_button, ::submitClicked)
    }

    private fun submitClicked() {
        val color = newColorEditText.text.toString()
        try {
            Color.parseColor(color)
            view.screenTransitioner()?.push(ColorScreenFactory.create(color))
        } catch (e: IllegalArgumentException) {
            Toast.makeText(context, "Invalid Color", Toast.LENGTH_LONG).show()
        }
    }
}

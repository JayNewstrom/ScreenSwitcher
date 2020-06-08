package com.jaynewstrom.screenswitcher.tabbar

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat

class BottomNavItemLayout(
    context: Context,
    @DrawableRes icon: Int,
    title: String
) : LinearLayout(context) {
    private val titleTextView: TextView
    private val iconImageView: ImageView
    private val badgeTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.bottom_nav_item, this, true)

        titleTextView = findViewById(R.id.bottom_bar_title_text_view)
        titleTextView.text = title

        iconImageView = findViewById(R.id.bottom_bar_icon_image_view)
        iconImageView.setImageResource(icon)

        badgeTextView = findViewById(R.id.bottom_bar_badge_text_view)

        style()
    }

    private fun style() {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        )

        val colors = intArrayOf(
            Color.WHITE,
            Color.parseColor("#99FFFFFF")
        )

        val colorStateList = ColorStateList(states, colors)
        titleTextView.setTextColor(colorStateList)
        ImageViewCompat.setImageTintList(iconImageView, colorStateList)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        titleTextView.isSelected = selected
        iconImageView.isSelected = selected
    }

    fun setBadgeText(badgeValue: Int) {
        badgeTextView.visibility = if (badgeValue == 0) View.GONE else View.VISIBLE

        val badgeText = if (badgeValue > 99) "99+" else badgeValue.toString()
        badgeTextView.text = badgeText
    }
}

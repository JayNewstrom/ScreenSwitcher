package com.jaynewstrom.screenswitchersample.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(layoutResId: Int, attach: Boolean = false, context: Context = this.context): View {
    return LayoutInflater.from(context).inflate(layoutResId, this, attach)
}

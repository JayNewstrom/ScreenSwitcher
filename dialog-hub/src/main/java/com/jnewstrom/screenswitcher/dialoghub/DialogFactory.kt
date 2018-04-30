package com.jnewstrom.screenswitcher.dialoghub

import android.app.Dialog
import android.content.Context

interface DialogFactory {
    fun createDialog(context: Context): Dialog
}

package com.jaynewstrom.screenswitcher.dialogmanager

import android.app.Dialog
import android.content.Context

interface DialogFactory {
    fun createDialog(context: Context): Dialog
}

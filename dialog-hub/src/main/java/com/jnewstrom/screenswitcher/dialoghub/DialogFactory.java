package com.jnewstrom.screenswitcher.dialoghub;

import android.app.Dialog;
import android.content.Context;

public interface DialogFactory {
    Dialog createDialog(Context context);
}

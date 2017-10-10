package com.jaynewstrom.screenswitchersample.base;

import com.jnewstrom.screenswitcher.dialoghub.DialogHub;

public interface ParentComponent {
    ScreenManager getScreenManager();

    DialogHub getDialogHub();

    LeakWatcher getLeakWatcher();
}

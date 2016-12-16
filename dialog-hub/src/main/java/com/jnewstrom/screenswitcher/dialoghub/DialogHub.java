package com.jnewstrom.screenswitcher.dialoghub;

import android.app.Activity;
import android.app.Dialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class DialogHub {
    private List<DialogInformation> dialogInformationList;
    private Activity activity;

    public DialogHub() {
        dialogInformationList = new ArrayList<>();
    }

    public void attachActivity(Activity activity) {
        this.activity = activity;
    }

    public void dropActivity(Activity activity) {
        if (this.activity == activity) {
            this.activity = null;
        }
    }

    public void show(DialogFactory dialogFactory) {
        if (activity != null) {
            Dialog dialog = dialogFactory.createDialog(activity);
            dialog.show();
            dialogInformationList.add(new DialogInformation(dialog, dialogFactory));
        } else {
            throw new IllegalStateException("DialogHub is not attached to the activity.");
        }
    }

    public Object saveState() {
        List<DialogFactory> items = new ArrayList<>();
        for (DialogInformation information : dialogInformationList) {
            Dialog dialog = information.dialogWeakReference.get();
            if (dialog != null && dialog.isShowing()) {
                items.add(information.dialogFactory);
                dialog.dismiss();
            }
        }
        return items;
    }

    public void restoreState(Object objectState) {
        if (objectState == null) {
            return;
        }
        dialogInformationList = new ArrayList<>();
        //noinspection unchecked
        for (DialogFactory dialogFactory : (List<DialogFactory>) objectState) {
            show(dialogFactory);
        }
    }

    private static final class DialogInformation {
        final WeakReference<Dialog> dialogWeakReference;
        final DialogFactory dialogFactory;

        DialogInformation(Dialog dialog, DialogFactory factory) {
            this.dialogWeakReference = new WeakReference<>(dialog);
            this.dialogFactory = factory;
        }
    }
}

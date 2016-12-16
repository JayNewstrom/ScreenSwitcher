package com.jnewstrom.screenswitcher.dialoghub;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

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
        show(dialogFactory, null);
    }

    private void show(DialogFactory dialogFactory, Bundle savedState) {
        if (activity != null) {
            Dialog dialog = dialogFactory.createDialog(activity);
            if (savedState != null) {
                dialog.onRestoreInstanceState(savedState);
            }
            dialog.show();
            dialogInformationList.add(new DialogInformation(dialog, dialogFactory));
        } else {
            throw new IllegalStateException("DialogHub is not attached to the activity.");
        }
    }

    public Object saveState() {
        List<SavedDialogFactory> items = new ArrayList<>();
        for (DialogInformation information : dialogInformationList) {
            Dialog dialog = information.dialogWeakReference.get();
            if (dialog != null && dialog.isShowing()) {
                items.add(new SavedDialogFactory(information.dialogFactory, dialog.onSaveInstanceState()));
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
        for (SavedDialogFactory savedDialogFactory : (List<SavedDialogFactory>) objectState) {
            show(savedDialogFactory.dialogFactory, savedDialogFactory.savedState);
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

    private static final class SavedDialogFactory {
        final DialogFactory dialogFactory;
        final Bundle savedState;

        SavedDialogFactory(DialogFactory dialogFactory, Bundle savedState) {
            this.dialogFactory = dialogFactory;
            this.savedState = savedState;
        }
    }
}

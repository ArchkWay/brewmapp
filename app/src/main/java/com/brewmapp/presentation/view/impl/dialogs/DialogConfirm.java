package com.brewmapp.presentation.view.impl.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.brewmapp.R;

/**
 * Created by Kras on 07.12.2017.
 */
@SuppressLint("ValidFragment")
public class DialogConfirm extends DialogFragment {

    private OnConfirm onConfirm;
    private String text;

    public DialogConfirm(String text,FragmentManager supportManagerFragment, OnConfirm onConfirm){
        this.text = text;
        this.onConfirm = onConfirm;
        show(supportManagerFragment,"sss");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(R.string.title_confimation);
        alert.setMessage(text);
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            onConfirm.onOk();
        });
        alert.setNegativeButton(android.R.string.no,((dialog, which) -> {
            onConfirm.onCancel();
        }));
        return alert.create();
    }

    public interface OnConfirm {
        void onOk();
        void onCancel();
    }

}

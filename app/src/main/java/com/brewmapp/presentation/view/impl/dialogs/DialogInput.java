package com.brewmapp.presentation.view.impl.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;

import com.brewmapp.R;

/**
 * Created by Kras on 09.12.2017.
 */

public class DialogInput extends DialogFragment {

    private EditText input;
    private OnInputText onInputText;
    private int tittle;
    private String old_text;

    public DialogInput(){

    }

    public void show(FragmentManager supportManagerFragment, int tittle,String old_text,OnInputText onInputText){
        this.onInputText=onInputText;
        this.tittle=tittle;
        this.old_text=old_text;
        show(supportManagerFragment,"qqq");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        input = new EditText(getActivity());
        input.setGravity(Gravity.CENTER);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(old_text);
        alert.setView(input);
        alert.setTitle(tittle);
        alert.setCancelable(false);
        alert.setPositiveButton(android.R.string.ok, (dialog, which) -> onInputText.onOk(input.getText().toString()));
        alert.setNeutralButton(android.R.string.cancel, (dialog, which) -> {});
        return alert.create();
    }

    public interface OnInputText {
        void onOk(String string);
    }

}

package com.brewmapp.presentation.view.impl.dialogs;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by Kras on 29.12.2017.
 */

public class DialogFragmentWait extends DialogFragment {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=new ProgressBar(getActivity());
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener(){
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,android.view.KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    return true; // pretend we've processed it
                }
                else
                    return false; // pass on to be processed as normal
            }
        });

    }

}

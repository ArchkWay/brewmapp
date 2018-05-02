package com.brewmapp.presentation.view.impl.dialogs;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.presentation.view.contract.InfoWindowMap_view;
import com.brewmapp.presentation.view.impl.fragment.MapFragment;
import com.brewmapp.presentation.view.impl.widget.InfoWindowMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;

import java.util.Collection;
import java.util.Iterator;

import ru.frosteye.ovsa.execution.executor.Callback;

/**
 * Created by Kras on 23.02.2018.
 */

public class DialogShowView extends DialogFragment {
    private ViewGroup view;
    private Callback<Void> callBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    public void setView(ViewGroup view) {
        this.view = view;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(callBack!=null)
            callBack.onResult(null);
    }

    public void setCallBack(Callback<Void> callBack) {
        this.callBack = callBack;
    }

    public Callback<Void> getCallBack() {
        return callBack;
    }

    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        super.show(manager, tag);
    }


    @Nullable
    @Override
    public ViewGroup getView() {
        return view;
    }
}

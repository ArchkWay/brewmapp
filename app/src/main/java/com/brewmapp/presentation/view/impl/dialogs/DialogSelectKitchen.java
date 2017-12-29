package com.brewmapp.presentation.view.impl.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.execution.task.KitchenTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;


/**
 * Created by Kras on 29.12.2017.
 */

public class DialogSelectKitchen extends DialogFragment {

    private boolean[] booleans;
    private KitchenTask kitchenTask;
    private ArrayList<String> arrayAdapter=new ArrayList<>();
    private ArrayList<Kitchen> arrayKitchen=new ArrayList<>();
    private OnSelectKitchens onSelectKitchens;
    private DialogFragmentWait dialogFragmentWait;
    private List<Kitchen> resto_kitchen;

    public void showDialog(FragmentManager fragmentManager,OnSelectKitchens onSelectKitchens){
        this.onSelectKitchens=onSelectKitchens;
        kitchenTask=new KitchenTask(BeerMap.getAppComponent().mainThread(),BeerMap.getAppComponent().executor(),BeerMap.getAppComponent().api());
        kitchenTask.execute(null,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                Iterator<IFlexible> iterator=iFlexibles.iterator();
                while (iterator.hasNext()) {
                    Kitchen kitchen=((KitchenInfo) iterator.next()).getModel();
                    arrayAdapter.add(kitchen.getName());
                    arrayKitchen.add(kitchen);
                }
                    dialogFragmentWait.dismiss();
                    show(fragmentManager, "qqq");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dialogFragmentWait.dismiss();
                dismiss();
            }
        });
        dialogFragmentWait=new DialogFragmentWait();
        dialogFragmentWait.show(fragmentManager,"111");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        String[] strings=new String[arrayAdapter.size()];
        arrayAdapter.toArray(strings);
        booleans=new boolean[arrayAdapter.size()];
        if(resto_kitchen!=null)
            for (int i=0;i<arrayKitchen.size();i++){
                for (int j=0;j<resto_kitchen.size();j++){
                    try {
                        if(arrayKitchen.get(i).getName().toLowerCase().equals(resto_kitchen.get(j).getName().toLowerCase()))
                            booleans[i]=true;
                    }catch (Exception e){}
                }
            }
        adb.setMultiChoiceItems(strings, booleans, (dialog, which, isChecked) -> {});
        adb.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            int cnt=0;
            ArrayList<Kitchen> resultKitchen=new ArrayList<>();
            Iterator<Kitchen> iterator=arrayKitchen.iterator();
            while (iterator.hasNext()){
                Kitchen kitchen=iterator.next();
                if(booleans[cnt++])
                    resultKitchen.add(kitchen);
            }
            onSelectKitchens.onSelect(resultKitchen);
            dismiss();
        });
        adb.setNeutralButton(android.R.string.cancel, (dialog, which) -> {
            dismiss();
        });

        return adb.create();
    }

    public DialogSelectKitchen setKitchen(List<Kitchen> resto_kitchen) {
        this.resto_kitchen=resto_kitchen;
        return this;
    }

    public interface OnSelectKitchens{
        void onSelect(ArrayList<Kitchen> kitchenArrayList);
    }

}

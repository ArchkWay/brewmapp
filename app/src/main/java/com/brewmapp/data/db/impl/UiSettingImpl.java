package com.brewmapp.data.db.impl;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.UiSettingContainer;

import javax.inject.Inject;

import ru.frosteye.ovsa.data.storage.BaseRepo;
import ru.frosteye.ovsa.data.storage.Storage;

/**
 * Created by Kras on 13.10.2017.
 */

public class UiSettingImpl extends BaseRepo<UiSettingContainer> implements UiSettingRepo {

    private UiSettingContainer uiSettingContainer;

    @Inject
    public UiSettingImpl(Storage storage) {
        super(storage);
        uiSettingContainer =load();
        if(uiSettingContainer ==null){
            uiSettingContainer =new UiSettingContainer();}
    }

    @Override
    protected Class<UiSettingContainer> provideClass() {
        return UiSettingContainer.class;
    }


    @Override
    public int getnActiveFragment() {
        return uiSettingContainer.getnActiveFragment();
    }

    @Override
    public void setActiveFragment(int nActiveFragment) {
        uiSettingContainer.setnActiveFragment(nActiveFragment);
        save(uiSettingContainer);
    }

    @Override
    public void setnActiveTabEventFragment(int nActiveTabEventFragment) {
        uiSettingContainer.setnActiveTabEventFragment(nActiveTabEventFragment);
        save(uiSettingContainer);

    }

    @Override
    public int getnActiveTabEventFragment() {
        return uiSettingContainer.getnActiveTabEventFragment();
    }
}

package com.brewmapp.data.db.contract;

import com.brewmapp.data.entity.UiSettingContainer;

import ru.frosteye.ovsa.data.storage.Repo;

/**
 * Created by Kras on 13.10.2017.
 */

public interface UiSettingRepo extends Repo<UiSettingContainer> {
    int getnActiveFragment();
    void setActiveFragment(int nActiveFragment);
    void setnActiveTabEventFragment(int nActiveTabEventFragment );
    int getnActiveTabEventFragment();
}

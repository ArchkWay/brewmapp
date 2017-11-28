package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.view.contract.FilterMapView;

import java.util.List;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by nlbochas on 28/10/2017.
 */

public interface FilterMapPresenter extends LivePresenter<FilterMapView> {
    void loadFilterResult(List<FilterRestoField> fieldList, int specialOffer);
    void storeTabActive(int position);}

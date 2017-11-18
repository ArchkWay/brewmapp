package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.presentation.view.contract.FilterMapView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by nlbochas on 28/10/2017.
 */

public interface FilterMapPresenter extends LivePresenter<FilterMapView> {
    void loadFilterResult(FilterRestoPackage filterRestoPackage);
}

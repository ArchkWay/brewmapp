package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.data.model.ILocation;
import com.brewmapp.data.pojo.GeolocatorResultPackage;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface PickLocationView extends BasicView {
    void showVariants(List<? extends ICommonItem> restos);
    void showGeolocationResult(GeolocatorResultPackage resultPackage);
    void enableSearchPanel(boolean enabled);
}

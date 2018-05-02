package com.brewmapp.presentation.view.contract;

import android.view.View;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 24.08.2017.
 */

public interface MapFragment_view extends BasicView {
    void addRestoToMap(List<FilterRestoLocationInfo> list);
    void showDialogProgressBar(boolean show);
    void showProgressBar();
    void hideProgressBar();
    void appendItems(List<IFlexible> list);

    void commonError(String message);
}

package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.FilterRestoLocation;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface ResultSearchActivityView extends BasicView {
    void showDialogProgressBar(int message);
    void hideProgressBar();
    void appendItems(List<IFlexible> list);
    void commonError(String... strings);

    void setRestoLocations(List<FilterRestoLocation> filterRestoLocations);
}

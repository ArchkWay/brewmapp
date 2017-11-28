package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.model.ILikeable;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface EventsView extends BasicView, RefreshableView{
    int MODE_EVENTS = 0;
    int MODE_SALES = 1;
    int MODE_NEWS = 2;
    void appendItems(List<IFlexible> list);
    void setTabActive(int i);
}

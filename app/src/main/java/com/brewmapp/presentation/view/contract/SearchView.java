package com.brewmapp.presentation.view.contract;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface SearchView extends BasicView {
    void showDialogProgressBar(int message);
    void hideProgressBar();
    void appendItems(List<IFlexible> list);
}

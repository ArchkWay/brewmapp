package com.brewmapp.presentation.view.contract;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by nixus on 01.11.2017.
 */

public interface SelectCategoryActivityView extends BasicView {
    void appendItems(List<IFlexible> list);
    void showProgressBar(boolean show);
    void commonError(String... strings);
}

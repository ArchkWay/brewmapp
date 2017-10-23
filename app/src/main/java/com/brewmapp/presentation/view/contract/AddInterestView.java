package com.brewmapp.presentation.view.contract;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 10/20/2017.
 */

public interface AddInterestView extends BasicView{

    void appendItems(List<IFlexible> s);
    void onError();

}

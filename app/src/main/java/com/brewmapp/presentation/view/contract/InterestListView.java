package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Resto;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 20.10.2017.
 */

public interface InterestListView extends BasicView {
    void appendItems(List<IFlexible> iFlexibles);
    void onError();
    void refreshItems();

    void addOneItemBeer(Beer model);

    void addOneItemResto(Resto resto);
}

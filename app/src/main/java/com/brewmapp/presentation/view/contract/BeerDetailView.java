package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.BeerDetail;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 30.10.2017.
 */

public interface BeerDetailView extends BasicView {
    void setModel(BeerDetail beerDetail, int mode);
    void commonError(String... strings);

    void setFavorite(boolean b);

    void addItemsReviews(List<IFlexible> iFlexibles);

    void addItemsWhereTheyPour(ArrayList<IFlexible> iFlexibles);

    void addItemsAddedToFavorite(List<IFlexible> iFlexibles);

    void setProductAverageValue(List<Averagevalue> models);
}

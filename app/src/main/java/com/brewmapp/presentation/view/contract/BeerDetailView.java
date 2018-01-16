package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.BeerDetail;
import com.brewmapp.data.entity.wrapper.RestoInfo;

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

    void setReviews(List<IFlexible> iFlexibles);

    void addItemsResto(ArrayList<IFlexible> iFlexibles);

    void addItemsInterest(List<IFlexible> iFlexibles);

    void setProductAverageValue(List<Averagevalue> models);
}

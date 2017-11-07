package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.RestoDetail;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoDetailView extends BasicView{
    void setModel(RestoDetail restoDetail);

    void commonError(String... string);

    void SubscriptionExist(boolean b);

    void setReviews(List<IFlexible> iFlexibles);

    void setCnt(int i, int size);

    void setFav(boolean b);
}

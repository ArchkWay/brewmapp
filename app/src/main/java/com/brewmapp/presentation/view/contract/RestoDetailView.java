package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.AverageEvaluation;
import com.brewmapp.data.entity.RestoDetail;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoDetailView extends BasicView{
    int ACTION_SCROLL_TO_NEWS = 1;
    int ACTION_SCROLL_TO_ADD_REVIEWS = 2;


    void setModel(RestoDetail restoDetail, int mode);

    void commonError(String... string);

    void SubscriptionExist(boolean b);

    void setReviews(List<IFlexible> iFlexibles);

    void setCnt(int i, int size);

    void setFav(boolean b);

    void AverageEvaluation(List<AverageEvaluation> evaluations);

    void addItemsAddedToFavorite(List<IFlexible> iFlexibles);


}

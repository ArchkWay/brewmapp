package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.container.RestoDetails;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoDetailView extends BasicView{
    void setModel(RestoDetail restoDetail);

    void commonError(String... string);

    void SubscriptionExist(boolean b);

}

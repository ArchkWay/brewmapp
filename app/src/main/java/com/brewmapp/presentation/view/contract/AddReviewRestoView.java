package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.RestoDetail;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 03.11.2017.
 */

public interface AddReviewRestoView  extends BasicView {

    void setModel(RestoDetail restoDetail);
    void commonError(String... strings);
}

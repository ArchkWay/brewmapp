package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 10.12.2017.
 */

public interface RestoEditFragmentView extends BasicView {
    void setContent(RestoDetail restoDetail);
    void commonError(String... strings);
}

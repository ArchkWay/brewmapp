package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.presentation.view.contract.RestoEditFragmentView;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectAddress;
import com.daimajia.slider.library.SliderLayout;

import java.util.ArrayList;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 10.12.2017.
 */

public interface RestoEditFragmentPresenter extends LivePresenter<RestoEditFragmentView> {
    void requestContent(String id_resto);

    void loadAllPhoto(SliderLayout sliderLayout);

    boolean isNeedSave();

    void storeChanges();

    RestoDetail getRestoDetail_new_data();


}

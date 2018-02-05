package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Brewery;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 04.02.2018.
 */

public interface BreweryDetailsActivityView extends BasicView {
    void commonError(String... strings);

    void setContent(Brewery brewery);
}

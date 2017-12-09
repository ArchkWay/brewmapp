package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Beer;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 12/8/2017.
 */

public interface BeerEditFragmentView  extends BasicView{
    void commonError(String... strings);

    void setContent(Beer model);
}

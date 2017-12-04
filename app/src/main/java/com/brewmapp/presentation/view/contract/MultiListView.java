package com.brewmapp.presentation.view.contract;

import com.brewmapp.execution.task.LoadRestoDetailTask;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 10/20/2017.
 */

public interface MultiListView extends BasicView{
    int MODE_ACTIVTY_SHOW_AND_SELECT_BEER=0;
    int MODE_ACTIVTY_SHOW_AND_SELECT_RESTO=1;
    int MODE_ACTIVTY_SHOW_HASHTAG=2;
    int MODE_ACTIVTY_ERROR=3;
    int MODE_ACTIVTY_SHOW_AND_SELECT_FRIENDS =4;

    void appendItems(List<IFlexible> s);
    void onError();
    void commonError(String... strings);

}

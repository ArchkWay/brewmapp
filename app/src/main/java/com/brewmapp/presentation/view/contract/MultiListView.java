package com.brewmapp.presentation.view.contract;

import com.brewmapp.execution.task.LoadRestoDetailTask;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 10/20/2017.
 */

public interface MultiListView extends BasicView{
    String MODE_SHOW_AND_SELECT_BEER ="0";
    String MODE_SHOW_AND_SELECT_RESTO ="1";
    String MODE_SHOW_HASHTAG ="2";
    String MODE_ACTIVTY_ERROR="3";
    String MODE_SHOW_AND_SELECT_FRIENDS ="4";
    String MODE_SHOW_REVIEWS_BEER ="5";
    String MODE_SHOW_REVIEWS_RESTO ="6";
    String MODE_SHOW_ALL_MY_RATING ="7";
    void appendItems(List<IFlexible> s);
    void onError();
    void commonError(String... strings);

}

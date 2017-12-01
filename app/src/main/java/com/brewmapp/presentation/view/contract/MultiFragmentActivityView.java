package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.User;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 11/20/2017.
 */

public interface MultiFragmentActivityView extends BasicView{
    void commonError(String... strings);

    String MODE_ABOUT="0";
    String MODE_HELP="1";

    void setContent(String modeContent);
}

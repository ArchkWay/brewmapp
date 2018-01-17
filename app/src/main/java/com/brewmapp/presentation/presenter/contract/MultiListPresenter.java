package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.task.LoadRestoDetailTask;
import com.brewmapp.presentation.view.contract.MultiListView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 10/20/2017.
 */

public interface MultiListPresenter extends LivePresenter<MultiListView> {

    void sendQueryFullSearch(FullSearchPackage fullSearchPackage);

    String parseIntent(Intent intent);

    void sentQueryQuickSearch(FullSearchPackage fullSearchPackage);

}

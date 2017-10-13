package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.presentation.view.contract.MainView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 02.08.2017.
 */

public interface MainPresenter extends LivePresenter<MainView> {
    void onLogout();

    int getActiveFragment();
}

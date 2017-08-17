package ru.frosteye.beermap.presentation.presenter.contract;

import ru.frosteye.beermap.presentation.view.contract.MainView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 02.08.2017.
 */

public interface MainPresenter extends LivePresenter<MainView> {
    void onLogout();
}

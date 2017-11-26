package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.brewmapp.presentation.view.contract.MainView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 02.08.2017.
 */

public interface MainPresenter extends LivePresenter<MainView> {
    void onLogout();

    int getActiveFragment();

    String parseMode(Intent intent);

    Bundle prepareArguments(Intent intent, FrameLayout container);
}

package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.presentation.presenter.contract.MultiFragmentActivityPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.UiCustomControl;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.fragment.BeerEditFragment;
import com.brewmapp.presentation.view.impl.fragment.SimpleFragment.AboutFragment;
import com.brewmapp.presentation.view.impl.fragment.SimpleFragment.WebViewFragment;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;



/**
 * Created by xpusher on 11/20/2017.
 */

public class MultiFragmentActivityPresenterImpl extends BasePresenter<MultiFragmentActivityView> implements MultiFragmentActivityPresenter,UiCustomControl{

    @Inject    public MultiFragmentActivityPresenterImpl(){}

    @Override
    public void onDestroy() {

    }

    @Override
    public void parseIntent(Intent intent) {
        try {
            switch (intent.getAction()){
                case MultiFragmentActivityView.MODE_ABOUT:
                    view.showFragment(new AboutFragment());
                    break;
                case MultiFragmentActivityView.MODE_WEBVIEW:
                    view.showFragment(new WebViewFragment());
                    break;
                case MultiFragmentActivityView.MODE_BEER_EDIT:
                        view.showFragment(new BeerEditFragment());
                    break;
                default:
                    view.commonError();return;
            }
        } catch (Exception e){
            view.commonError(e.getMessage());return;
        }
    }

}

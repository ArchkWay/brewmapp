package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.presentation.presenter.contract.MultiFragmentActivityPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.fragment.BeerEditFragment;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatFragment;
import com.brewmapp.presentation.view.impl.fragment.RestoEditFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.AboutFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.CreateBeerFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.CreateRestoFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.OwnerFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.WebViewFragment;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;



/**
 * Created by xpusher on 11/20/2017.
 */

public class MultiFragmentActivityPresenterImpl extends BasePresenter<MultiFragmentActivityView> implements MultiFragmentActivityPresenter{

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
                case MultiFragmentActivityView.MODE_RESTO_EDIT:
                    view.showFragment(new RestoEditFragment());
                    break;
                case MultiFragmentActivityView.MODE_CHAT:
                    view.showFragment(new ChatFragment());
                    break;
                case MultiFragmentActivityView.MODE_FORM_I_OWNER:
                    view.showFragment(new OwnerFragment());
                    break;
                case MultiFragmentActivityView.MODE_ADD_RESTO:
                    view.showFragment(new CreateRestoFragment());
                    break;
                case MultiFragmentActivityView.MODE_ADD_BEER:
                    view.showFragment(new CreateBeerFragment());
                    break;
                default:
                    view.commonError();return;
            }
        } catch (Exception e){
            view.commonError(e.getMessage());return;
        }
    }

}

package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragmentPresenterImpl extends BasePresenter<SearchAllView> implements SearchFragmentPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;
    private LoadCityTask loadCityTask;
    private String ActiveTab;

    @Inject
    public SearchFragmentPresenterImpl(Context context, UiSettingRepo uiSettingRepo,LoadCityTask loadCityTask) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
        this.loadCityTask = loadCityTask;
    }

    @Override
    public void onAttach(SearchAllView searchAllView) {
        super.onAttach(searchAllView);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void setTabActive(String newTabActive) {
        ActiveTab=newTabActive;

        if(!Paper.book().contains(SearchFragment.CATEGORY_LIST_RESTO))
            Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO,FilterRestoField.createDefault());
        if(!Paper.book().contains(SearchFragment.CATEGORY_LIST_BEER))
            Paper.book().write(SearchFragment.CATEGORY_LIST_BEER,FilterBeerField.createDefault());
        if(!Paper.book().contains(SearchFragment.CATEGORY_LIST_BREWERY))
            Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY,FilterBreweryField.createDefault());

        switch (ActiveTab){
            case SearchFragment.CATEGORY_LIST_RESTO:
                view.showRestoFilters(Paper.book().read(ActiveTab));
                break;
            case SearchFragment.CATEGORY_LIST_BEER:
                view.showBeerFilters(Paper.book().read(ActiveTab));
                break;
            case SearchFragment.CATEGORY_LIST_BREWERY:
                view.showBreweryFilters(Paper.book().read(ActiveTab));
                break;
        }

    }

}

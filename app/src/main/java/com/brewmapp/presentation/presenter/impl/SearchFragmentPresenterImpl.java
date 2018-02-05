package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import java.util.List;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragmentPresenterImpl extends BasePresenter<SearchAllView> implements SearchFragmentPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;

    @Inject
    public SearchFragmentPresenterImpl(Context context, UiSettingRepo uiSettingRepo) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
    }

    @Override
    public void onAttach(SearchAllView searchAllView) {
        super.onAttach(searchAllView);
        Paper.init(context);
        if (Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO) == null) {
            Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, FilterRestoField.createDefault(context));
        }
        if (Paper.book().read(SearchFragment.CATEGORY_LIST_BEER) == null) {
            Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, FilterBeerField.createDefault(context));
        }
        if (Paper.book().read(SearchFragment.CATEGORY_LIST_BREWERY) == null) {
            Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, FilterBreweryField.createDefault(context));
        }

        if (uiSettingRepo.getnActiveFragment() == 0 || uiSettingRepo.getnActiveFragment() == -1) {
            view.showRestoFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO));
        } else if (uiSettingRepo.getnActiveFragment() == 1) {
            view.showBeerFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_BEER));
        } else if (uiSettingRepo.getnActiveFragment() == 2) {
            view.showBreweryFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_BREWERY));
        }
        searchAllView.setTabActive(uiSettingRepo.getnActiveTabEventFragment());
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void storeTabActive(int position) {
        uiSettingRepo.setnActiveTabEventFragment(position);
        selectTab(position);
    }

    private void selectTab(int position) {
        switch (position) {
            case 0:
                view.showRestoFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO));
                break;
            case 1:
                view.showBeerFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_BEER));
                break;
            case 2:
                view.showBreweryFilters(Paper.book().read(SearchFragment.CATEGORY_LIST_BREWERY));
                break;
        }
    }

    @Override
    public void saveRestoFilterChanges(List<FilterRestoField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, fields)).start();
    }

    @Override
    public void saveBeerFilterChanges(List<FilterBeerField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, fields)).start();
    }

    @Override
    public void saveBreweryFilterChanges(List<FilterBreweryField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, fields)).start();
    }
}

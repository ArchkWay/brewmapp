package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.presenter.contract.SearchAllPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;

import java.util.List;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchAllPresenterImpl extends BasePresenter<SearchAllView> implements SearchAllPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;

    @Inject
    public SearchAllPresenterImpl(Context context, UiSettingRepo uiSettingRepo) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
    }

    @Override
    public void onAttach(SearchAllView searchAllView) {
        super.onAttach(searchAllView);
        Paper.init(context);
        if (Paper.book().read("restoCategoryList") == null) {
            Paper.book().write("restoCategoryList", FilterRestoField.createDefault(context));
        }
        if (Paper.book().read("beerCategoryList") == null) {
            Paper.book().write("beerCategoryList", FilterBeerField.createDefault(context));
        }
        if (Paper.book().read("breweryCategoryList") == null) {
            Paper.book().write("breweryCategoryList", FilterBreweryField.createDefault(context));
        }

        if (uiSettingRepo.getnActiveFragment() == 0 || uiSettingRepo.getnActiveFragment() == -1) {
            view.showRestoFilters(Paper.book().read("restoCategoryList"));
        } else if (uiSettingRepo.getnActiveFragment() == 1) {
            view.showBeerFilters(Paper.book().read("beerCategoryList"));
        } else if (uiSettingRepo.getnActiveFragment() == 2) {
            view.showBreweryFilters(Paper.book().read("breweryCategoryList"));
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
                view.showRestoFilters(Paper.book().read("restoCategoryList"));
                break;
            case 1:
                view.showBeerFilters(Paper.book().read("beerCategoryList"));
                break;
            case 2:
                view.showBreweryFilters(Paper.book().read("breweryCategoryList"));
                break;
        }
    }

    @Override
    public void saveRestoFilterChanges(List<FilterRestoField> fields) {
        new Thread(() -> Paper.book().write("restoCategoryList", fields)).start();
    }

    @Override
    public void saveBeerFilterChanges(List<FilterBeerField> fields) {
        new Thread(() -> Paper.book().write("beerCategoryList", fields)).start();
    }

    @Override
    public void saveBreweryFilterChanges(List<FilterBreweryField> fields) {
        new Thread(() -> Paper.book().write("breweryCategoryList", fields)).start();
    }
}
